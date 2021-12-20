package ro.unibuc.fmi.ppcnewsletterproject.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "newsletters")
public class Newsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NewsletterType type;

    // cron job syntax
    @Column(nullable = false)
    private String time;

    @ToString.Exclude
    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id"
    )
    @ManyToOne(
            targetEntity = Account.class,
            fetch = FetchType.EAGER
    )
    private Account account;

    @CreationTimestamp
    private LocalDateTime insertedDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Getter
    public enum NewsletterType {
        WIKIPEDIA_ARTICLE("wikipediaArticle"),
        BACON_IPSUM("baconIpsum"),
        CAT_PHOTO("catPhoto");

        private String value;

        NewsletterType(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static NewsletterType fromValue(String text) {
            for (NewsletterType b : NewsletterType.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
}
