package ro.unibuc.fmi.ppcnewsletterproject.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;

public enum ExceptionStatus {

    ACCOUNT_NOT_FOUND("Account with id '%s' not found!", HttpStatus.NOT_FOUND),

    ACCOUNT_ALREADY_EXISTS("Account with id '%s' already exists!", HttpStatus.BAD_REQUEST),

    NEWSLETTER_NOT_FOUND("Newsletter with id '%s' not found!", HttpStatus.NOT_FOUND),

    NEWSLETTER_ALREADY_EXISTS("Newsletter with id '%s' already exists!", HttpStatus.BAD_REQUEST),

    INVALID_NEWSLETTER_TYPE("Newsletter of type '%s' not found!", HttpStatus.NOT_FOUND),

    ACCOUNT_NEWSLETTER_NOT_FOUND("Account newsletter with id '%s' not found!", HttpStatus.NOT_FOUND),

    ACCOUNT_NEWSLETTER_ALREADY_EXISTS("Account newsletter with id '%s' already exists!", HttpStatus.BAD_REQUEST);

    private final String value;
    private final HttpStatus httpStatus;

    ExceptionStatus(String value, HttpStatus httpStatus) {
        this.value = value;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ExceptionStatus fromValue(String text) {
        for (ExceptionStatus b : ExceptionStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

}
