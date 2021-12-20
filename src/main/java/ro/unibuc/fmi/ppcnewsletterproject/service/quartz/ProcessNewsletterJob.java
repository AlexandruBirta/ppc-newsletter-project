package ro.unibuc.fmi.ppcnewsletterproject.service.quartz;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.KafkaPayload;
import ro.unibuc.fmi.ppcnewsletterproject.model.Newsletter;
import ro.unibuc.fmi.ppcnewsletterproject.repository.NewsletterRepository;

import javax.transaction.Transactional;

@Slf4j
public class ProcessNewsletterJob extends QuartzJobBean {

    private final QuartzService quartzService;
    private final NewsletterRepository newsletterRepository;

    @Autowired
    public ProcessNewsletterJob(QuartzService quartzService, NewsletterRepository newsletterRepository) {
        this.quartzService = quartzService;
        this.newsletterRepository = newsletterRepository;
    }

    @Override
    @Transactional
    public void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {

        try {
            String jobName = jobExecutionContext.getJobDetail().getKey().getName();
            String schedulerId = jobExecutionContext.getScheduler().getSchedulerInstanceId();
            Long newsletterId = Long.parseLong(jobExecutionContext.getTrigger().getKey().getName());

            Newsletter newsletter = newsletterRepository.findById(newsletterId).orElseThrow(
                    () -> new ApiException(ExceptionStatus.NEWSLETTER_NOT_FOUND, String.valueOf(newsletterId)));

            KafkaPayload kafkaPayload = KafkaPayload.builder()
                    .newsletter(newsletter)
                    .account(newsletter.getAccount())
                    .build();

            log.warn(kafkaPayload + " should be sent to kafka!");
//          kafKaProducerService.sendKafkaPayload(kafkaPayload);

            log.info("Executed job '" + jobName + "' fired from trigger '" + newsletterId + "' by scheduler with id '" + schedulerId + "'.");

        } catch (ApiException e) {
            log.error(e.getErrorMessage());
            String triggerName = jobExecutionContext.getTrigger().getKey().getName();
            quartzService.deleteTrigger(Long.parseLong(triggerName));
        } catch (Exception e) {
            log.error(e.toString());
        }

    }

}