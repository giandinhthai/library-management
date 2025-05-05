package com.example.BorrowBookService.usecase.command;

import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.buildingblocks.cqrs.handler.RequestHandler;
import com.example.buildingblocks.cqrs.request.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUser implements Command<Void> {
    private UUID memberId;
    private String email;
}
@Service
@RequiredArgsConstructor
class CreateUserHandler implements RequestHandler<CreateUser, Void> {
    private final MemberRepository memberRepository;
    @Override
    public Void handle(CreateUser request) {
        var member=Member.create(request.getMemberId(), request.getEmail());
        memberRepository.save(member);
        return null;
    }
}
