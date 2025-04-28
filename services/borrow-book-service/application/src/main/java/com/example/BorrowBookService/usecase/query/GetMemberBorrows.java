package com.example.BorrowBookService.usecase.query;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.borrow.mapper.BorrowMapper;
import com.example.BorrowBookService.DTO.reverse.mapper.ReserveMapper;
import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.BorrowStatus;
import com.example.BorrowBookService.repository.BorrowReadOnlyRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Query;
import lombok.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetMemberBorrows implements Query<List<BorrowResult>> {
    private UUID memberId;
    private BorrowStatus status;

}
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class GetMemberBorrowsHandler implements RequestHandler<GetMemberBorrows,List<BorrowResult>>{
    private final BorrowReadOnlyRepository borrowReadOnlyRepository;
    private final BorrowMapper borrowMapper;
    @Override
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN') or " +
            " hasRole('MEMBER') and authentication.principal.equals(#request.memberId)")
    public List<BorrowResult> handle(@Param("request") GetMemberBorrows request) {
        List<Borrow> borrows = borrowReadOnlyRepository.getBorrow(
                request.getMemberId(),
                request.getStatus()
        );
        return borrows.stream().map(borrowMapper::toResult).toList();

    }
}
