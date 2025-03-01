package br.beehome.beetasky.exception.core;

import org.springframework.http.HttpStatus;

public class TaskNotFound extends CustomException {

	private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.TASK_NOT_FOUND;
	private static HttpStatus status = HttpStatus.NOT_FOUND;
	
	public TaskNotFound(Object[] args) {
		super(messageKey, status, args);
	}

}
