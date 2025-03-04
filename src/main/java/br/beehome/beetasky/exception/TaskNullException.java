package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class TaskNullException extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.TASK_NULL;
    private static HttpStatus status = HttpStatus.BAD_REQUEST;

    public TaskNullException() {
	super(messageKey, status, null);
    }
    
}
