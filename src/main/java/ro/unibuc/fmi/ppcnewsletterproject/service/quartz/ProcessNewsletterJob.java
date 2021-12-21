package ro.unibuc.fmi.ppcnewsletterproject.service.quartz;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ApiException;
import ro.unibuc.fmi.ppcnewsletterproject.exception.ExceptionStatus;
import ro.unibuc.fmi.ppcnewsletterproject.model.AccountNewsletter;
import ro.unibuc.fmi.ppcnewsletterproject.model.KafkaPayload;
import ro.unibuc.fmi.ppcnewsletterproject.repository.AccountNewsletterRepository;

import javax.transaction.Transactional;

@Slf4j
public class ProcessNewsletterJob extends QuartzJobBean {

    private final QuartzService quartzService;
    private final AccountNewsletterRepository accountNewsletterRepository;

    @Autowired
    public ProcessNewsletterJob(QuartzService quartzService, AccountNewsletterRepository accountNewsletterRepository) {
        this.quartzService = quartzService;
        this.accountNewsletterRepository = accountNewsletterRepository;
    }

    @Override
    @Transactional
    public void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {

        try {
            String jobName = jobExecutionContext.getJobDetail().getKey().getName();
            String schedulerId = jobExecutionContext.getScheduler().getSchedulerInstanceId();
            Long accountNewsletterTriggerKey = Long.parseLong(jobExecutionContext.getTrigger().getKey().getName());

            AccountNewsletter accountNewsletter = accountNewsletterRepository.findById(accountNewsletterTriggerKey).orElseThrow(
                    () -> new ApiException(ExceptionStatus.ACCOUNT_NEWSLETTER_NOT_FOUND, String.valueOf(accountNewsletterTriggerKey)));

            KafkaPayload kafkaPayload = KafkaPayload.builder()
                    .accountNewsletter(accountNewsletter)
                    .build();

            log.warn(kafkaPayload + " should be sent to kafka!");
//          kafKaProducerService.sendKafkaPayload(kafkaPayload);

            log.info("Executed job '" + jobName + "' fired from trigger '" + accountNewsletterTriggerKey + "' by scheduler with id '" + schedulerId + "'.");

        } catch (ApiException e) {
            log.error(e.getErrorMessage());
            String triggerName = jobExecutionContext.getTrigger().getKey().getName();
            quartzService.deleteTrigger(Long.parseLong(triggerName));
        } catch (Exception e) {
            log.error(e.toString());
        }

    }

}