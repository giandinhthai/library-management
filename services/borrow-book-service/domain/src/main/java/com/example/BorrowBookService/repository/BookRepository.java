package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.Book;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BookRepository {
    List<Book> findAllByIdOrThrow(List<UUID> bookUUIDs);


    Map<UUID,Boolean> validateAvailableBookForBorrow(List<UUID> bookUUIDs);


    Map<UUID, Integer> getAllPriceByBookId(List<UUID> bookUUIDs);

    
    Book save(Book book);

    List<Book> saveAll(List<Book> books);

    Map<UUID,Boolean> checkAvailableBookForReserve(List<UUID> listBookId);

    Book findByIdOrThrow(UUID bookId);

    Integer getPrice(UUID bookId);
}
