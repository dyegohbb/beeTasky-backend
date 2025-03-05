package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class UserMissingAuthParameters extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.USER_MISSING_AUTH_PARAMETERS;
    private static HttpStatus status = HttpStatus.BAD_REQUEST;

    public UserMissingAuthParameters() {
	super(messageKey, status, null);
    }
    
}
