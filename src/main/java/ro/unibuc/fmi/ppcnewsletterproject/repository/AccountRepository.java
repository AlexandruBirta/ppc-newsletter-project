package ro.unibuc.fmi.ppcnewsletterproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.ppcnewsletterproject.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
