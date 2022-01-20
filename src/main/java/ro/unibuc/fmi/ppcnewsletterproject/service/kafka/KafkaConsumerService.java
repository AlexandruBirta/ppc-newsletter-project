package ro.unibuc.fmi.ppcnewsletterproject.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;
import ro.unibuc.fmi.ppcnewsletterproject.model.KafkaPayload;
import ro.unibuc.fmi.ppcnewsletterproject.service.LoadBalancer;
import ro.unibuc.fmi.ppcnewsletterproject.service.NewsletterGeneratorService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KafkaConsumerService {

    private final Integer numberOfThreads;
    private final NewsletterGeneratorService newsletterGeneratorService;
    private final List<AccountNewsletter> accountNewsletterList = new ArrayList<>();

    @Autowired
    public KafkaConsumerService(@Value("${newsletter.loadbalancing.threads}") Integer numberOfThreads, NewsletterGeneratorService newsletterGeneratorService) {
        this.numberOfThreads = numberOfThreads;
        this.newsletterGeneratorService = newsletterGeneratorService;
    }


    @KafkaListener(topics = "#{'${kafka.topic}'.split(',')}", containerFactory = "kafkaListenerContainerFactory")
    public void consumeAll(
            @Payload KafkaPayload message,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) Long partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long receivedTimestamp
    ) {

        log.info("Consumed payload '{}' from topic {}, partition {} and offset {} with receivedTimestamp {}.", message, topic, partition, offset, receivedTimestamp);

        if(accountNewsletterList.size() < numberOfThreads) {
            accountNewsletterList.add(message.getAccountNewsletter());
        }

        if(accountNewsletterList.size() == numberOfThreads) {
            log.warn(accountNewsletterList.toString() + " Newsletter list from consumer");
            LoadBalancer loadBalancer = new LoadBalancer(numberOfThreads, accountNewsletterList, newsletterGeneratorService);
            loadBalancer.startRoundRobin();
            accountNewsletterList.clear();
        }

    }

}
