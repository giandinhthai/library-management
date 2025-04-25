package com.example.BorrowBookService.eventHandler;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.event.MemberReturnBooksEvent;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.BorrowBookService.repository.ReservationReadOnlyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class MemberReturnBooksEventHandler {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final ReservationReadOnlyRepository reservationReadOnlyRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateBookStatusOnMemberReturnedEvent(MemberReturnBooksEvent memberReturnBooksEvent) {
        log.info("Book returned event received");
        List<UUID> bookIds = memberReturnBooksEvent.getBookIds().stream().toList();
        List<Book> books = bookRepository.findAllByIdOrThrow(bookIds);

        for (Book book : books) {
            book.isReturned();
        }
        bookRepository.saveAll(books);
    }



}
