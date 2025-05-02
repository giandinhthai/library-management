package com.example.BorrowBookService.aggregate;

import com.example.BorrowBookService.event.BooksBorrowedEvent;
import com.example.BorrowBookService.event.BooksReservedEvent;
import com.example.BorrowBookService.event.BooksReturnedEvent;
import com.example.BorrowBookService.exception.UnvalidBorrowRequestException;
import com.example.BorrowBookService.exception.UnvalidReservationRequestException;
import com.example.BorrowBookService.exception.UnvalidReturnRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "members")
@Getter
public class Member extends AbstractAggregateRoot<Member> {
    @Id
    private UUID memberId;
    private String email;
    private int reputation;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipTier tier;

    @Column(name = "outstanding_fines")
    private int outstandingFines;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Borrow> borrows = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Reservation> reservations = new ArrayList<>();

    public static final int MAX_OUTSTANDING_FINE = 100000;
    public static final int INITIAL_REPUTATION = 50;
    public static final int MONTHLY_REPUTATION_INCREASE = 5;
    public static final int MINOR_OVERDUE_PENALTY = 5;  // For 1-7 days overdue
    public static final int MAJOR_OVERDUE_PENALTY = 20; // For >7 days overdue
    public static final int OVERDUE_PENALTY_DIFFERENCE = MAJOR_OVERDUE_PENALTY - MINOR_OVERDUE_PENALTY;
    public static final int MAX_REPUTATION = 100;
    public static final int MIN_REPUTATION = 0;
    public static final int CONTINUOUS_BORROW_WAITING_DAYS = 2;
    public static final int BRONZE_MAX_REPUTATION = 40;
    public static final int SILVER_MAX_REPUTATION = 80;

    private Member(UUID uuid, String email) {
        this.memberId = uuid;
        this.email = email;
        this.reputation = INITIAL_REPUTATION;
        this.tier = MembershipTier.BRONZE;
        this.outstandingFines = 0;
    }

    protected Member() {

    }

    // Domain methods
    public static Member create(String email) {
        return new Member(UUID.randomUUID(), email);
    }


    public void updateReputation(int change) {
        reputation += change;
        if (reputation < MIN_REPUTATION) {
            reputation = MIN_REPUTATION;
        }
        if (reputation > MAX_REPUTATION) {
            reputation = MAX_REPUTATION;
        }
        updateMembershipTier();
    }

    private void updateMembershipTier() {

        if (reputation <= BRONZE_MAX_REPUTATION) {
            tier = MembershipTier.BRONZE;
        } else if (reputation <= SILVER_MAX_REPUTATION) {
            tier = MembershipTier.SILVER;
        } else {
            tier = MembershipTier.GOLD;
        }
    }

    public int getMaxBorrowingLimit() {
        return switch (tier) {
            case BRONZE -> 0;
            case SILVER -> 2;
            case GOLD -> 4;
        };
    }

    public int getMaxReservationLimit() {
        return switch (tier) {
            case BRONZE -> 0;
            case SILVER -> 1;
            case GOLD -> 2;
        };
    }

    public void addFine(int amount) {
        outstandingFines += amount;
    }

    public void payFine(int amount) {
        outstandingFines -= amount;
        if (outstandingFines < 0) {
            outstandingFines = 0;
        }
    }

    public void monthlyReputationUpdate() {
        updateReputation(MONTHLY_REPUTATION_INCREASE);
    }

    public void applyOverdueReturnPenalty(int daysOverdue) {
        if (daysOverdue > 7) {
            updateReputation(-OVERDUE_PENALTY_DIFFERENCE);
        } else if (daysOverdue > 0) {
            updateReputation(-MINOR_OVERDUE_PENALTY);
        }
    }


    public Borrow borrow(List<UUID> bookUUIDs, List<Integer> booksPrice) {
        checkCanBorrow(bookUUIDs);
        Borrow borrow = Borrow.create(this, bookUUIDs, booksPrice);
        this.borrows.add(borrow);
        registerEvent(new BooksBorrowedEvent(memberId,bookUUIDs));
        return borrow;
    }
    public void returnBook(List<UUID> bookUUIDs) {
        checkValidReturn(bookUUIDs);
        for(Borrow borrow : borrows){
            if(borrow.getStatus() == BorrowStatus.COMPLETED){
                continue;
            }
            borrow.getBorrowItems().stream()
                    .filter(item -> !item.isReturned() && bookUUIDs.contains(item.getBookId()))
                    .forEach(item -> {
                        item.processReturn();
                        borrow.changeTotalFineAmount(item.getFineAmount());
                        this.addFine(item.getFineAmount());
                    });

            borrow.markCompletedIfAllReturned();
        }
        registerEvent(new BooksReturnedEvent(memberId,bookUUIDs));
    }
    public List<Reservation> reserve(List<UUID> bookUUIDs) {
        checkCanReserve(bookUUIDs);
        List<Reservation> reservations = new ArrayList<>();
        for (UUID bookUUID : bookUUIDs) {
            reservations.add(Reservation.create(bookUUID, this));
        }
        this.reservations.addAll(reservations);
        registerEvent(new BooksReservedEvent(memberId,bookUUIDs));
        return reservations;
    }

