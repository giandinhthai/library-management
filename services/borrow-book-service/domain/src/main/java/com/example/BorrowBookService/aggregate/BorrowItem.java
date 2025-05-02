package com.example.BorrowBookService.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "borrow_items")
@Getter
public class BorrowItem {
    @Id
    @Column(name = "borrow_item_id")
    private UUID borrowItemId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "borrow_id", nullable = false)
    private Borrow borrow;

    @Column(name = "book_id", nullable = false)
    private UUID bookId;

    @Column(name = "fine_amount")
    private int fineAmount;

    @Column(name = "book_price")
    private int bookPrice;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "is_returned")
    private boolean returned;

    private static final int OVERDUE_PENALTY = 2000;
    private static final double LAST_FINE_RATE = 1.5;
    private static final int MAX_OVERDUE_DAYS = 30;
    protected BorrowItem() {
    }

    private BorrowItem(UUID borrowItemId, UUID bookId, int bookPrice, int fineAmount, boolean returned, Borrow borrow) {
        this.borrowItemId = borrowItemId;
        this.bookId = bookId;
        this.bookPrice = bookPrice;
        this.fineAmount = fineAmount;
        this.returned = returned;
        this.borrow = borrow;
    }

    public static BorrowItem create(UUID bookId,int currentBookPrice) {
        if (bookId == null) {
            throw new IllegalArgumentException("Book ID cannot be null.");
        }
        if (currentBookPrice <= 0) {
            throw new IllegalArgumentException("Book price must be positive.");
        }
        return new BorrowItem(UUID.randomUUID(), bookId, currentBookPrice, 0, false, null);
    }

    private void markReturned() {
        if (returned) {
            throw new IllegalStateException("Item already returned");
        }
        this.returnedAt = LocalDateTime.now();
        this.returned = true;
    }
    private void calculateFine() {
        if (LocalDateTime.now().isBefore(this.borrow.getDueDate())) {
            return;
        }
        int overdueDays = (int) ChronoUnit.DAYS.between(borrow.getDueDate(), LocalDateTime.now());
        this.fineAmount= (overdueDays<=MAX_OVERDUE_DAYS)?OVERDUE_PENALTY*overdueDays
                :(int) (LAST_FINE_RATE*this.bookPrice);
    }
    public void processReturn() {
        this.markReturned();
        this.calculateFine();
    }


}