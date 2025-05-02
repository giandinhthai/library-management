package com.example.BorrowBookService.repository;

import com.example.BorrowBookService.aggregate.Book;

import java.util.List;
import java.util.UUID;

public interface BookRepository {
    List<Book> findAllByIdOrThrow(List<UUID> bookUUIDs);


    List<Boolean> checkAvailableBook(List<UUID> bookUUIDs);

    List<Integer> getAllPriceByBookId(List<UUID> bookUUIDs);

    Book save(Book book);

    List<Book> saveAll(List<Book> books);

    List<Boolean> checkAvailableBookForReserve(List<UUID> listBookId);
}
