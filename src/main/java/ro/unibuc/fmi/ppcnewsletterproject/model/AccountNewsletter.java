package ro.unibuc.fmi.ppcnewsletterproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts_newsletters")
public class AccountNewsletter
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // cron job syntax
    @Column()
    private String time;

    @ManyToOne
    @JoinColumn(name = "account_id")
    Account account;

    @ManyToOne
    @JoinColumn(name = "newsletter_id")
    Newsletter newsletter;
}
