package br.beehome.beetasky.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessageKeyEnum {

    GENERIC_ERROR("generic.error"), 
    TASK_NOT_FOUND("task.not_found.error"),
    USER_NOT_FOUND("user.not_found.error"),
    UNAUTHORIZED("unauthorized.error"), 
    FORBIDDEN("forbbiden.error");

    private final String msg;

}
