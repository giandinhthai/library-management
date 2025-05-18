package com.example.BorrowBookService.usecase.command.member;


import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.borrow.mapper.BorrowMapper;
import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.exception.InvalidReservationRequestException;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.BorrowBookService.repository.ReservationReadOnlyRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class CompleteReservation implements Command<BorrowResult> {
    private UUID ReservationId;
    private UUID memberId;
}

@Service
@RequiredArgsConstructor
class CompleteReservationHandler implements RequestHandler<CompleteReservation, BorrowResult> {
    private final MemberRepository memberRepository;
    private final ReservationReadOnlyRepository reservationRepository;
    private final BookRepository bookRepository;
    private final BorrowMapper borrowMapper;

    @Override
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public BorrowResult handle(CompleteReservation request) {
        Member member = memberRepository.findByIdOrThrow(request.getMemberId());
        Reservation reservation = reservationRepository.getReservationByIdOrThrow(request.getReservationId());

        if (!reservation.getMember().getMemberId().equals(request.getMemberId())) {
            throw new InvalidReservationRequestException("Reservation does not belong to this member");
        }

        Integer bookPrice = bookRepository.getPrice(reservation.getBookId());
        Borrow result = member.completeReservation(reservation, bookPrice);
        memberRepository.save(member);
        return borrowMapper.toResult(result);
    }
}
