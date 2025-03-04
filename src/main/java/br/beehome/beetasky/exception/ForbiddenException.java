package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class ForbiddenException extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.FORBIDDEN;
    private static HttpStatus status = HttpStatus.FORBIDDEN;

    public ForbiddenException(Object... args) {
	super(messageKey, status, args);
    }
    
}
