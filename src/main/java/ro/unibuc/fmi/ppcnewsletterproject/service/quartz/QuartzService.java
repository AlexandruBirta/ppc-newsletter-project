package ro.unibuc.fmi.ppcnewsletterproject.service.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.unibuc.fmi.ppcnewsletterproject.model.Newsletter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.quartz.JobBuilder.newJob;
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

    public void addTrigger(Newsletter newsletter) {

        try {
            String triggerName = String.valueOf(newsletter.getId());
            TriggerKey newsletterTriggerKey = new TriggerKey(triggerName, NEWSLETTER_TRIGGER_GROUP);

            if (this.scheduler.getTrigger(newsletterTriggerKey) == null) {

                Trigger trigger = newTrigger()
                        .withIdentity(newsletterTriggerKey)
                        .forJob(NEWSLETTER_JOB_KEY)
                        .withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                                .withIntervalInSeconds(10)
                        )
                        .build();

                this.scheduler.scheduleJob(trigger);
                log.info("Trigger for newsletter with id '" + newsletter.getId() + "' added.");
            }

        } catch (SchedulerException e) {
            log.error(String.valueOf(e));
        }

    }

    public void deleteTrigger(Long newsletterId) {

        try {
            String triggerName = newsletterId.toString();
            TriggerKey newsletterTriggerKey = new TriggerKey(triggerName, NEWSLETTER_TRIGGER_GROUP);

            this.scheduler.unscheduleJob(newsletterTriggerKey);
            log.info("Trigger for newsletter with id '" + newsletterId + "' deleted.");

        } catch (SchedulerException e) {
            log.error(String.valueOf(e));
        }

    }

    public void updateTrigger(Long newsletterId) {

        try {
            String triggerName = newsletterId.toString();
            TriggerKey newsletterTriggerKey = new TriggerKey(triggerName, NEWSLETTER_TRIGGER_GROUP);

            Trigger updatedTrigger = newTrigger()
                    .withIdentity(newsletterTriggerKey)
                    .forJob(NEWSLETTER_JOB_KEY)
                    .withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                            .withIntervalInSeconds(5)
                    )
                    .build();

            this.scheduler.rescheduleJob(newsletterTriggerKey, updatedTrigger);
            log.info("Trigger for newsletter with id '" + newsletterId + "' updated.");

        } catch (SchedulerException e) {
            log.error(String.valueOf(e));
        }

    }

}
