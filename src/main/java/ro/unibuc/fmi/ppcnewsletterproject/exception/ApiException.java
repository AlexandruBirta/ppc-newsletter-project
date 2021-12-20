package ro.unibuc.fmi.ppcnewsletterproject.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ExceptionStatus exceptionStatus;
    private final String errorParameter;
    private final String errorMessage;

    public ApiException(ExceptionStatus exceptionStatus, String errorParameter) {
        this.exceptionStatus = exceptionStatus;
        this.errorParameter = errorParameter;
        this.errorMessage = String.format(exceptionStatus.toString(), errorParameter);
    }

}

