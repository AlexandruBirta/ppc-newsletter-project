package ro.unibuc.fmi.ppcnewsletterproject.service;

import lombok.extern.slf4j.Slf4j;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LoadBalancer {

    private final Integer numberOfThreads;
    private final List<AccountNewsletter> accountNewsletterList;
    public Object[] locks;
    private final NewsletterGeneratorService newsletterGeneratorService;

    public LoadBalancer(Integer numberOfThreads, List<AccountNewsletter> accountNewsletterList, NewsletterGeneratorService newsletterGeneratorService) {
        this.numberOfThreads = numberOfThreads;
        this.accountNewsletterList = accountNewsletterList;
        this.newsletterGeneratorService = newsletterGeneratorService;
        initialiseThreads();
    }

    public void initialiseThreads() {
        locks = new Object[numberOfThreads + 1];

        int i;
        int j;

        //Initializing instances in the array
        for (i = 0; i < this.numberOfThreads; ++i) {
            locks[i] = new Object();
        }

        List<Thread> threadList = new ArrayList<>();

        // Creating threads   !!! Round robin
        for (j = 0; j < this.numberOfThreads; j++) {

            List<AccountNewsletter> accountNewsletterListForThreadX = new ArrayList<>();
            int k = j;

            while (k < accountNewsletterList.size()) {
                accountNewsletterListForThreadX.add(accountNewsletterList.get(k));
                k = k + numberOfThreads;
            }

            Thread thread = new Thread(new ThreadLoadBalancer(accountNewsletterListForThreadX, newsletterGeneratorService));
            threadList.add(thread);
            thread.start();

        }

        for (Thread thread : threadList) {

            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new ApiException(ExceptionStatus.UNEXPECTED_EXCEPTION, e.getClass().getSimpleName());
            }

        }

        ThreadLoadBalancer.setThreadCount(0);

    }

    public void startRoundRobin() {
        synchronized (locks[0]) {
            locks[0].notify();
        }

    }

}


