package com.example.BorrowBookService.jpa.mapper;

import com.example.BorrowBookService.DTO.borrow.BorrowItemResult;
import com.example.BorrowBookService.DTO.borrow.BorrowResult;
import com.example.BorrowBookService.aggregate.Borrow;
import com.example.BorrowBookService.aggregate.BorrowItem;
import com.example.BorrowBookService.aggregate.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-02T16:41:21+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.jar, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class BorrowMapperAdapterImpl implements BorrowMapperAdapter {

    @Override
    public BorrowResult toResult(Borrow borrow) {
        if ( borrow == null ) {
            return null;
        }

        BorrowResult borrowResult = new BorrowResult();

        borrowResult.setStatus( mapStatus( borrow.getStatus() ) );
        borrowResult.setMemberId( borrowMemberMemberId( borrow ) );
        borrowResult.setBorrowItems( toItemResultList( borrow.getBorrowItems() ) );
        borrowResult.setBorrowId( borrow.getBorrowId() );
        borrowResult.setBorrowedAt( borrow.getBorrowedAt() );
        borrowResult.setDueDate( borrow.getDueDate() );
        borrowResult.setTotalFineAmount( borrow.getTotalFineAmount() );
        borrowResult.setFinePaid( borrow.isFinePaid() );

        return borrowResult;
    }

    @Override
    public BorrowItemResult toResult(BorrowItem borrowItem) {
        if ( borrowItem == null ) {
            return null;
        }

        BorrowItemResult borrowItemResult = new BorrowItemResult();

        borrowItemResult.setBorrowItemId( borrowItem.getBorrowItemId() );
        borrowItemResult.setBookId( borrowItem.getBookId() );
        borrowItemResult.setBookPrice( borrowItem.getBookPrice() );
        borrowItemResult.setReturned( borrowItem.isReturned() );
        borrowItemResult.setReturnedAt( borrowItem.getReturnedAt() );
        borrowItemResult.setFineAmount( borrowItem.getFineAmount() );

        return borrowItemResult;
    }

    @Override
    public List<BorrowItemResult> toItemResultList(List<BorrowItem> borrowItems) {
        if ( borrowItems == null ) {
            return null;
        }

        List<BorrowItemResult> list = new ArrayList<BorrowItemResult>( borrowItems.size() );
        for ( BorrowItem borrowItem : borrowItems ) {
            list.add( toResult( borrowItem ) );
        }

        return list;
    }

    private UUID borrowMemberMemberId(Borrow borrow) {
        if ( borrow == null ) {
            return null;
        }
        Member member = borrow.getMember();
        if ( member == null ) {
            return null;
        }
        UUID memberId = member.getMemberId();
        if ( memberId == null ) {
            return null;
        }
        return memberId;
    }
}
