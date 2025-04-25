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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetMemberReservations implements Query<List<ReserveResult>> {
    private UUID memberId;
    private ReservationStatus status;
}

@Service
@RequiredArgsConstructor
class GetMemberReservationsHandler implements RequestHandler<GetMemberReservations, List<ReserveResult>> {
    private final ReservationReadOnlyRepository reservationReadOnlyRepository;
    private final ReserveMapper reserveMapper;

    @Override
    public List<ReserveResult> handle(GetMemberReservations query) {
        List<Reservation> reservations = reservationReadOnlyRepository.getAllReservation(
            query.getMemberId(), 
            query.getStatus()
        );
        return reservations.stream()
                .map(reserveMapper::toResult)
                .collect(Collectors.toList());
    }
}