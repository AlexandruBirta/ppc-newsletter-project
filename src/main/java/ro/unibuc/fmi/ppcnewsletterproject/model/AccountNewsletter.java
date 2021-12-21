package ro.unibuc.fmi.ppcnewsletterproject.model;

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
@Table(name = "account_newsletters")
public class AccountNewsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Long id;

    @JoinColumn(
            name = "account_id",
            referencedColumnName = "id"
    )
    @OneToOne(
            targetEntity = Account.class,
            fetch = FetchType.EAGER
    )
    private Account account;

    @JoinColumn(
            name = "newsletter_id",
            referencedColumnName = "id"
    )
    @OneToOne(
            targetEntity = Newsletter.class,
            fetch = FetchType.EAGER
    )
    private Newsletter newsletter;

    @CreationTimestamp
    private LocalDateTime insertedDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;
}
