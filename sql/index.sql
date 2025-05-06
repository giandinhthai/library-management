-- Begin analyzing SELECT queries
explain analyse SELECT bi.*
                FROM borrow_items bi
                         JOIN borrows b ON bi.borrow_id = b.borrow_id
                WHERE  bi.book_id IN ('f9e94640-2c9b-46a3-be96-cf30c5ac5209')
                  AND b.member_id = 'efe2bb23-ca50-58c9-8fa0-deb73ac8681c'

                  AND bi.is_returned = false;

explain analyse      SELECT bi.*
                     FROM borrow_items bi
                              JOIN borrows b ON bi.borrow_id = b.borrow_id
                     WHERE bi.book_id IN ('f9e94640-2c9b-46a3-be96-cf30c5ac5209')
                       AND bi.is_returned = false
                       AND b.member_id = 'efe2bb23-ca50-58c9-8fa0-deb73ac8681c';

drop index if exists borrow_items_book_id_index;
create index borrow_items_book_id_index on borrow_items(book_id);

explain analyse SELECT bi.*
                FROM borrow_items bi
                WHERE bi.book_id = 'f9e94640-2c9b-46a3-be96-cf30c5ac5209';

explain analyse SELECT b.*
                FROM borrows b
                WHERE b.member_id = 'de96a20e-19fd-593f-b699-962f22938613'
                  AND COALESCE('ACTIVE', b.status) = b.status;

create index borrows_member_index on borrows(member_id);
drop index if exists borrows_member_index;

explain analyse  SELECT r.*
                 FROM reservations r
                 WHERE r.book_id = 'dcc04d96-56fe-481f-a4db-ecd5c340653f'
                   AND r.status = 'PENDING'
                 order by r.reserved_at
                 limit 100;

drop index if exists reserve_at_idx;
drop index if exists book_reserve_idx;
create index reserve_at_idx on reservations(reserved_at);
create index book_reserve_idx on reservations(book_id,reserved_at) where status = 'PENDING';


explain analyse SELECT *
                FROM (
                         SELECT r.*,
                                ROW_NUMBER() OVER (PARTITION BY r.book_id ORDER BY r.reserved_at ASC, r.reservation_id ASC) as rn
                         FROM reservations r
                         WHERE r.status = 'PENDING'
                           AND r.book_id IN (:bookIds)
                     ) t
                WHERE t.rn = 1;

explain analyse SELECT *
                FROM reservations r
                WHERE r.book_id = :bookId
                  AND r.member_id = :memberId
                  AND r.status = 'READY_FOR_PICKUP';

explain analyse SELECT *
                FROM reservations r
                WHERE r.member_id = :memberId
                  AND r.book_id = :bookId
                  AND r.status = 'READY_FOR_PICKUP';
create index ready_book_member_idx on reservations(book_id,member_id) ;
drop index if exists ready_book_member_idx;


BEGIN;
EXPLAIN ANALYZE
INSERT INTO reservations (reservation_id, book_id, member_id, status, reserved_at, expires_at)
VALUES
    (gen_random_uuid(), -- Automatically generate a UUID for reservation_id
     'ed042083-5f18-4e69-9b02-0f10dd76cc60',  -- Replace with a real UUID for book_id
     'feff124f-18b4-5bef-854c-007c57413674',  -- Replace with a real UUID for member_id
     'PENDING',  -- Must be one of the allowed statuses
     CURRENT_TIMESTAMP,   -- Use the current timestamp for reserved_at
     NULL);               -- You can provide an expires_at value or leave NULL

ROLLBACK;

explain analyse SELECT r
                FROM reservations r
                WHERE r.book_id = :bookId;

-- Final Indexes to keep:

CREATE INDEX borrow_items_book_id_index ON borrow_items(book_id);

CREATE INDEX borrows_member_index ON borrows(member_id);

-- CREATE INDEX reserve_at_idx ON reservations(reserved_at);

CREATE INDEX book_reserve_idx ON reservations(book_id, reserved_at) WHERE status = 'PENDING';

-- CREATE INDEX ready_book_member_idx ON reservations(book_id, member_id);
create index ready_book_member_idx on reservations(member_id,book_id) ;