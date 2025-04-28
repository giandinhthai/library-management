package com.example.BorrowBookService.jpa.mapper;

import com.example.BorrowBookService.DTO.borrow.BorrowItemOnBook;
import com.example.BorrowBookService.DTO.borrow.BorrowItemResult;
import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.borrow.mapper.BorrowMapper;
import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.BorrowItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component("borrowMapper")
public interface BorrowMapperAdapter extends BorrowMapper {

    @Override
    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "borrowItems", target = "borrowItems")
    @Mapping(source = "borrowId", target = "borrowId")
    @Mapping(source = "borrowedAt", target = "borrowedAt")
    @Mapping(source = "dueDate", target = "dueDate")
    @Mapping(source = "totalFineAmount", target = "totalFineAmount")
    @Mapping(source = "finePaid", target = "finePaid")
    BorrowResult toResult(Borrow borrow);

    BorrowItemResult toResult(BorrowItem borrowItem);

    List<BorrowItemResult> toItemResultList(List<BorrowItem> borrowItems);
    @Mapping(source = "borrow.dueDate", target = "dueDate")
    @Mapping(source="borrow.member.memberId",target = "memberId")
    @Mapping(source="borrow.borrowedAt",target = "borrowAt")
    @Mapping(source="borrow.borrowId",target="borrowId")
    BorrowItemOnBook toResultOnBook(BorrowItem borrowItem);
    @Named("mapStatus")
    default String mapStatus(com.example.BorrowBookService.aggregate.BorrowStatus status) {
        return status.name();
    }
}