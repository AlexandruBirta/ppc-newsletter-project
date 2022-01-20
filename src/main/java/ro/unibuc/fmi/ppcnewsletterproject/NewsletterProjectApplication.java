package ro.unibuc.fmi.ppcnewsletterproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class NewsletterProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsletterProjectApplication.class, args);
    }

}
