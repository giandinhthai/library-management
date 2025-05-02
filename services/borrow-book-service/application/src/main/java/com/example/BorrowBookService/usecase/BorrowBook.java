package com.example.BorrowBookService.usecase;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.DTO.borrow.mapper.BorrowMapper;
import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.exception.UnvalidBorrowRequestException;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BorrowBook implements com.example.buildingblocks.cqrs.request.Command<BorrowResult> {
    private List<String> listBookId;
    private String memberId;
}


@Service
@RequiredArgsConstructor

class BorrowBookHandler implements RequestHandler<BorrowBook, BorrowResult> {
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BorrowMapper borrowMapper;

    @Override
    @Transactional
    public BorrowResult handle(BorrowBook request) {
        UUID memberId = UUID.fromString(request.getMemberId());
        List<UUID> bookUUIDs = request.getListBookId().stream()
                .map(UUID::fromString)
                .toList();
        checkAvailable(bookUUIDs);
        List<Integer> booksPrice = bookRepository.getAllPriceByBookId(bookUUIDs);
        Member member = memberRepository.findByIdOrThrow(memberId);


        Borrow borrowResult = member.borrow(bookUUIDs, booksPrice);
        memberRepository.save(member);

        return borrowMapper.toResult(borrowResult);//todo
    }

    private void checkAvailable(List<UUID> bookUUIDs) {
        List<Boolean> ListBookAvailable = bookRepository.checkAvailableBook(bookUUIDs);
        Set<UUID> notAvailableBookIds = new HashSet<>();
        for (int i = 0; i < ListBookAvailable.size(); i++) {
            if (!ListBookAvailable.get(i)) {
                notAvailableBookIds.add(bookUUIDs.get(i));
            }
        }
        if (!notAvailableBookIds.isEmpty()) {
            throw new UnvalidBorrowRequestException("Some books is not available: " + notAvailableBookIds.toString());
        }
    }
}