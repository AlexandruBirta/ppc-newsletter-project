package ro.unibuc.fmi.ppcnewsletterproject.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ro.unibuc.fmi.ppcnewsletterproject.model.KafkaPayload;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate, @Value("${kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendKafkaPayload(KafkaPayload kafkaPayload) {

        String kafkaPayloadKey = kafkaPayload.getAccountNewsletter().getId() + kafkaPayload.getAccountNewsletter().getAccount().getEmail();

        ListenableFuture<SendResult<String, Object>> future = this.kafkaTemplate.send(topic, kafkaPayloadKey, kafkaPayload);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Sent message=[" + kafkaPayload +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=["
                        + kafkaPayload + "] due to : " + ex.getMessage());
            }
        });

    }
}
