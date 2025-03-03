package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;

public class TaskNotFoundException extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.TASK_NOT_FOUND;
    private static HttpStatus status = HttpStatus.NOT_FOUND;

    public TaskNotFoundException(Object... args) {
	super(messageKey, status, args);
    }

}
