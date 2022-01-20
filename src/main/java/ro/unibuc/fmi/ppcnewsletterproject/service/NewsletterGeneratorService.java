package ro.unibuc.fmi.ppcnewsletterproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;
import ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator.NewsletterGeneratorFactory;

@Slf4j
@Service
public class NewsletterGeneratorService {

    private final NewsletterGeneratorFactory newsletterGeneratorFactory;

    @Autowired
    public NewsletterGeneratorService(NewsletterGeneratorFactory newsletterGeneratorFactory) {
        this.newsletterGeneratorFactory = newsletterGeneratorFactory;
    }

    public void sendEmail(AccountNewsletter accountNewsletter) {

        try {
            String content = newsletterGeneratorFactory.make(accountNewsletter).getEmailHTML();
            //TODO IMPLEMENT SEND EMAIL SERVICE
            log.warn(content);
        } catch (Exception e) {
            throw new ApiException(ExceptionStatus.UNEXPECTED_EXCEPTION, e.getClass().getSimpleName());
        }

    }
}
