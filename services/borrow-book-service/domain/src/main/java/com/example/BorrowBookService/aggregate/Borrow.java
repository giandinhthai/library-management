package com.example.BorrowBookService.aggregate;

import com.example.BorrowBookService.event.FineAmountUpdatedEvent;
import com.example.BorrowBookService.exception.UnvalidReturnRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.*;
@Entity
@Table(name = "borrows")
@Getter
public class Borrow {
    @Id
    @Column(name = "borrow_id")
    private UUID borrowId;

    @Column(name = "borrowed_at", nullable = false)
    private LocalDateTime borrowedAt;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowStatus status;

    @Column(name = "total_fine_amount")
    private int totalFineAmount;

    @Column(name = "fine_paid")
    private boolean finePaid;

    @OneToMany(mappedBy = "borrow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowItem> borrowItems = new ArrayList<>();

    @Setter
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private static final int STANDARD_LOAN_PERIOD_DAYS = 14;

    protected Borrow() {}

    private Borrow(UUID borrowId, Member member, LocalDateTime borrowedAt, LocalDateTime dueDate, BorrowStatus status, int totalFineAmount, boolean finePaid) {
        this.borrowId = borrowId;
        this.member = member;
        this.borrowedAt = borrowedAt;
        this.dueDate = dueDate;
        this.status = status;
        this.totalFineAmount = totalFineAmount;
        this.finePaid = finePaid;
    }

    public static Borrow create(Member member, List<UUID> bookUUIDs, List<Integer> booksPrice) {
        if (bookUUIDs.size() != booksPrice.size()) {
            throw new IllegalArgumentException("Book UUIDs and book prices must have the same length.");
        }
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }

        Borrow borrow = new Borrow(UUID.randomUUID(), member, LocalDateTime.now(), LocalDateTime.now().plusDays(STANDARD_LOAN_PERIOD_DAYS), BorrowStatus.ACTIVE, 0, false);
        for (int i = 0; i < bookUUIDs.size(); i++) {
            borrow.addBorrowItem(BorrowItem.create(bookUUIDs.get(i), booksPrice.get(i)));
        }
        return borrow;
    }

    private void addBorrowItem(BorrowItem item) {
        item.setBorrow(this);
        this.borrowItems.add(item);
    }

//    public void returnBook(List<Book> books) {
//        Set<UUID> bookIds = new HashSet<>();
//        for (Book book : books) {
//            bookIds.add(book.getBookId());
//        }
//        for (BorrowItem item : this.borrowItems) {
//            if (bookIds.contains(item.getBookId())) {
//                bookIds.remove(item.getBookId());
//                item.processReturn();
//                changeTotalFineAmount(item.getFineAmount());
//            }
//        }
//        if (!bookIds.isEmpty()) {
//            throw new UnvalidReturnRequestException("Borrow item and return item do not match");
//        }
//        checkBorrowStatus();
//    }

    public void markCompletedIfAllReturned() {
        if (this.borrowItems.stream().allMatch(BorrowItem::isReturned)) {
            this.status = BorrowStatus.COMPLETED;
        }
    }

    public void changeTotalFineAmount(int fineAmount) {
        this.totalFineAmount += fineAmount;
    }
}