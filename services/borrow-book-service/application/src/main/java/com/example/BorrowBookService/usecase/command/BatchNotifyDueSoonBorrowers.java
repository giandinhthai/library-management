package com.example.BorrowBookService.usecase.command;

import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.repository.BorrowReadOnlyRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import com.example.buildingblocks.eventbus.kafkaService.EventPublisher;
import com.example.buildingblocks.shared.intergation_event.BorrowDueSoonIntegrationEvent;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class BatchNotifyDueSoonBorrowers implements Command<Void> {
}

@Service
@RequiredArgsConstructor
@Slf4j
class BatchNotifyDueSoonBorrowersHandler implements RequestHandler<BatchNotifyDueSoonBorrowers, Void> {
    private final MemberRepository memberRepository;
    private final BorrowReadOnlyRepository borrowRepository;
    private final EventPublisher eventPublisher;
    private final int noticePeriodInDays = 3;
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Void handle(BatchNotifyDueSoonBorrowers command) {
        log.info("Running BatchNotifyDueSoonBorrowersHandler");
        var dueSoonBorrowers = borrowRepository.findNearlyDueBorrowers(noticePeriodInDays);
        dueSoonBorrowers.stream()
                .map(borrow -> new BorrowDueSoonIntegrationEvent(
                        borrow.getMember().getMemberId(),
                        borrow.getBorrowId(),
                        Instant.now()
                )).forEach(eventPublisher::publish);

        return null;
    }
}