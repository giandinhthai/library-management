package com.example.BorrowBookService.service.cache;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.exception.NotFoundException;

import java.util.*;
public interface BookCacheService {

    // New cache-aside method
    Book getBookAside(UUID bookId) throws NotFoundException;

    // Other existing methods remain unchanged
    void invalidateBook(UUID bookId);
    void cacheBooks(Collection<Book> books);
    Map<UUID, Book> getBooks(Set<UUID> bookIds);
    void invalidateBooks(Set<UUID> bookIds);
    void invalidateAllBooks();
    int getBookCount();
}