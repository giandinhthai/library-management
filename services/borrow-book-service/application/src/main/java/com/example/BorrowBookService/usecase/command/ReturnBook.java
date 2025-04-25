package com.example.BorrowBookService.usecase.command;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.borrow.mapper.BorrowMapper;
import com.example.BorrowBookService.exception.InvalidReturnRequestException;
import com.example.BorrowBookService.repository.BorrowItemReadOnlyRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.BorrowBookService.usecase.BaseBookHandler;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ReturnBook implements Command<Set<BorrowResult>> {
    private List<UUID> listBookId;
    private UUID memberId;
    //borrows/:id/return

}

@Service
@AllArgsConstructor
class ReturnBookHandler extends BaseBookHandler implements RequestHandler<ReturnBook, Set<BorrowResult>> {
    private final MemberRepository memberRepository;
    private final BorrowMapper borrowMapper;
    private final BorrowItemReadOnlyRepository borrowItemReadOnlyRepository;

    @Override
    @Transactional
    public Set<BorrowResult> handle(ReturnBook request) {
        checkForDuplicateBooks(request.getListBookId(), InvalidReturnRequestException::new);
        var member = memberRepository.findByIdOrThrow(request.getMemberId());
        var bookItems = borrowItemReadOnlyRepository.getBorrowItem(request.getMemberId(), request.getListBookId());
        if (bookItems.size() != request.getListBookId().size()) {
            throw new InvalidReturnRequestException("Book not borrowed by this member");
        }
        var borrows = member.returnBook(bookItems);
        memberRepository.save(member);
        return borrows.stream()
                .map(borrowMapper::toResult)
                .collect(Collectors.toSet());
    }
}
