package br.beehome.beetasky.dto;

import java.time.LocalDateTime;

import br.beehome.beetasky.common.TaskStatusEnum;

public record TaskFilterDTO(
	String taskIdentifier, 
	String title, 
	TaskStatusEnum status, 
	LocalDateTime createdOn) {}
