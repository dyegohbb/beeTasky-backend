package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class UserNullException extends CustomException {
    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.TASK_NULL;
    private static HttpStatus status = HttpStatus.BAD_REQUEST;

    public UserNullException() {
	super(messageKey, status, null);
    }
}
