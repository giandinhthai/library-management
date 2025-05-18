package com.example.BorrowBookService.usecase.query.book;


import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.DTO.reverse.mapper.ReserveMapper;
import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.aggregate.ReservationStatus;
import com.example.BorrowBookService.repository.ReservationReadOnlyRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Query;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class GetReservationOnBook implements Query<Page<ReserveResult>> {
    private UUID bookId;
    private ReservationStatus status;
    private Pageable pageable;
}

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class GetReservationOnBookHandler implements RequestHandler<GetReservationOnBook, Page<ReserveResult>> {
    private final ReservationReadOnlyRepository reservationReadOnlyRepository;
    private final ReserveMapper reserveMapper;

    @Override
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public Page<ReserveResult> handle(GetReservationOnBook request) {
        Page<Reservation> reservations = reservationReadOnlyRepository.getReservationByBook(
                request.getBookId(),
                request.getStatus(),
                request.getPageable()
        );
        return reservations.map(reserveMapper::toResult);
    }
}