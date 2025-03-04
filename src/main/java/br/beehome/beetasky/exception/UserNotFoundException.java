package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class UserNotFoundException extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.USER_NOT_FOUND;
    private static HttpStatus status = HttpStatus.NOT_FOUND;

    public UserNotFoundException(Object... args) {
	super(messageKey, status, args);
    }

}
