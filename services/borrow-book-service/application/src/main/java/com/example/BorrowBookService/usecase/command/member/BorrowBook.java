package com.example.BorrowBookService.usecase.command.member;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.borrow.mapper.BorrowMapper;
import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.exception.InvalidBorrowRequestException;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.BorrowBookService.usecase.BaseBookHandler;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBook implements Command<BorrowResult> {
    private List<UUID> listBookId;
    private UUID memberId;
}


@Service
@RequiredArgsConstructor
class BorrowBookHandler extends BaseBookHandler implements RequestHandler<BorrowBook, BorrowResult> {
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowMapper borrowMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public BorrowResult handle(BorrowBook request) {
        Member member = memberRepository.findByIdOrThrow(request.getMemberId());
        validateBooksCanBorrow(request.getListBookId());

        Map<UUID, Integer> booksPrice = bookRepository.getAllPriceByBookId(request.getListBookId());

        Borrow borrowResult = member.borrow(booksPrice);
        memberRepository.save(member);
        return borrowMapper.toResult(borrowResult);
    }

    private void validateBooksCanBorrow(List<UUID> bookUUIDs) {
        checkForDuplicateBooks(bookUUIDs, InvalidBorrowRequestException::new);
        validateBooksAvailability(bookUUIDs);
    }

    private void validateBooksAvailability(List<UUID> bookUUIDs) {
        var availabilityMap = bookRepository.validateAvailableBookForBorrow(bookUUIDs);
        Set<UUID> notAvailableBooks = availabilityMap.entrySet().stream()
                .filter(entry -> !entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        if (!notAvailableBooks.isEmpty()) {
            throw new InvalidBorrowRequestException("Some books are not available for borrowing", notAvailableBooks);
        }
    }

}