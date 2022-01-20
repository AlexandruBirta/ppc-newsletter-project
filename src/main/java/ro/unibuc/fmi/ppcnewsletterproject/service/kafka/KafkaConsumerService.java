package ro.unibuc.fmi.ppcnewsletterproject.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.model.KafkaPayload;

@Slf4j
@Service
public class KafkaConsumerService {


    @KafkaListener(topics = "#{'${kafka.topic}'.split(',')}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeAll(
            @Payload KafkaPayload message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Long partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long receivedTimestamp
    ) {

        log.info("Consumed payload '{}' from topic {}, partition {} and offset {} with receivedTimestamp {}.", message, topic, partition, offset, receivedTimestamp);
    }

}
