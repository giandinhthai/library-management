package com.example.BorrowBookService.usecase.query.book;

import com.example.BorrowBookService.DTO.borrow.BorrowItemOnBook;
import com.example.BorrowBookService.DTO.borrow.mapper.BorrowMapper;
import com.example.BorrowBookService.aggregate.BorrowItem;
import com.example.BorrowBookService.repository.BorrowItemReadOnlyRepository;
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
public class GetBorrowOnBook implements Query<Page<BorrowItemOnBook>> {
    private UUID bookId;
    private Boolean isReturned;
    Pageable pageable;
}
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class GetBorrowOnBookHandler implements RequestHandler<GetBorrowOnBook,Page<BorrowItemOnBook>>{
    private final BorrowItemReadOnlyRepository borrowItemReadOnlyRepository;
    private final BorrowMapper borrowMapper;
    @Override
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public Page<BorrowItemOnBook> handle(GetBorrowOnBook request) {
        Page<BorrowItem> borrows = borrowItemReadOnlyRepository.getBorrowItemOnBook(
                request.getBookId(),
                request.getIsReturned(),
                request.getPageable()
        );
        return borrows.map(borrowMapper::toResultOnBook);
    }
}
