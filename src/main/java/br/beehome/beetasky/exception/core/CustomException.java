package br.beehome.beetasky.exception.core;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final String message;
    private final HttpStatus status;
    private final Object[] args;
	
    public CustomException(HttpStatus status, String message, Object... args) {
        super(message);
        this.status = status;
        this.message = message;
        this.args = args;
    }
    
    public CustomException(ExceptionMessageKeyEnum messageKey, HttpStatus status, Object[] args) {
		super(messageKey.getMsg());
		this.message = messageKey.getMsg();
		this.status = status;
		this.args = args;
	}
}
