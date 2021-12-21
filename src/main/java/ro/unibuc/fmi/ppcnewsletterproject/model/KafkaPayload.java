package ro.unibuc.fmi.ppcnewsletterproject.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class KafkaPayload {

    private AccountNewsletter accountNewsletter;
}
