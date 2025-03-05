package br.beehome.beetasky.exception;

import org.springframework.http.HttpStatus;

import br.beehome.beetasky.exception.core.CustomException;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

public class TaskFilterEndDateBeforeStartDate extends CustomException {

    private static ExceptionMessageKeyEnum messageKey = ExceptionMessageKeyEnum.TASK_FILTER_ENDDATE_BEFORE_STARTDATE;
    private static HttpStatus status = HttpStatus.BAD_REQUEST;

    public TaskFilterEndDateBeforeStartDate(Object... args) {
	super(messageKey, status, args);
    }
}
