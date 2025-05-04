package com.example.BorrowBookService.usecase.command;

import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.BorrowBookService.repository.ReservationReadOnlyRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNextPendingReservationOnBook implements Command<UUID> {
    private UUID bookId;
}
@Service
@AllArgsConstructor
@Slf4j
class UpdateNextPendingReservationOnBookHandler implements RequestHandler<UpdateNextPendingReservationOnBook, UUID> {
    private final MemberRepository memberRepository;
    private final ReservationReadOnlyRepository reservationReadOnlyRepository;
    @Override
    @Transactional
    public UUID handle(UpdateNextPendingReservationOnBook command) {
        Reservation nextPendingReservation = reservationReadOnlyRepository.getNextReservationOnBook(command.getBookId());
        if (nextPendingReservation == null) {
            log.info("no pending reservation for book {}", command.getBookId());
            return null;
        }

        Member member = nextPendingReservation.getMember();
        var reservationId = member.markAsReadyReservationContains(nextPendingReservation);
        memberRepository.save(member);
        return reservationId;
    }
}
