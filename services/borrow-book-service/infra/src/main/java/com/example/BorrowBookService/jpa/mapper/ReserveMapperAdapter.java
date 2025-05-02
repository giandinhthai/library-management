package com.example.BorrowBookService.jpa.mapper;

import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.DTO.reverse.mapper.ReserveMapper;
import com.example.BorrowBookService.aggregate.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component("reserveMapper")
public interface ReserveMapperAdapter extends ReserveMapper {
    @Mapping(source = "reservationId", target = "reserveId")
    @Mapping(source = "bookId", target = "bookId")
    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    @Mapping(source = "reservedAt", target = "reservedAt")
    @Mapping(source = "expiresAt", target = "expiresAt")
    ReserveResult toResult(Reservation reservation);
    @Named("statusToString")
    default String statusToString(com.example.BorrowBookService.aggregate.ReservationStatus status) {
        return status != null ? status.name() : null;
    }
}
