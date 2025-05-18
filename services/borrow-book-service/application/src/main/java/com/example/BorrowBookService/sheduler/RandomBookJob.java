package com.example.BorrowBookService.sheduler;

import com.example.BorrowBookService.aggregate.Book;
import com.example.BorrowBookService.aggregate.Member;
import com.example.BorrowBookService.repository.BookRepository;
import com.example.BorrowBookService.repository.MemberRepository;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Component
@Slf4j
public class RandomBookJob {
    private final MemberRepository memberRepository;

    private final BookRepository bookRepository;
    private final Mediator mediator;
    private final Random random = new Random();
    @Transactional
    public void executeJob() {
        List<Member> members = memberRepository.findAll();
        List<Book> books = bookRepository.findAll(); // Assuming this method exists

//        for (int i = 0; i < 20000; i++) {
//
//            var book = books.get(random.nextInt(books.size()));
//            var member = members.get(random.nextInt(members.size()));
//
//            // Attempt to borrow a book using the service method
//            try {
//                mediator.send(new BorrowBook(Collections.singletonList(book.getBookId()), member.getMemberId()));
//                log.info("Borrowed book {} by member {}", book.getBookId(), member.getMemberId());
//            } catch (RuntimeException e) {
//                log.error("Failed to borrow book {} for member {}: {}", book.getBookId(), member.getMemberId(), e.getMessage());
//            }
//
//
//
//        }
//        for (int i=0;i<50;i++){
//            // Attempt to reserve a book
//            var memberReserve = members.get(random.nextInt(members.size()));
//            var bookReserve = books.get(random.nextInt(books.size()));
//            try {
////                mediator.send(new ReserveBook(Collections.singletonList(bookReserve.getBookId()), memberReserve.getMemberId()));
//                mediator.send(new ReserveBook(Collections.singletonList(UUID.fromString("dcc04d96-56fe-481f-a4db-ecd5c340653f")), memberReserve.getMemberId()));
//                log.info("Reserved book {} by member {}", bookReserve.getBookId(), memberReserve.getMemberId());
//            } catch (RuntimeException e) {
//                log.error("Failed to reserve book {} for member {}: {}", bookReserve.getBookId(), memberReserve.getMemberId(), e.getMessage());
//            }
//
//        }
//        for(int i=0 ; i<500;i++){
//            var memberReturn = members.get(random.nextInt(members.size()));
//            if (memberReturn.getCurrentlyBorrowedBookIds().isEmpty()) {
//                continue;
//            }
//            try{
//                mediator.send(new ReturnBook(memberReturn.getCurrentlyBorrowedBookIds().stream().toList(), memberReturn.getMemberId()));
//                log.info("{}, Returned book  by member {}",i, memberReturn.getMemberId());
//            }catch (RuntimeException e){
//                log.error("Failed to return book for member {} : {} ", memberReturn.getMemberId(), e.getMessage());
//            }
//        }

    }
}
