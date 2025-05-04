package com.example.BorrowBookService.jpa.repository;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.exception.NotFoundException;
import com.example.BorrowBookService.DTO.BookAvailablePair;
import com.example.BorrowBookService.DTO.BookPricePair;
import com.example.BorrowBookService.jpa.repository.jpa.JpaBookRepository;
import com.example.BorrowBookService.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class BookRepositoryAdapter implements BookRepository {

    private final JpaBookRepository bookJpaRepository;


    @Override
    public List<Book> findAllByIdOrThrow(List<UUID> bookUUIDs) {
        List<Book> foundBooks = bookJpaRepository.findAllById(bookUUIDs);

        if (foundBooks.size() != bookUUIDs.size()) {
            Set<UUID> foundBookIds = foundBooks.stream()
                    .map(Book::getBookId)
                    .collect(Collectors.toSet());

            List<UUID> notFoundBookIds = bookUUIDs.stream()
                    .filter(id -> !foundBookIds.contains(id))
                    .toList();

            throw new NotFoundException("The following books were not found: " + notFoundBookIds);
        }

        return foundBooks;
    }


    @Override
    public Map<UUID, Boolean> validateAvailableBookForBorrow(List<UUID> bookUUIDs) {
        if (bookUUIDs.isEmpty()) {
            return Collections.emptyMap();
        }
        List<BookAvailablePair> results = bookJpaRepository.checkBookAvailabilityForBorrow(bookUUIDs);
        if (results.size() < bookUUIDs.size()) {
            throw new NotFoundException("Some books are not found or duplicate");
        }
        return results.stream().collect(Collectors.toMap(
                BookAvailablePair::getBookId,
                BookAvailablePair::getAvailable
        ));
    }

    @Override
    public Map<UUID, Integer> getAllPriceByBookId(List<UUID> bookUUIDs) {
        if (bookUUIDs.isEmpty()) {
            return Collections.emptyMap();
        }
        List<BookPricePair> results = bookJpaRepository.findBookPrices(bookUUIDs);
        if (results.size() < bookUUIDs.size()) {
            throw new NotFoundException("Some books are not found");
        }
        return results.stream()
                .collect(Collectors.toMap(
                        BookPricePair::getBookId,
                        BookPricePair::getPrice
                ));
    }

    @Override
    public Book save(Book book) {
        return bookJpaRepository.save(book);
    }

    @Override
    public List<Book> saveAll(List<Book> books) {
        return bookJpaRepository.saveAll(books);
    }

    private static final List<ReservationStatus> AVAILABLE_RESERVATION_STATUS = List.of(
            ReservationStatus.PENDING,
            ReservationStatus.READY_FOR_PICKUP
    );

    @Override
    public Map<UUID, Boolean> checkAvailableBookForReserve(List<UUID> bookUUIDs) {
        var results = bookJpaRepository.checkBookAvailabilityForReserve(bookUUIDs);
        if (results.size() < bookUUIDs.size()) {
            throw new NotFoundException("Some books are not found");
        }
        return results.stream()
                .collect(Collectors.toMap(
                        BookAvailablePair::getBookId,
                        BookAvailablePair::getAvailable
                ));
    }

    @Override
    public Book findByIdOrThrow(UUID bookId) {
        return bookJpaRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + bookId));
    }

    @Override
    public Integer getPrice(UUID bookId) {
        return bookJpaRepository.getPrice(bookId);
    }

    @Override
    public List<Book> findAll() {
        return bookJpaRepository.findAll();
    }


}