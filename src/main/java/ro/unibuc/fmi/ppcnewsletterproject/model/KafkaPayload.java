package ro.unibuc.fmi.ppcnewsletterproject.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KafkaPayload {

    private AccountNewsletter accountNewsletter;
}
