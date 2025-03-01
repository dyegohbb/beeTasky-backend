package br.beehome.beetasky.exception.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessageKeyEnum {

	GENERIC_ERROR("generic.error"),
	TASK_NOT_FOUND("task.not_found.error");
	
	private final String msg;

}
