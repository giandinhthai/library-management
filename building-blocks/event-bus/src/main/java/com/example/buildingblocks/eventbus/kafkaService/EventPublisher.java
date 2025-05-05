package com.example.buildingblocks.eventbus.kafkaService;

import com.example.buildingblocks.shared.intergation_event.IntegrationEvent;

public interface EventPublisher {
    void publish(IntegrationEvent event);
}