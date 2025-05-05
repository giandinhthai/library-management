package com.example.BorrowBookService;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.config.TestConfig;
import com.example.BorrowBookService.exception.NotFoundException;
import com.example.BorrowBookService.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class BookRepositoryAdapterIntegrationTest {
    @Autowired
    private BookRepository bookRepository;


    @Test
    void findByIdForUpdateOrThrow_ShouldReturnBook_WhenBookExists() {
        // Arrange
        UUID bookId = createAndSaveBook();

        // Act
        Book result = bookRepository.findByIdForUpdateOrThrow(bookId);

        // Assert
        assertNotNull(result);
        assertEquals(bookId, result.getBookId());
        assertEquals(10, result.getAvailableQuantity()); // Assuming Book.create(10, 10)
    }

    @Test
    void findByIdForUpdateOrThrow_ShouldThrowNotFoundException_WhenBookDoesNotExist() {
        // Arrange
        UUID nonExistentBookId = UUID.randomUUID();

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookRepository.findByIdForUpdateOrThrow(nonExistentBookId);
        });
        assertEquals("Book not found with ID: " + nonExistentBookId, exception.getMessage());
    }

    private UUID createAndSaveBook() {
        Book book = Book.create(10, 10);
        bookRepository.save(book);
        return book.getBookId();
    }
}