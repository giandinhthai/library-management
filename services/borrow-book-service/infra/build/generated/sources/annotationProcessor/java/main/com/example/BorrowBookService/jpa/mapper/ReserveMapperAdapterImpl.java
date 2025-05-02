package com.example.BorrowBookService.jpa.mapper;

import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.aggregate.Reservation;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T16:41:21+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class ReserveMapperAdapterImpl implements ReserveMapperAdapter {

    @Override
    public ReserveResult toResult(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReserveResult reserveResult = new ReserveResult();

        reserveResult.setReserveId( reservation.getReservationId() );
        reserveResult.setBookId( reservation.getBookId() );
        reserveResult.setMemberId( reservationMemberMemberId( reservation ) );
        reserveResult.setStatus( statusToString( reservation.getStatus() ) );
        reserveResult.setReservedAt( reservation.getReservedAt() );
        reserveResult.setExpiresAt( reservation.getExpiresAt() );

        return reserveResult;
    }

    private UUID reservationMemberMemberId(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        Member member = reservation.getMember();
        if ( member == null ) {
            return null;
        }
        UUID memberId = member.getMemberId();
        if ( memberId == null ) {
            return null;
        }
        return memberId;
    }
}
