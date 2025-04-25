package com.example.BorrowBookService.jpa.repository;

import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.exception.NotFoundException;
import com.example.BorrowBookService.jpa.repository.jpa.JpaMemberRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class JpaMemberRepositoryAdapter implements MemberRepository {
    private final JpaMemberRepository jpaMemberRepository;

    @Override
    public Member findByIdOrThrow(UUID memberId) {
        return jpaMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found with ID: " + memberId));
    }

    @Override
    public Member save(Member member) {
        return jpaMemberRepository.save(member);
    }

    @Override
    public List<Member> saveAll(List<Member> members) {
        return jpaMemberRepository.saveAll(members);
    }

    @Override
    public List<Member> findAllWithReadyReservations() {
        return jpaMemberRepository.findAllWithReadyReservations();
    }


}
