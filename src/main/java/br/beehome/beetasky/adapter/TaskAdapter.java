package br.beehome.beetasky.adapter;

import org.springframework.stereotype.Component;

import br.beehome.beetasky.dto.TaskDTO;
import br.beehome.beetasky.entity.Task;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaskAdapter {
    
    private final UserAdapter userAdapter;

    public TaskDTO toDTO(Task task) {
	if (task == null) {
	    return null;
	}

	return TaskDTO.builder().identifier(task.getIdentifier())
		.title(task.getTitle())
		.description(task.getDescription())
		.status(task.getStatus())
		.createdOn(task.getCreatedOn())
		.deadline(task.getDeadline())
		.assignedTo(userAdapter.toDTO(task.getAssignedTo()))
		.build();
    }

    public Task toEntity(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }

        return Task.builder()
                .identifier(taskDTO.getIdentifier())
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .status(taskDTO.getStatus())
                .createdOn(taskDTO.getCreatedOn())
                .deadline(taskDTO.getDeadline())
                .assignedTo(userAdapter.toEntity(taskDTO.getAssignedTo()))
                .build();
    }

}
