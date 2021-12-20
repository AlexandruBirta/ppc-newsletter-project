package ro.unibuc.fmi.ppcnewsletterproject.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
public class HandledException {
    private final String errorMessage;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;

}