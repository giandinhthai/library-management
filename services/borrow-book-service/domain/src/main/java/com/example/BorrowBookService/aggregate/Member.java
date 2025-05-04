package com.example.BorrowBookService.aggregate;

import com.example.BorrowBookService.event.*;
import com.example.BorrowBookService.exception.InvalidBorrowRequestException;
import com.example.BorrowBookService.exception.InvalidReservationRequestException;
import com.example.BorrowBookService.exception.InvalidReturnRequestException;
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
    public static final int MAX_REPUTATION = 100;
    public static final int MIN_REPUTATION = 0;
    public static final int CONTINUOUS_BORROW_WAITING_DAYS = 2;
    public static final int BRONZE_MAX_REPUTATION = 40;
    public static final int SILVER_MAX_REPUTATION = 80;

    private Member(UUID uuid, String email, int reputation, MembershipTier tier, int outstandingFines) {
        this.memberId = uuid;
        this.email = email;
        this.reputation = reputation;
        this.tier = tier;
        this.outstandingFines = outstandingFines;
    }

    protected Member() {

    }

    // Domain methods
    public static Member create(UUID uuid, String email) {
        return new Member(uuid, email, INITIAL_REPUTATION, MembershipTier.BRONZE, 0);
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



    public Borrow borrow(Map<UUID, Integer> booksPrice) {
        checkCanBorrow(booksPrice.keySet());
        Borrow borrow = Borrow.create(this, booksPrice);
        this.borrows.add(borrow);
        registerEvent(new MemberBorrowBooksEvent(memberId,booksPrice.keySet()));
        return borrow;
    }
    public Set<Borrow> returnBook(List<BorrowItem> borrowItems) {
        var borrows= borrowItems.stream().map(BorrowItem::getBorrow).collect(Collectors.toSet());
        checkValidReturn(borrows);
        for (BorrowItem borrowItem:borrowItems) {
            borrowItem.getBorrow().returnBorrowItem(borrowItem);
            addFine(borrowItem.getFineAmount());
        }
        registerEvent(new MemberReturnBooksEvent(memberId,borrowItems.stream().map(BorrowItem::getBookId).collect(Collectors.toSet())));
        return borrows;

    }
    public List<Reservation> reserve(Set<UUID> bookUUIDs) {
        checkCanReserve(bookUUIDs);
        List<Reservation> reservations = new ArrayList<>();
        for (UUID bookUUID : bookUUIDs) {
            reservations.add(Reservation.create(bookUUID, this));
        }
        this.reservations.addAll(reservations);
        registerEvent(new BooksReservedEvent(memberId,bookUUIDs));
        return reservations;
    }

    private void checkCanReserve(Set<UUID> bookUUIDs) {
        if (bookUUIDs.isEmpty()) {
            throw new InvalidReservationRequestException("Cannot reserve an empty list of books");
        }
//        if (hasDuplicateBook(bookUUIDs)) {
//            throw new InvalidReservationRequestException("Cannot reserve the same book multiple times");
//        }
        if (outstandingFines > MAX_OUTSTANDING_FINE) {
            throw new InvalidReservationRequestException("Member currently has outstanding fines: " + outstandingFines + " which exceeds the limit of " + MAX_OUTSTANDING_FINE);
        }
        List<UUID> activeReservations = getActiveReservations();
        Set<UUID> currentlyBorrowedBookIds = getCurrentlyBorrowedBookIds();
        for (UUID bookId : bookUUIDs) {
            if (currentlyBorrowedBookIds.contains(bookId)) {
                throw new InvalidReservationRequestException("Member has currently borrow book: " + bookId);
            }
            if (activeReservations.contains(bookId)) {
                throw new InvalidReservationRequestException("Member has already reserved book: " + bookId);
            }
        }

        int activeReservationCount = activeReservations.size();
        if (activeReservationCount + bookUUIDs.size() > getMaxReservationLimit()) {
            throw new InvalidReservationRequestException("Member cannot reserve more than " + getMaxReservationLimit() + " books");
        }
        Set<UUID> isRecentlyReturned = getRecentlyReturnedBookIds(CONTINUOUS_BORROW_WAITING_DAYS);
        for (UUID bookUUID : bookUUIDs) {
            if (isRecentlyReturned.contains(bookUUID)) {
                throw new InvalidReservationRequestException("Member must wait for " + CONTINUOUS_BORROW_WAITING_DAYS + " days to reserve book: " + bookUUID + " again");
            }
        }
    }

    private List<UUID> getActiveReservations() {
        return reservations.stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.PENDING||reservation.getStatus() == ReservationStatus.READY_FOR_PICKUP)
                .map(Reservation::getBookId)
                .collect(Collectors.toList());
    }

    private void checkValidReturn(Set<Borrow> borrows) {
        if (borrows.isEmpty()) {
            throw new InvalidReturnRequestException("Cannot return an empty list of books");
        }
        for (Borrow borrow : borrows) {
            if (borrow.getMember() != this) {
                throw new InvalidReturnRequestException("Borrow does not belong to member");
            }
        }

    }

    private void checkCanBorrow(Set<UUID> bookUUIDs) {
        if (bookUUIDs.isEmpty()) {
            throw new InvalidBorrowRequestException("Cannot borrow an empty list of books");
        }
        if (hasReadyReservationOn(bookUUIDs)){
            throw new InvalidBorrowRequestException("Member has a ready reservation on one of the books");
        }
        if (outstandingFines > MAX_OUTSTANDING_FINE) {
            throw new InvalidBorrowRequestException("Member currently has outstanding fines: " + outstandingFines + " which exceeds the limit of " + MAX_OUTSTANDING_FINE);
        }
        Set<UUID> currentlyBorrowedBookIds = getCurrentlyBorrowedBookIds();
        for (UUID bookUUID : bookUUIDs) {
            if (currentlyBorrowedBookIds.contains(bookUUID)) {
                throw new InvalidBorrowRequestException("Member has already borrowed book: " + bookUUID);
            }
        }
        int currentlyBorrowedBookCount = currentlyBorrowedBookIds.size();
        if (currentlyBorrowedBookCount + bookUUIDs.size() > getMaxBorrowingLimit()) {
            throw new InvalidBorrowRequestException("Member cannot borrow more than " + getMaxBorrowingLimit() + " books");
        }
        Set<UUID> isRecentlyReturned = getRecentlyReturnedBookIds(CONTINUOUS_BORROW_WAITING_DAYS);
        for (UUID bookUUID : bookUUIDs) {
            if (isRecentlyReturned.contains(bookUUID)) {
                throw new InvalidBorrowRequestException("Member must wait for " + CONTINUOUS_BORROW_WAITING_DAYS + " days to borrow book: " + bookUUID + " again");
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

    public Set<UUID> getCurrentlyBorrowedBookIds() {
        Set<UUID> currentlyBorrowedBookIds = new HashSet<>();
        for (Borrow borrow : borrows) {
            currentlyBorrowedBookIds.addAll(borrow.getCurrentlyBorrowedBookIds());
        }
        return currentlyBorrowedBookIds;
    }



    public UUID markAsReadyReservationContains(Reservation reservation) {
        reservation.markAsReady();
        registerEvent(new ReservationReadyEvent(reservation));
        return reservation.getReservationId();
    }


    public boolean hasReadyReservationOn(Set<UUID> bookIds) {
        return reservations.stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.READY_FOR_PICKUP)
                .map(Reservation::getBookId)
                .anyMatch(bookIds::contains);
    }



    public int checkAndExpireReservations() {
        int expiredCount = 0;
        for (Reservation reservation : reservations) {
            if (reservation.markAsExpiredIfNeeded()) {
                expiredCount++;
                registerEvent(new ReservationExpiredEvent(memberId, reservation.getBookId()));
            }
        }
        return expiredCount;
    }

    public Borrow completeReservation(Reservation reservation, Integer bookPrice) {
        reservation.complete();
        Borrow borrow = Borrow.create(this, Collections.singletonMap(reservation.getBookId(), bookPrice));
        this.borrows.add(borrow);
        registerEvent(new MemberCompleteReservationEvent(reservation));
        return borrow;
    }
}