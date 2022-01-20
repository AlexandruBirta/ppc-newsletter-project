package ro.unibuc.fmi.ppcnewsletterproject.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.model.KafkaPayload;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    @Value("${topic.name.producer}")
    private String topicName;

    private final KafkaTemplate<String, KafkaPayload> kafkaTemplate;

    public void sendKafkaPayload(KafkaPayload kafkaPayload) {
        log.info("Payload: {}", kafkaPayload.toString());
        kafkaTemplate.send(topicName, kafkaPayload);
    }

}
