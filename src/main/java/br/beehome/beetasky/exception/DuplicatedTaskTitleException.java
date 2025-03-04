package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class DuplicatedTaskTitleException extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.DUPLICATED_TASK_TITLE;
    private static HttpStatus status = HttpStatus.BAD_REQUEST;

    public DuplicatedTaskTitleException(Object... args) {
	super(messageKey, status, args);
    }
    
}
