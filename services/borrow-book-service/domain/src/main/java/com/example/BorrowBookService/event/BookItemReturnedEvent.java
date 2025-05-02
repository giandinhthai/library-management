//package com.example.loanmanagementservice.event;
//
//import com.example.buildingblocks.shared.domain.event.DomainEvent;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//@Getter
//public class BookItemReturnedEvent implements DomainEvent {
//    private UUID eventId;
//    private final UUID bookItemId;
//    private final UUID bookId;
//    private final UUID borrowerId;
//    private final LocalDateTime returnedAt;
//    private final boolean wasOverdue;
//    private final long daysOverdue;
//    private final LocalDateTime occurredOn;
//
//    public BookItemReturnedEvent(UUID bookItemId, UUID bookId, UUID borrowerId,
//                                 LocalDateTime returnedAt, boolean wasOverdue, long daysOverdue) {
//        this.eventId = UUID.randomUUID();
//        this.bookItemId = bookItemId;
//        this.bookId = bookId;
//        this.borrowerId = borrowerId;
//        this.returnedAt = returnedAt;
//        this.wasOverdue = wasOverdue;
//        this.daysOverdue = daysOverdue;
//        this.occurredOn = LocalDateTime.now();
//    }
//
//    @Override
//    public String getEventType() {
//        return "BookItemReturnedEvent";
//    }
//}
