package ro.unibuc.fmi.ppcnewsletterproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.ppcnewsletterproject.model.Newsletter;
import ro.unibuc.fmi.ppcnewsletterproject.service.NewsletterService;
import ro.unibuc.fmi.ppcnewsletterproject.service.quartz.QuartzService;

import javax.transaction.Transactional;

@RestController
@RequestMapping(path = "/v1")
public class NewsletterQuartzController {

    private final NewsletterService newsletterService;
    private final QuartzService quartzService;

    @Autowired
    public NewsletterQuartzController(NewsletterService newsletterService, QuartzService quartzService) {
        this.newsletterService = newsletterService;
        this.quartzService = quartzService;
    }

    @PostMapping(path = "/accounts/{accountId}")
    @Transactional
    public void addNewsletterSubscription(@RequestBody Newsletter newsletter, @PathVariable("accountId") Long accountId) {
        newsletterService.createNewsletter(newsletter);
        quartzService.addTrigger(newsletter);
    }
}
