package ro.unibuc.fmi.ppcnewsletterproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;
import ro.unibuc.fmi.ppcnewsletterproject.repository.AccountNewsletterRepository;

import java.util.List;

@Slf4j
@Service
public class AccountNewsletterService {

    private final AccountNewsletterRepository accountNewsletterRepository;

    @Autowired
    public AccountNewsletterService(AccountNewsletterRepository accountNewsletterRepository) {
        this.accountNewsletterRepository = accountNewsletterRepository;
    }

    public void createAccountNewsletter(AccountNewsletter accountNewsletter) {
        List<AccountNewsletter> accountNewsletterList = accountNewsletterRepository.findAll();

        for (AccountNewsletter accountNewsletterEntity : accountNewsletterList) {
            if (accountNewsletter.getAccount().equals(accountNewsletterEntity.getAccount()) && accountNewsletter.getNewsletter().equals(accountNewsletterEntity.getNewsletter())) {
                throw new ApiException(ExceptionStatus.ACCOUNT_NEWSLETTER_ALREADY_EXISTS, String.valueOf(accountNewsletterEntity.getId()));
            }
        }

        accountNewsletterRepository.save(accountNewsletter);
        log.info("Created account newsletter " + accountNewsletter);
    }
}
