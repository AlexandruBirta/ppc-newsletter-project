package ro.unibuc.fmi.ppcnewsletterproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.ppcnewsletterproject.model.Newsletter;

import java.util.Optional;

@Repository
public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {

    public Optional<Newsletter> getNewsletterByType(Newsletter.NewsletterType newsletterType);
}
