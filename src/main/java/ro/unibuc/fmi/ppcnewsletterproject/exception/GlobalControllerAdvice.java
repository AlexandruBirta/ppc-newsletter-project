package ro.unibuc.fmi.ppcnewsletterproject.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = {ApiException.class})
    public ResponseEntity<HandledException> handleApiException(ApiException e) {

        log.error(e.getErrorMessage());

        return new ResponseEntity<>(
                HandledException.builder()
                        .errorMessage(e.getErrorMessage())
                        .httpStatus(e.getExceptionStatus().getHttpStatus())
                        .timestamp(LocalDateTime.now())
                        .build(),
                e.getExceptionStatus().getHttpStatus()
        );
    }

}
