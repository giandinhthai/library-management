//package com.example.loanmanagementservice.event;
//
//import com.example.buildingblocks.shared.domain.event.DomainEvent;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//public class ReservationCreatedEvent implements DomainEvent {
//    private UUID eventId;
//    private LocalDateTime occurredOn;
//    private UUID reservationId;
//    private UUID bookId;
//    private UUID memberId;
//
//    public ReservationCreatedEvent( LocalDateTime occurredOn, UUID reservationId, UUID bookId, UUID memberId) {
//        this.eventId = UUID.randomUUID();
//        this.occurredOn = occurredOn;
//        this.reservationId = reservationId;
//        this.bookId = bookId;
//        this.memberId = memberId;
//    }
//
//    @Override
//    public LocalDateTime getOccurredOn() {
//        return occurredOn;
//    }
//
//    @Override
//    public String getEventType() {
//        return "ReservationCreatedEvent";
//    }
//}
