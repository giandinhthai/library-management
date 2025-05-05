package com.example.BorrowBookService.usecase.command;

import com.example.BorrowBookService.DTO.reverse.ReserveResult;
import com.example.BorrowBookService.DTO.reverse.mapper.ReserveMapper;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.exception.InvalidReservationRequestException;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.BorrowBookService.usecase.BaseBookHandler;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Request;
import lombok.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveBook implements Request<List<ReserveResult>> {
    private List<UUID> listBookId;
    private UUID memberId;
}

@Service
@RequiredArgsConstructor
class ReserveBookHandler extends BaseBookHandler implements RequestHandler<ReserveBook, List<ReserveResult>> {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final ReserveMapper reserveMapper;


    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN') or" +
            " hasRole('MEMBER') and authentication.principal.equals(#request.memberId)")
    public List<ReserveResult> handle(@Param("request") ReserveBook request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal().equals(request.getMemberId()));
        validateBookCanReserve(request.getListBookId());
        Member member = memberRepository.findByIdOrThrow(request.getMemberId());
        Set<UUID> bookIds = new HashSet<>(request.getListBookId());
        var reservations = member.reserve(bookIds);
        memberRepository.save(member);
        return reservations.stream()
                .map(reserveMapper::toResult)
                .toList();
    }

    private void validateBookCanReserve(List<UUID> bookUUIDs) {
        checkForDuplicateBooks(bookUUIDs, InvalidReservationRequestException::new);
        validateBooksAvailability(bookUUIDs);
    }


    private void validateBooksAvailability(List<UUID> bookUUIDs) {
        var bookAvailableForReserve = bookRepository.checkAvailableBookForReserve(bookUUIDs);
        Set<UUID> notAvailableBookIds = bookAvailableForReserve.entrySet().stream()
                .filter(entry -> !entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        if (!notAvailableBookIds.isEmpty()) {
            throw new InvalidReservationRequestException("Some books is not available for reserve: " + notAvailableBookIds.toString());
        }
    }
}
