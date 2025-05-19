package com.example.BorrowBookService.usecase.command;

import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.aggregate.Reservation;
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

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNextPendingReservationOnBook implements Command<Void> {
    private UUID bookId;
    private int quantity;
}
@Service
@AllArgsConstructor
@Slf4j
class UpdateNextPendingReservationOnBookHandler implements RequestHandler<UpdateNextPendingReservationOnBook, Void> {
    private final MemberRepository memberRepository;
    private final ReservationReadOnlyRepository reservationReadOnlyRepository;
    @Override
    @Transactional
    public Void handle(UpdateNextPendingReservationOnBook command) {
        var nextPendingReservations =
                reservationReadOnlyRepository.getNextReservationOnBook(command.getBookId(),command.getQuantity());
        nextPendingReservations.forEach(reservation -> {
            reservation.getMember().markAsReadyReservationContains(reservation);
        });
        List<Member> members = nextPendingReservations.stream()
                .map(Reservation::getMember)
                .distinct()
                .toList();
        memberRepository.saveAll(members);
        return null;
    }
}
