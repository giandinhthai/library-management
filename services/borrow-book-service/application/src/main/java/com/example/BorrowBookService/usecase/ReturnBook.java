package com.example.BorrowBookService.usecase;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ReturnBook implements Command<List<BorrowResult>> {
    private List<String> listBookId;
    private String memberId;

}

@Service
@AllArgsConstructor
class ReturnBookHandler implements RequestHandler<ReturnBook, List<BorrowResult>> {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public List<BorrowResult> handle(ReturnBook request) {
        var memberId = UUID.fromString(request.getMemberId());

        var bookUUIDs = request.getListBookId().stream()
                .map(UUID::fromString)
                .toList();
        var member = memberRepository.findByIdOrThrow(memberId);
        member.returnBook(bookUUIDs);
        memberRepository.save(member);
        return List.of();
    }
}
