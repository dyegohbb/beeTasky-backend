package br.beehome.beetasky.dto;

import java.time.LocalDateTime;

import br.beehome.beetasky.common.TaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaskDTO {

	private String identifier;
	private String title;
	private String description;
	private TaskStatusEnum status;
	private LocalDateTime createdOn;
	private LocalDateTime deadline;
	private UserDTO assignedTo;
}
