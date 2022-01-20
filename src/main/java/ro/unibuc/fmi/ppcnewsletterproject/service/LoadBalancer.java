package ro.unibuc.fmi.ppcnewsletterproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;

import java.util.List;

@Slf4j
public class LoadBalancer {

    private final Integer numberOfThreads;
    private final List<AccountNewsletter> accountNewsletterList;
    private final NewsletterGeneratorService newsletterGeneratorService;
    public Object[] locks;

    public LoadBalancer(Integer numberOfThreads, List<AccountNewsletter> accountNewsletterList, NewsletterGeneratorService newsletterGeneratorService) {
        this.numberOfThreads = numberOfThreads;
        this.accountNewsletterList = accountNewsletterList;
        this.newsletterGeneratorService = newsletterGeneratorService;

        initializeThreads();
    }

    public void initializeThreads() {

        locks = new Object[numberOfThreads];

        int i;
        int j;

        //Initializing instances in the array
        for(i = 0; i < this.numberOfThreads; ++i)  {
            locks[i] = new Object();
        }

        //Creating threads
        for(j = 0; j < (this.numberOfThreads - 1); j++) {
            log.warn("NEWSLETTER LIST FROM THREADS " + accountNewsletterList);
            Thread thread = new Thread(new ThreadLoadBalancer(numberOfThreads, locks[j], locks[j + 1], accountNewsletterList, newsletterGeneratorService));
            thread.start();
            log.warn(thread.getName() + " " + thread.getId() + " " + thread.getState());
        }

        log.warn("NEWSLETTER LIST FROM THREADS " + accountNewsletterList);
        Thread thread = new Thread(new ThreadLoadBalancer(numberOfThreads, locks[numberOfThreads - 1], locks[0], accountNewsletterList, newsletterGeneratorService));
        thread.start();
        log.warn(thread.getName() + " " + thread.getId() + " " + thread.getState());

    }

    public void startRoundRobin()
    {
        synchronized (locks[0]) {
            locks[0].notify();
        }
    }

}

