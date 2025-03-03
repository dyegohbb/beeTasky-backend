package br.beehome.beetasky.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.beehome.beetasky.adapter.ApiResponseAdapter;
import br.beehome.beetasky.adapter.TaskAdapter;
import br.beehome.beetasky.common.MessageKeyEnum;
import br.beehome.beetasky.dto.TaskDTO;
import br.beehome.beetasky.dto.TaskFilterDTO;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.entity.Task;
import br.beehome.beetasky.entity.User;
import br.beehome.beetasky.exception.TaskNotFoundException;
import br.beehome.beetasky.repository.jpa.TaskRepository;
import br.beehome.beetasky.repository.specifications.TaskSpecification;
import br.beehome.beetasky.service.TaskService;
import br.beehome.beetasky.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskSpecification taskSpecification;
    private final TaskRepository taskRepository;
    private final TaskAdapter taskAdapter;
    private final ApiResponseAdapter responseAdapter;
    private final UserService userService;

    @Override
    public ApiResponse<List<TaskDTO>> listAllTasksByUser(String loggedUser, TaskFilterDTO taskFilter,
	    Pageable pageable) {

	User user = userService.getUserByUsernameOrEmail(loggedUser);
	final Specification<Task> specificationFilter = taskSpecification.filter(taskFilter, user.getIdentifier());

	List<TaskDTO> tasks = taskRepository.findAll(specificationFilter, pageable).stream().map(taskAdapter::toDTO)
		.toList();

	return responseAdapter.toSuccess(tasks, MessageKeyEnum.SUCCESS,
		tasks.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @Override
    public ApiResponse<TaskDTO> createTask(String loggedUser, TaskDTO taskDTO) {
	User user = userService.getUserByUsernameOrEmail(loggedUser);
	Task savedTask = taskRepository.save(taskAdapter.toEntity(taskDTO, user));

	return responseAdapter.toSuccess(taskAdapter.toDTO(savedTask), MessageKeyEnum.SUCCESS, HttpStatus.OK);
    }

    @Override
    public ApiResponse<TaskDTO> updateTask(String loggedUser, String taskIdentifier, TaskDTO taskDTO) {

	User user = userService.getUserByUsernameOrEmail(loggedUser);
	Task task = taskRepository.findByTaskIdentifierAndUserIdentifier(taskIdentifier, user.getIdentifier())
		.orElseThrow(() -> new TaskNotFoundException(taskDTO.getIdentifier()));

	task.setTitle(taskDTO.getTitle());
	task.setDescription(taskDTO.getDescription());
	task.setStatus(taskDTO.getStatus());
	task.setDeadline(taskDTO.getDeadline());
	task.setUpdatedOn(LocalDateTime.now());

	taskRepository.save(task);

	TaskDTO updatedTaskDTO = taskAdapter.toDTO(task);

	return responseAdapter.toSuccess(updatedTaskDTO, MessageKeyEnum.SUCCESS, HttpStatus.OK);
    }

    @Override
    public ApiResponse<Void> deleteTaskByIdentifier(String loggedUser, String taskIdentifier) {
	User user = userService.getUserByUsernameOrEmail(loggedUser);
	Task task = taskRepository.findByTaskIdentifierAndUserIdentifier(taskIdentifier, user.getIdentifier())
		.orElseThrow(() -> new TaskNotFoundException(taskIdentifier));

	task.logicDelete();
	taskRepository.save(task);

	return responseAdapter.toSuccess(null, MessageKeyEnum.SUCCESS, HttpStatus.OK);
    }

}
