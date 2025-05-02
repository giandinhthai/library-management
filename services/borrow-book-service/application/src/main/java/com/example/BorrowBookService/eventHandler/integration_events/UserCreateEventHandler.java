package com.example.BorrowBookService.eventHandler.integration_events;

import com.example.BorrowBookService.usecase.command.CreateUser;
import com.example.buildingblocks.cqrs.mediator.Mediator;
import com.example.buildingblocks.shared.intergation_event.UserRegisteredIntegrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCreateEventHandler {
    private final Mediator mediator;

    @KafkaListener(
            topics = "user.registered",
            groupId = "user-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handle(UserRegisteredIntegrationEvent event) {
        log.info("Got event: {}", event);
        if (event.getRole().equals("MEMBER")) {
            mediator.send(new CreateUser(event.getUserId(), event.getEmail()));
        }
    }
}
