package com.example.BorrowBookService.usecase.command;

import com.example.BorrowBookService.repository.MemberRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@NoArgsConstructor
public class BatchExpireReservations implements Command<Integer> {
    // No additional fields needed as this is a system-wide operation
}

@Service
@RequiredArgsConstructor
@Slf4j
class BatchExpireReservationsHandler implements RequestHandler<BatchExpireReservations, Integer> {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Integer handle(BatchExpireReservations command) {
        int totalExpired = 0;
        log.info("Starting reservation expiration check");

        var members = memberRepository.findAllWithReadyReservations();

        for (var member : members) {
            int expiredCount = member.checkAndExpireReservations();
            if (expiredCount > 0) {
                memberRepository.save(member);
                totalExpired += expiredCount;
            }
        }
        log.info("Completed reservation expiration check. Expired {} reservations", totalExpired);
        return totalExpired;
    }
}