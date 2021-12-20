package ro.unibuc.fmi.ppcnewsletterproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.Newsletter;
import ro.unibuc.fmi.ppcnewsletterproject.repository.NewsletterRepository;

import java.util.List;

@Service
@Slf4j
public class NewsletterService {

    private final NewsletterRepository newsletterRepository;


    public NewsletterService(NewsletterRepository newsletterRepository) {
        this.newsletterRepository = newsletterRepository;
    }

    public Newsletter getNewsletter(Long newsletterId) {

        return newsletterRepository.findById(newsletterId).orElseThrow(
                () -> new ApiException(ExceptionStatus.NEWSLETTER_NOT_FOUND, String.valueOf(newsletterId)));
    }

    public Newsletter getNewsletterByType(Newsletter.NewsletterType newsletterType) {

        return newsletterRepository.getNewsletterByType(newsletterType).orElseThrow(
                () -> new ApiException(ExceptionStatus.INVALID_NEWSLETTER_TYPE, newsletterType.name()));
    }

    public void createNewsletter(Newsletter newsletter) {

        List<Newsletter> newsletterList = newsletterRepository.findAll();

        for (Newsletter newsletterEntity : newsletterList) {
            if (newsletter.getType().equals(newsletterEntity.getType()) && newsletter.getName().equals(newsletterEntity.getName()) && newsletter.getAccount().getId().equals(newsletterEntity.getAccount().getId())) {
                throw new ApiException(ExceptionStatus.NEWSLETTER_ALREADY_EXISTS, String.valueOf(newsletterEntity.getId()));
            }
        }

        newsletterRepository.save(newsletter);
        log.info("Created newsletter " + newsletter);
    }

    public void deleteNewsletter(Long newsletterId) {

        if (!newsletterRepository.existsById(newsletterId)) {
            throw new ApiException(ExceptionStatus.NEWSLETTER_NOT_FOUND, String.valueOf(newsletterId));
        }

        newsletterRepository.deleteById(newsletterId);
        log.info("Deleted newsletter with id '" + newsletterId + "'");
    }
}
