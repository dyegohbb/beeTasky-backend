package br.beehome.beetasky.exception.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessageKeyEnum {

    GENERIC_ERROR("system.generic_error"), 
    TASK_NOT_FOUND("task.not_found.error"),
    DUPLICATED_TASK_TITLE("task.duplicated_title.error"),
    USER_NOT_FOUND("user.not_found.error"),
    UNAUTHORIZED("unauthorized.error"), 
    FORBIDDEN("forbidden.error"),
    DUPLICATED_USERNAME_EMAIL("user.duplicated_username_or_email.error"),
    USER_MISSING_AUTH_PARAMETERS("user.missing_auth_parameters.error"),
    TASK_MISSING_CREATE_PARAMETERS("task.missing_create_parameters.error"),
    USER_MISSING_CREATE_PARAMETERS("user.missing_create_parameters.error"),
    TASK_NULL("task.null.error"),
    USER_NULL("user.null.error"),
    TASK_DEADLINE_IN_THE_PAST("task.deadline_in_the_past.error"),
    TASK_FILTER_ENDDATE_BEFORE_STARTDATE("task.filter_enddate_before_startdate.error");

    private final String msgCode;

}
