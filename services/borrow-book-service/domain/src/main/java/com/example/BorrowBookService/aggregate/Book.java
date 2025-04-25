package com.example.BorrowBookService.aggregate;

import com.example.BorrowBookService.event.BookAvailableQuantityIncreasedEvent;
import com.example.BorrowBookService.exception.InvalidBookStateException;
import com.example.BorrowBookService.exception.InvalidBorrowRequestException;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
public class Book extends AbstractAggregateRoot<Book> {
    @Id
    @Column(name = "book_id")
    private UUID bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "isbn", unique = true)
    private String isbn;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "available_quantity")
    private int availableQuantity;

    @Column(name = "reservation_quantity")
    private int reservationQuantity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;


    protected Book() {
    }

    private Book(UUID bookId, String title, String author, String isbn, int price, int quantity,int reservationQuantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.quantity = quantity;
        this.availableQuantity = quantity;
        this.status = BookStatus.ACTIVE;
        this.reservationQuantity = reservationQuantity;
    }
    public void addQuantity(int quantity) {
        if (status != BookStatus.ACTIVE) {
            throw new InvalidBookStateException("Book is not active in the system");
        }
        this.quantity += quantity;
        this.availableQuantity += quantity;
    }

    public static Book create(String title, String author, String isbn, int price, int quantity) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        return new Book(UUID.randomUUID(), title, author, isbn, price, quantity, 0);
    }
    public boolean isAvailable() {
        return availableQuantity > 0 && status == BookStatus.ACTIVE;
    }
    public boolean hasAvailableCopies() {
        return availableQuantity > 0;
    }

    public void getBorrowed() {
        validateAvailableRequest();
        availableQuantity--;
    }
    public void completeReservation() {
        if (reservationQuantity <= 0) {
            throw new InvalidBookStateException("No reserved books to complete");
        }
        reservationQuantity--;
    }
    public void approveReserved() {
        validateAvailableRequest();
        availableQuantity--;
        reservationQuantity++;
    }

    public void isReturned() {
        if (availableQuantity + reservationQuantity >= quantity) {
            throw new InvalidBookStateException("Cannot return more books than total quantity");
        }
        availableQuantity++;
        registerEvent(new BookAvailableQuantityIncreasedEvent(bookId));

    }
    private void validateAvailableRequest() {
        if (!hasAvailableCopies()) {
            throw new InvalidBorrowRequestException("No available copies of this book");
        }
        if (status != BookStatus.ACTIVE) {
            throw new InvalidBorrowRequestException("Book is not active in the system");
        }
    }

}
