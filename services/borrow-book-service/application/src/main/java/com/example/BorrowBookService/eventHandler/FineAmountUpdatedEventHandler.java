package com.example.BorrowBookService.eventHandler;

import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.event.FineAmountUpdatedEvent;
import com.example.BorrowBookService.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FineAmountUpdatedEventHandler {
    private final MemberRepository memberRepository;
    @TransactionalEventListener
    public void onFineAmountUpdatedEvent(FineAmountUpdatedEvent fineAmountUpdatedEvent) {
        System.out.println("Fine amount updated event received");
        Member member = memberRepository.findByIdOrThrow(fineAmountUpdatedEvent.getMemberId());
        member.addFine(fineAmountUpdatedEvent.getAmount());
    }
}
