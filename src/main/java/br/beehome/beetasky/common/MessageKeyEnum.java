package br.beehome.beetasky.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKeyEnum {
	USER_CREATED("user.created"),
	USER_UPDATED("user.updated"),
	USER_DELETED("user.deleted"),
	TASK_CREATED("task.created"),
	TASK_UPDATED("task.updated"),
	TASK_STATUS_UPDATED("task.status.updated"),
	TASK_DELETED("task.deleted"),
	TASK_LISTED("task.listed"),
	TASK_RETRIEVED("task.retrieved"),
	TOKEN_GENERATED("token.generated");
	
	private String msg;
}
