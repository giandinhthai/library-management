package com.example.buildingblocks.eventbus.kafkaService;

import com.example.buildingblocks.shared.intergation_event.IntegrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publish(IntegrationEvent event) {
        String topic = resolveTopic(event.getEventName());
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), event);
    }

    private String resolveTopic(String eventName) {
        return eventName;
    }
}
