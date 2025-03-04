package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class UnauthorizedException extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.UNAUTHORIZED;
    private static HttpStatus status = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException() {
	super(messageKey, status, null);
    }
    
}
