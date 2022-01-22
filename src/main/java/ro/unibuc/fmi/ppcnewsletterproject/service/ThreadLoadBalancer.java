package ro.unibuc.fmi.ppcnewsletterproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
public class ThreadLoadBalancer implements Runnable {

    private List<AccountNewsletter> accountNewsletterList;
    private static int threadCount = 0;
    private final int id;
    private final NewsletterGeneratorService newsletterGeneratorService;

    @Autowired
    public ThreadLoadBalancer(List<AccountNewsletter> accountNewsletterList, NewsletterGeneratorService newsletterGeneratorService) {
        this.accountNewsletterList = accountNewsletterList;
        this.newsletterGeneratorService = newsletterGeneratorService;
        this.id = threadCount++;
    }

    @Override
    public void run() {

        while (!accountNewsletterList.isEmpty()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Thread.currentThread().setName("Load Balancing Thread " + id);
            log.warn(Thread.currentThread().getName() + " started at " + timestamp + " with Status:" + Thread.currentThread().getState());
            newsletterGeneratorService.sendEmail(accountNewsletterList.get(accountNewsletterList.size() - 1));
            accountNewsletterList.remove(accountNewsletterList.size() - 1);
        }

    }

    public static void setThreadCount(int threadCount) {
        ThreadLoadBalancer.threadCount = threadCount;
    }

}
