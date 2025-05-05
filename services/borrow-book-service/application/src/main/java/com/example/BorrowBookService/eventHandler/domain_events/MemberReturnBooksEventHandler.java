package com.example.BorrowBookService.eventHandler.domain_events;

import com.example.BorrowBookService.event.MemberReturnBooksEvent;
import com.example.BorrowBookService.usecase.command.UpdateBooksStatusOnReturn;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class MemberReturnBooksEventHandler {
    private final Mediator mediator;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void updateBookStatusOnMemberReturnedEvent(MemberReturnBooksEvent event) {
        log.info("Book returned event received");
        mediator.send(new UpdateBooksStatusOnReturn(List.copyOf(event.getBookIds())));
    }


}
