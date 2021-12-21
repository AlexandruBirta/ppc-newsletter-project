package ro.unibuc.fmi.ppcnewsletterproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.ppcnewsletterproject.model.Account;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountDTO;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;
import ro.unibuc.fmi.ppcnewsletterproject.model.Newsletter;
import ro.unibuc.fmi.ppcnewsletterproject.service.AccountNewsletterService;
import ro.unibuc.fmi.ppcnewsletterproject.service.AccountService;
import ro.unibuc.fmi.ppcnewsletterproject.service.NewsletterService;
import ro.unibuc.fmi.ppcnewsletterproject.service.quartz.QuartzService;

import javax.transaction.Transactional;

@RestController
@RequestMapping(path = "/v1")
public class NewsletterQuartzController {

    private final NewsletterService newsletterService;
    private final QuartzService quartzService;
    private final AccountService accountService;
    private final AccountNewsletterService accountNewsletterService;

    @Autowired
    public NewsletterQuartzController(NewsletterService newsletterService, QuartzService quartzService, AccountService accountService, AccountNewsletterService accountNewsletterService) {
        this.newsletterService = newsletterService;
        this.quartzService = quartzService;
        this.accountService = accountService;
        this.accountNewsletterService = accountNewsletterService;
    }

    @PostMapping(path = "/newsletters/{newsletterType}")
    @Transactional
    public void addNewsletterSubscription(@RequestBody AccountDTO account, @PathVariable("newsletterType") String newsletterType) {
        Account accountEntity = accountService.createAccount(account);
        Newsletter newsletter = newsletterService.getNewsletterByType(Newsletter.NewsletterType.fromValue(newsletterType));

        AccountNewsletter accountNewsletter = AccountNewsletter.builder()
                .account(accountEntity)
                .newsletter(newsletter)
                .build();

        accountNewsletterService.createAccountNewsletter(accountNewsletter);

        quartzService.addTrigger(accountNewsletter);
    }
}
