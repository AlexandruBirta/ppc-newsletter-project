package ro.unibuc.fmi.ppcnewsletterproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;

import java.util.List;

@Slf4j
public class ThreadLoadBalancer implements Runnable {

    private final int numberOfThreads;
    private final Object currentLock;
    private final Object nextLock;
    private final List<AccountNewsletter> accountNewsletters;
    private final NewsletterGeneratorService newsletterGeneratorService;
    public List<AccountNewsletter> accountNewsletterList;

    @Autowired
    public ThreadLoadBalancer(int numberOfThreads, Object currentLock, Object nextLock, List<AccountNewsletter> accountNewsletters, NewsletterGeneratorService newsletterGeneratorService) {
        this.numberOfThreads = numberOfThreads;
        this.currentLock = currentLock;
        this.nextLock = nextLock;
        this.accountNewsletters = accountNewsletters;
        this.newsletterGeneratorService = newsletterGeneratorService;

        log.warn("NEWSLETTERS FROM CONSTRUCTOR " + accountNewsletters);
    }

    @Override
    public void run(){

        int currentNewsletter = numberOfThreads;
        accountNewsletterList = accountNewsletters;
        log.warn("COPY OF LIST " + accountNewsletterList);

        synchronized(this) {

            while (!accountNewsletters.isEmpty()) {

                try {
                    currentLock.wait();
                    log.warn("current newsletter" + currentNewsletter);
                    log.warn("NEWSLETTER LIST " + accountNewsletterList);
                    newsletterGeneratorService.sendEmail(accountNewsletterList.get(currentNewsletter));
                    log.warn(accountNewsletterList.get(currentNewsletter).toString());
                    accountNewsletterList.remove(currentNewsletter);
                    currentNewsletter--;
                } catch(InterruptedException e) {
                    throw new ApiException(ExceptionStatus.UNEXPECTED_EXCEPTION, e.getClass().getSimpleName());
                }

                synchronized(nextLock) {
                    nextLock.notify();
                }

            }

        }

        synchronized(nextLock) {
            nextLock.notify();
        }

    }

}
