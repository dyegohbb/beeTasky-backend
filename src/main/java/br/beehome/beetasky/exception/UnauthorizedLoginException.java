package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class UnauthorizedLoginException extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.UNAUTHORIZED_LOGIN;
    private static HttpStatus status = HttpStatus.UNAUTHORIZED;

    public UnauthorizedLoginException() {
	super(messageKey, status, null);
    }
    
}