    private void checkCanReserve(List<UUID> bookUUIDs) {
        if (bookUUIDs.isEmpty()) {
            throw new UnvalidReservationRequestException("Cannot reserve an empty list of books");
        }
        if (hasDuplicateBook(bookUUIDs)) {
            throw new UnvalidReservationRequestException("Cannot reserve the same book multiple times");
        }
        if (outstandingFines > MAX_OUTSTANDING_FINE) {
            throw new UnvalidReservationRequestException("Member currently has outstanding fines: " + outstandingFines + " which exceeds the limit of " + MAX_OUTSTANDING_FINE);
        }
        List<UUID> activeReservations = getActiveReservations();
        Set<UUID> currentlyBorrowedBookIds = getCurrentlyBorrowedBookIds();
        for (UUID bookId : bookUUIDs) {
            if (currentlyBorrowedBookIds.contains(bookId)) {
                throw new UnvalidReservationRequestException("Member has currently borrow book: " + bookId);
            }
            if (activeReservations.contains(bookId)) {
                throw new UnvalidReservationRequestException("Member has already reserved book: " + bookId);
            }
        }

        int activeReservationCount = activeReservations.size();
        if (activeReservationCount + bookUUIDs.size() > getMaxReservationLimit()) {
            throw new UnvalidReservationRequestException("Member cannot reserve more than " + getMaxReservationLimit() + " books");
        }
        Set<UUID> isRecentlyReturned = getRecentlyReturnedBookIds(CONTINUOUS_BORROW_WAITING_DAYS);
        for (UUID bookUUID : bookUUIDs) {
            if (isRecentlyReturned.contains(bookUUID)) {
                throw new UnvalidReservationRequestException("Member must wait for " + CONTINUOUS_BORROW_WAITING_DAYS + " days to reserve book: " + bookUUID + " again");
            }
        }
    }

    private List<UUID> getActiveReservations() {
        return reservations.stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.PENDING||reservation.getStatus() == ReservationStatus.READY_FOR_PICKUP)
                .map(Reservation::getBookId)
                .collect(Collectors.toList());
    }

    private void checkValidReturn(List<UUID> bookUUIDs) {
        if(bookUUIDs.isEmpty()){
            throw new UnvalidReturnRequestException("Cannot return an empty list of books");
        }
        Set<UUID> currentlyBorrowedBookIds = getCurrentlyBorrowedBookIds();
        for (UUID bookUUID : bookUUIDs) {
            if (!currentlyBorrowedBookIds.contains(bookUUID)) {
                throw new UnvalidReturnRequestException("Member has not borrowed book: " + bookUUID);
            }
        }

    }

    private void checkCanBorrow(List<UUID> bookUUIDs) {
        if (bookUUIDs.isEmpty()) {
            throw new UnvalidBorrowRequestException("Cannot borrow an empty list of books");
        }
        if (hasDuplicateBook(bookUUIDs)) {
            throw new UnvalidBorrowRequestException("Cannot borrow the same book multiple times");
        }
        if (outstandingFines > MAX_OUTSTANDING_FINE) {
            throw new UnvalidBorrowRequestException("Member currently has outstanding fines: " + outstandingFines + " which exceeds the limit of " + MAX_OUTSTANDING_FINE);
        }
        Set<UUID> currentlyBorrowedBookIds = getCurrentlyBorrowedBookIds();
        for (UUID bookUUID : bookUUIDs) {
            if (currentlyBorrowedBookIds.contains(bookUUID)) {
                throw new UnvalidBorrowRequestException("Member has already borrowed book: " + bookUUID);
            }
        }
        int currentlyBorrowedBookCount = currentlyBorrowedBookIds.size();
        if (currentlyBorrowedBookCount + bookUUIDs.size() > getMaxBorrowingLimit()) {//todo check max book can borrow
            throw new UnvalidBorrowRequestException("Member cannot borrow more than " + getMaxBorrowingLimit() + " books");
        }


        Set<UUID> isRecentlyReturned = getRecentlyReturnedBookIds(CONTINUOUS_BORROW_WAITING_DAYS);
        for (UUID bookUUID : bookUUIDs) {
            if (isRecentlyReturned.contains(bookUUID)) {
                throw new UnvalidBorrowRequestException("Member must wait for " + CONTINUOUS_BORROW_WAITING_DAYS + " days to borrow book: " + bookUUID + " again");
            }
        }


    }

    private Set<UUID> getRecentlyReturnedBookIds(int days) {
        Set<UUID> recentlyReturnedBookIds = new HashSet<>();
        for (Borrow borrow : borrows) {

            for (BorrowItem item : borrow.getBorrowItems()) {
                if (item.isReturned() && ChronoUnit.DAYS.between(item.getReturnedAt(), LocalDateTime.now()) <= days) {
                    recentlyReturnedBookIds.add(item.getBookId());
                }
            }

        }
        return recentlyReturnedBookIds;
    }

    private Set<UUID> getCurrentlyBorrowedBookIds() {
        Set<UUID> currentlyBorrowedBookIds = new HashSet<>();
        for (Borrow borrow : borrows) {
            if (borrow.getStatus() != BorrowStatus.COMPLETED) {
                for (BorrowItem item : borrow.getBorrowItems()) {
                    if (!item.isReturned()) {
                        currentlyBorrowedBookIds.add(item.getBookId());
                    }
                }
            }
        }
        return currentlyBorrowedBookIds;
    }

    private boolean hasDuplicateBook(List<UUID> bookUUIDs) {
        Set<UUID> uniqueBookIds = new HashSet<>();
        for (UUID bookUUID : bookUUIDs) {
            if (!uniqueBookIds.add(bookUUID)) {
                return true;
            }
        }
        return false;
    }
}