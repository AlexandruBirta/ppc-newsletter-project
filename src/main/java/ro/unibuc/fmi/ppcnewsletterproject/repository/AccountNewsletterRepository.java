package ro.unibuc.fmi.ppcnewsletterproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;

@Repository
public interface AccountNewsletterRepository extends JpaRepository<AccountNewsletter, Long> {
}
