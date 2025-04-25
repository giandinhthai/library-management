package com.example.BorrowBookService.event;

import com.example.buildingblocks.shared.domain.event.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
@Getter
public class MemberBorrowBooksEvent  {
    private final UUID eventId;
    private final UUID memberId;
    private final Set<UUID> bookIds;
    private final LocalDateTime occurredOn;
    public MemberBorrowBooksEvent(UUID memberId, Set<UUID> bookIds) {
        this.eventId = UUID.randomUUID();
        this.bookIds = bookIds;
        this.memberId = memberId;
        this.occurredOn = LocalDateTime.now();
    }

}
