package ro.unibuc.fmi.ppcnewsletterproject.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.model.KafkaPayload;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumerService {

    @Value("${topic.name.consumer")
    private String topicName;

    @KafkaListener(topics = "${topic.name.consumer}", groupId = "ppc")
    public void consume(ConsumerRecord<String, KafkaPayload> payload) {
        log.info("Topic: {}", topicName);
        log.info("key: {}", payload.key());
        log.info("Headers: {}", payload.headers());
        log.info("Order: {}", payload.value());
    }
}
