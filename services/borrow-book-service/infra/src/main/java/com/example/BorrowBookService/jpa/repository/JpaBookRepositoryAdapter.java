package com.example.BorrowBookService.jpa.repository;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.exception.NotFoundException;
import com.example.BorrowBookService.jpa.repository.jpa.JpaBookRepository;
import com.example.BorrowBookService.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class JpaBookRepositoryAdapter implements BookRepository {

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
    public List<Boolean> checkAvailableBook(List<UUID> bookUUIDs) {
        if (bookUUIDs.isEmpty()) {
            return Collections.emptyList();
        }
        List<Object[]> results = bookJpaRepository.checkBookAvailability(bookUUIDs);
        if (results.size()<bookUUIDs.size()){
            throw new NotFoundException("Some books are not found");
        }
        Map<UUID, Boolean> availabilityMap = new HashMap<>();
        for (Object[] result : results) {
            UUID bookId = (UUID) result[0];
            Boolean isAvailable = (Boolean) result[1];
            availabilityMap.put(bookId, isAvailable);
        }

        // Return availability in the same order as input UUIDs
        return bookUUIDs.stream()
                .map(id -> availabilityMap.getOrDefault(id, false))
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getAllPriceByBookId(List<UUID> bookUUIDs) {
        if (bookUUIDs.isEmpty()) {
            return Collections.emptyList();
        }
        List<Object[]> results = bookJpaRepository.findBookPrices(bookUUIDs);
        if (results.size()<bookUUIDs.size()){
            throw new NotFoundException("Some books are not found");
        }
        Map<UUID, Integer> priceMap = results.stream()
                .collect(Collectors.toMap(
                        result -> (UUID) result[0],
                        result -> (Integer) result[1]
                ));
        return bookUUIDs.stream()
                .map(id -> priceMap.getOrDefault(id, 0))
                .collect(Collectors.toList());
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
    public List<Boolean> checkAvailableBookForReserve(List<UUID> bookUUIDs) {
        var result = bookJpaRepository.checkBookAvailabilityForReserve(bookUUIDs,AVAILABLE_RESERVATION_STATUS);
        if (result.size()<bookUUIDs.size()){
            throw new NotFoundException("Some books are not found");
        }
        Map<UUID, Boolean> availabilityMap = result.stream().collect(Collectors.toMap(
                result1 -> (UUID) result1[0],
                result1 -> (Boolean) result1[1]
        ));

        return bookUUIDs.stream()
                .map(id -> availabilityMap.getOrDefault(id, false))
                .collect(Collectors.toList());}
}