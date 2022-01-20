package ro.unibuc.fmi.ppcnewsletterproject.service.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Service
public class QuartzService {

    private static final JobKey NEWSLETTER_JOB_KEY = new JobKey(ProcessNewsletterJob.class.getSimpleName(), "ppc_newsletter_jobs");
    private static final String NEWSLETTER_TRIGGER_GROUP = "ppc_newsletter_triggers";
    private static final boolean IS_REPLACED = false;

    private final Scheduler scheduler;

    @Autowired
    public QuartzService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    public void init() {
        try {
            this.scheduler.start();

            if (this.scheduler.getJobDetail(NEWSLETTER_JOB_KEY) == null) {

                JobDetail jobDetail = newJob(ProcessNewsletterJob.class)
                        .withIdentity(NEWSLETTER_JOB_KEY)
                        .storeDurably()
                        .build();

                this.scheduler.addJob(jobDetail, IS_REPLACED);
            }

        } catch (SchedulerException e) {
            log.error(String.valueOf(e));
        }
    }

    @PreDestroy
    public void close() {
        try {
            this.scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error(String.valueOf(e));
        }
    }

    public void addTrigger(AccountNewsletter accountNewsletter) {

        try {
            String triggerName = String.valueOf(accountNewsletter.getId());
            TriggerKey accountNewsletterTriggerKey = new TriggerKey(triggerName, NEWSLETTER_TRIGGER_GROUP);

            if (this.scheduler.getTrigger(accountNewsletterTriggerKey) == null) {

                Trigger trigger = newTrigger()
                        .withIdentity(accountNewsletterTriggerKey)
                        .forJob(NEWSLETTER_JOB_KEY)
//                        .withSchedule(cronSchedule(accountNewsletter.getNewsletter().getTime()))
                        .withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever())
                        .startNow()
                        .build();

                this.scheduler.scheduleJob(trigger);
                log.info("Trigger for newsletter with id '" + accountNewsletter.getId() + "' added.");
            }

        } catch (SchedulerException e) {
            log.error(String.valueOf(e));
        }

    }

    public void deleteTrigger(Long accountNewsletterId) {

        try {
            String triggerName = accountNewsletterId.toString();
            TriggerKey accountNewsletterTriggerKey = new TriggerKey(triggerName, NEWSLETTER_TRIGGER_GROUP);

            this.scheduler.unscheduleJob(accountNewsletterTriggerKey);
            log.info("Trigger for newsletter with id '" + accountNewsletterId + "' deleted.");

        } catch (SchedulerException e) {
            log.error(String.valueOf(e));
        }

    }

    public void updateTrigger(Long accountNewsletterId, String newTime) {

        try {
            String triggerName = accountNewsletterId.toString();
            TriggerKey accountNewsletterTriggerKey = new TriggerKey(triggerName, NEWSLETTER_TRIGGER_GROUP);

            Trigger updatedTrigger = newTrigger()
                    .withIdentity(accountNewsletterTriggerKey)
                    .forJob(NEWSLETTER_JOB_KEY)
                    .withSchedule(cronSchedule(newTime))
                    .build();

            this.scheduler.rescheduleJob(accountNewsletterTriggerKey, updatedTrigger);
            log.info("Trigger for newsletter with id '" + accountNewsletterId + "' updated.");

        } catch (SchedulerException e) {
            log.error(String.valueOf(e));
        }

    }

}
