package com.example.BorrowBookService.usecase.query;

import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.DTO.reverse.mapper.ReserveMapper;
import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.repository.ReservationReadOnlyRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetMemberReservations implements Query<List<ReserveResult>> {
    private UUID memberId;
    private ReservationStatus status;
}

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class GetMemberReservationsHandler implements RequestHandler<GetMemberReservations, List<ReserveResult>> {
    private final ReservationReadOnlyRepository reservationReadOnlyRepository;
    private final ReserveMapper reserveMapper;

    @Override
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN') or " +
            "hasRole('MEMBER') and authentication.principal.equals(#query.memberId)")
    public List<ReserveResult> handle(@Param("query") GetMemberReservations query) {
        List<Reservation> reservations = reservationReadOnlyRepository.getAllReservation(
                query.getMemberId(),
                query.getStatus()
        );
        return reservations.stream()
                .map(reserveMapper::toResult)
                .toList();
    }
}