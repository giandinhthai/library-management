package com.example.BorrowBookService.jpa.repository;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.buildingblocks.caching.constants.CacheNames;
import com.example.buildingblocks.caching.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
//@Repository
//@Primary
@RequiredArgsConstructor
public class CachedBookRepository implements BookRepository {
    private final BookRepository delegate;
    private final CacheService cacheService;

    @Override
    public Book findByIdOrThrow(UUID bookId) {
        String cacheKey = bookId.toString();
        Book cachedBook = cacheService.get(CacheNames.BOOKS, cacheKey);
        if (cachedBook != null) {
            return cachedBook;
        }
        Book dbBook = delegate.findByIdOrThrow(bookId);
        cacheService.put(CacheNames.BOOKS, cacheKey, dbBook,1, TimeUnit.HOURS);
        return dbBook;
    }

    @Override
    public List<Book> findAllByIdOrThrow(List<UUID> bookIds) {
        // Attempt to get books from cache
        Map<String, Book> cachedBooks = cacheService.multiGet(
                CacheNames.BOOKS, bookIds.stream().map(UUID::toString).toList()
        );
        List<UUID> missedIds;
        // Identify IDs that were not found in cache
        if (cachedBooks!=null) {
             missedIds = bookIds.stream()
                    .filter(id -> cachedBooks.get(id.toString()) == null)
                    .toList();
        }else{
            missedIds = bookIds;
        }

        if (!missedIds.isEmpty()) {
            // Fetch missing books from DB
            List<Book> missingBooks = delegate.findAllByIdOrThrow(missedIds);

            // Cache newly fetched books
            Map<String, Book> booksToCache = missingBooks.stream()
                    .collect(Collectors.toMap(book -> book.getBookId().toString(), book -> book));
            cacheService.multiPut(CacheNames.BOOKS, booksToCache);

            // Merge new books into the result map
            if (cachedBooks != null) {
                cachedBooks.putAll(booksToCache);
            }
        }

        // Preserve input order
        return bookIds.stream()
                .map(id -> cachedBooks.get(id.toString()))
                .toList();
    }


    @Override
    public List<Book> findAllByIdForUpdateOrThrow(List<UUID> bookUUIDs) {
        return delegate.findAllByIdForUpdateOrThrow(bookUUIDs);
    }

    @Override
    public Map<UUID, Boolean> validateAvailableBookForBorrow(List<UUID> bookUUIDs) {
        return delegate.validateAvailableBookForBorrow(bookUUIDs);
    }

    @Override
    public Map<UUID, Integer> getAllPriceByBookId(List<UUID> bookUUIDs) {
        return delegate.getAllPriceByBookId(bookUUIDs);
    }

    @Override
    public Book save(Book book) {
        Book savedBook = delegate.save(book);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        cacheService.put(CacheNames.BOOKS, savedBook.getBookId().toString(),savedBook);
                    }
                }
        );
        return savedBook;
    }

    @Override
    public List<Book> saveAll(List<Book> books) {
        List<Book> savedBooks = delegate.saveAll(books);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        Map<String, Book> toCache = savedBooks.stream()
                                .collect(Collectors.toMap(
                                        b -> b.getBookId().toString(),
                                        b -> b
                                ));
                        cacheService.multiPut(CacheNames.BOOKS, toCache);
                    }
                }
        );
        return savedBooks;
    }

    @Override
    public Map<UUID, Boolean> checkAvailableBookForReserve(List<UUID> listBookId) {
        return delegate.checkAvailableBookForReserve(listBookId);
    }


    @Override
    public Integer getPrice(UUID bookId) {
        return delegate.getPrice(bookId);
    }

    @Override
    public List<Book> findAll() {
        return delegate.findAll();
    }

    @Override
    public Book findByIdForUpdateOrThrow(UUID bookId) {
        return delegate.findByIdForUpdateOrThrow(bookId);
    }
}