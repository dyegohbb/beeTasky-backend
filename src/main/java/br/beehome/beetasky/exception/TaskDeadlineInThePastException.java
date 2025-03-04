package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class TaskDeadlineInThePastException extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.TASK_DEADLINE_IN_THE_PAST;
    private static HttpStatus status = HttpStatus.BAD_REQUEST;

    public TaskDeadlineInThePastException(Object... args) {
	super(messageKey, status, args);
    }
    
}
