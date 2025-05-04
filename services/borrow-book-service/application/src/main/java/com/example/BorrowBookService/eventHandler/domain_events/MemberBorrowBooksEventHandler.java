package com.example.BorrowBookService.eventHandler.domain_events;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.event.BookAvailableQuantityIncreasedEvent;
import com.example.BorrowBookService.event.MemberBorrowBooksEvent;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.usecase.command.UpdateBooksStatusOnBorrow;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MemberBorrowBooksEventHandler {
    private final Mediator mediator;
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateBookStatusOnMemberBorrowedEvent(MemberBorrowBooksEvent memberBorrowBooksEvent) {
        mediator.send(new UpdateBooksStatusOnBorrow(List.copyOf(memberBorrowBooksEvent.getBookIds())));
    }
}
