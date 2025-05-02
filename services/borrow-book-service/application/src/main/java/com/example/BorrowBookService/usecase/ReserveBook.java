package com.example.BorrowBookService.usecase;

import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.DTO.reverse.mapper.ReserveMapper;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.aggregate.Reservation;
import com.example.BorrowBookService.exception.UnvalidReservationRequestException;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Request;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ReserveBook implements Request<List<ReserveResult>> {
    private List<String> listBookId;
    private String memberId;
}

@Service
@RequiredArgsConstructor
class ReserveBookHandler implements RequestHandler<ReserveBook, List<ReserveResult>> {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ReserveMapper reserveMapper;
    @Override
    public List<ReserveResult> handle(ReserveBook request) {
        UUID memberId = UUID.fromString(request.getMemberId());
        List<UUID> bookUUIDs = request.getListBookId().stream()
                .map(UUID::fromString)
                .toList();
        checkBookCanReserve(request.getListBookId());
        Member member = memberRepository.findByIdOrThrow(memberId);
        List<Reservation> reservations = member.reserve(bookUUIDs);
        memberRepository.save(member);
        return reservations.stream()
                .map(reserveMapper::toResult)
                .toList();
    }

    private void checkBookCanReserve(List<String> listBookId) {
        List<UUID> bookUUIDs = listBookId.stream()
                .map(UUID::fromString)
                .toList();
        List<Boolean> listBookAvailableForReserve =
                bookRepository.checkAvailableBookForReserve(bookUUIDs);

        List<UUID> notAvailableBookIds = new ArrayList<>();
        for (int i = 0; i < listBookAvailableForReserve.size(); i++) {
            if (!listBookAvailableForReserve.get(i)) {
                notAvailableBookIds.add(bookUUIDs.get(i));
            }
        }
        if (!notAvailableBookIds.isEmpty()) {
            throw new UnvalidReservationRequestException("Some books is not available for reserve: " + notAvailableBookIds.toString());
        }
    }
}
