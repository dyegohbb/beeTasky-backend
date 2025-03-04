package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class DuplicatedUserException extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.DUPLICATED_USERNAME_EMAIL;
    private static HttpStatus status = HttpStatus.BAD_REQUEST;

    public DuplicatedUserException(Object... args) {
	super(messageKey, status, args);
    }
    
}
