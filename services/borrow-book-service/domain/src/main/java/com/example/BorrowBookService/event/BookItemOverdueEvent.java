//package com.example.loanmanagementservice.event;
//
//import com.example.buildingblocks.shared.domain.event.DomainEvent;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//@Getter
//public class BookItemOverdueEvent implements DomainEvent {
//    private UUID eventId;
//    private final UUID bookItemId;
//    private final UUID bookId;
//    private final UUID borrowerId;
//    private final LocalDateTime dueDate;
//    private final long daysOverdue;
//    private final LocalDateTime occurredOn;
//
//    public BookItemOverdueEvent(UUID bookItemId, UUID bookId, UUID borrowerId,
//                                LocalDateTime dueDate, long daysOverdue) {
//        this.bookItemId = bookItemId;
//        this.bookId = bookId;
//        this.borrowerId = borrowerId;
//        this.dueDate = dueDate;
//        this.daysOverdue = daysOverdue;
//        this.occurredOn = LocalDateTime.now();
//    }
//
//    // Getters
//    public UUID getBookItemId() {
//        return bookItemId;
//    }
//
//    public UUID getBookId() {
//        return bookId;
//    }
//
//    public UUID getBorrowerId() {
//        return borrowerId;
//    }
//
//    public LocalDateTime getDueDate() {
//        return dueDate;
//    }
//
//    public long getDaysOverdue() {
//        return daysOverdue;
//    }
//
//    public LocalDateTime getOccurredOn() {
//        return occurredOn;
//    }
//
//    @Override
//    public String getEventType() {
//        return "BookItemOverdueEvent";
//    }
//}
