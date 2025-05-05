package com.example.BorrowBookService.usecase.command;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApproveBookReservation implements Command<UUID> {
    private UUID bookId;
}
@Service
@RequiredArgsConstructor
@Slf4j
class ApproveBookReservationHandler implements RequestHandler<ApproveBookReservation, UUID> {
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public UUID handle(ApproveBookReservation command) {
        Book book = bookRepository.findByIdForUpdateOrThrow(command.getBookId());
        book.approveReserved();
        bookRepository.save(book);
        return book.getBookId();
    }
}