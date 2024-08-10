package com.n1nt3nd0.authservice.kafka;

import com.n1nt3nd0.authservice.dto.RegisterMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(RegisterMessageDto dto){
        try {
        final String topic = "NEW_REGISTER_USER_MESSAGE_2";
        kafkaTemplate.send(topic, dto);
        log.info("Register message sent successfully: {}", dto.toString());
        }catch (Exception e){
            log.error("Error while send message IN KafkaMessageProducer: {}", e.getMessage());
        }
    }
}
