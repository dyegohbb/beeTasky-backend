package br.beehome.beetasky.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.beehome.beetasky.adapter.ApiResponseAdapter;
import br.beehome.beetasky.adapter.TaskAdapter;
import br.beehome.beetasky.common.MessageKeyEnum;
import br.beehome.beetasky.dto.TaskDTO;
import br.beehome.beetasky.dto.TaskFilterDTO;
import br.beehome.beetasky.dto.TaskUpdateStatusDTO;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.entity.Task;
import br.beehome.beetasky.entity.User;
import br.beehome.beetasky.exception.DuplicatedTaskTitleException;
import br.beehome.beetasky.exception.ForbiddenException;
import br.beehome.beetasky.exception.TaskDeadlineInThePastException;
import br.beehome.beetasky.exception.TaskFilterEndDateBeforeStartDate;
import br.beehome.beetasky.exception.TaskMissingCreateParameters;
import br.beehome.beetasky.exception.TaskNotFoundException;
import br.beehome.beetasky.exception.TaskNullException;
import br.beehome.beetasky.repository.jpa.TaskRepository;
import br.beehome.beetasky.repository.specifications.TaskSpecification;
import br.beehome.beetasky.service.TaskService;
import br.beehome.beetasky.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskSpecification taskSpecification;
    private final TaskRepository taskRepository;
    private final TaskAdapter taskAdapter;
    private final ApiResponseAdapter responseAdapter;
    private final UserService userService;

    @Override
    public ApiResponse<List<TaskDTO>> listAllTasksByUser(String loggedUser, TaskFilterDTO taskFilter,
            Pageable pageable) {

        validateTaskFilter(taskFilter);

        log.info("[TASK][LIST][{}] Listing tasks for user with filter: {}", loggedUser, taskFilter);

        log.info("[TASK][LIST][{}] Retrieving user for list tasks with filter: {}", loggedUser, taskFilter);
        User user = userService.getUserByUsernameOrEmail(loggedUser);

        log.info("[TASK][LIST][{}] Building filter specification for tasks using the provided filter", loggedUser);
        final Specification<Task> specificationFilter = taskSpecification.filter(taskFilter, user.getIdentifier());

        log.info("[TASK][LIST][{}] Querying tasks for page {}, page size {} and filter: {}", loggedUser, pageable.getPageNumber(), pageable.getPageSize(), taskFilter);
        
        Page<Task> taskPage = taskRepository.findAll(specificationFilter, pageable);
        List<TaskDTO> tasks = taskPage.stream().map(taskAdapter::toDTO).toList();

        if (tasks.isEmpty()) {
            log.info("[TASK][LIST][{}] No tasks found for user on page {}, page size {} and filter: {}", loggedUser, pageable.getPageNumber(), taskFilter);
        } else {
            log.info("[TASK][LIST][{}] Successfully retrieved {} tasks on page {}, page size {} and filter: {}", loggedUser, tasks.size(), pageable.getPageNumber(), taskFilter);
        }

        ApiResponse<List<TaskDTO>> success = responseAdapter.toSuccess(tasks, MessageKeyEnum.TASK_LISTED, 
                tasks.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);

        success.setHasNextPage(taskPage.hasNext());
        return success;
    }

    private void validateTaskFilter(TaskFilterDTO taskFilter) {
	if (taskFilter.createdOnStartDate() != null && taskFilter.createdOnEndDate() != null) {
	    if (taskFilter.createdOnEndDate().isBefore(taskFilter.createdOnStartDate())) {
		throw new TaskFilterEndDateBeforeStartDate("createdOn");
	    }
	}

	if (taskFilter.deadlineStartDate() != null && taskFilter.deadlineEndDate() != null) {
	    if (taskFilter.deadlineEndDate().isBefore(taskFilter.deadlineStartDate())) {
		throw new TaskFilterEndDateBeforeStartDate("deadline");
	    }
	}
    }


    @Override
    public ApiResponse<TaskDTO> createTask(String loggedUser, TaskDTO taskDTO) {
	
	validateRequiredFieldsToCreate(loggedUser, taskDTO);
	validatePastDeadline(loggedUser, taskDTO);
	
	String title = taskDTO.getTitle();
	
	log.info("[TASK][CREATE][{}] Starting task creation for task with title: {}", loggedUser, title);
	
	log.info("[TASK][CREATE][{}] Retrieving user for task with title: {}", loggedUser, title);
	User user = userService.getUserByUsernameOrEmail(loggedUser);
	
	if (taskRepository.existsByTitle(title)) {
	    log.warn("[TASK][CREATE][{}] Task creation failed - Duplicate title found: {}", loggedUser, title);
	    throw new DuplicatedTaskTitleException(title);
	}

	log.info("[TASK][CREATE][{}] Task with title '{}' is unique, proceeding with creation", loggedUser, title);
	Task savedTask = taskRepository.save(taskAdapter.toEntity(taskDTO, user));

	log.info("[TASK][CREATE][{}] Task with title '{}' successfully created", loggedUser, title);
	return responseAdapter.toSuccess(taskAdapter.toDTO(savedTask), MessageKeyEnum.TASK_CREATED, HttpStatus.OK);
    }

    private void validateRequiredFieldsToCreate(String loggedUser,TaskDTO taskDTO) {
	if (taskDTO == null) {
	    throw new TaskNullException();
	}
	
	List<String> requiredFieldsMissing = new ArrayList<>();

	String title = taskDTO.getTitle();
	if (StringUtils.isBlank(title)) {
	    requiredFieldsMissing.add("title");
	}

	if (taskDTO.getStatus() == null) {
	    requiredFieldsMissing.add("status");
	}

	if (taskDTO.getDeadline() == null) {
	    requiredFieldsMissing.add("deadline");
	}
	
	if(!requiredFieldsMissing.isEmpty()) {
	    log.info("[TASK][CREATE][{}] Task creation failed for task with {} title - Missing required parameters: {}",
	            loggedUser, title != null ? title : "missing", requiredFieldsMissing);
	    throw new TaskMissingCreateParameters(requiredFieldsMissing.toString());
	}
    }
    
    private void validatePastDeadline(String loggedUser, TaskDTO taskDTO) {
	if (taskDTO.getDeadline().isBefore(LocalDateTime.now())) {
	    log.warn(
		    "[TASK][CREATE][{}] Task creation failed for task with title {} - Deadline must be a future date, but got: {}",
		    loggedUser, taskDTO.getTitle(), taskDTO.getDeadline().toString());
	    throw new TaskDeadlineInThePastException(taskDTO.getDeadline().toString());
	}
    }

    @Override
    public ApiResponse<TaskDTO> updateTask(String loggedUser, String taskIdentifier, TaskDTO taskDTO) {

	log.info("[TASK][UPDATE][{}] Starting update process for task with identifier: {}", loggedUser, taskIdentifier);
	
	log.info("[TASK][UPDATE][{}] Retrieving user for update task with identifier: {}", loggedUser, taskIdentifier);
	User user = userService.getUserByUsernameOrEmail(loggedUser);

	Task task = taskRepository.findByIdentifier(taskIdentifier)
		.orElseThrow(() -> {
		    log.error("[TASK][UPDATE][{}] Task with identifier {} not found", loggedUser,
			    taskIdentifier);
		    return new TaskNotFoundException(taskIdentifier);
		});
	log.info("[TASK][UPDATE][{}] Task found with identifier: {}", loggedUser, task.getIdentifier());

	if (!task.getAssignedTo().getIdentifier().equals(user.getIdentifier())) {
	    log.warn("[TASK][UPDATE][{}] User is not authorized to update task with identifier {}", loggedUser, taskIdentifier);
	    throw new ForbiddenException("Update task with identifier: " + taskIdentifier);
	}
	log.info("[TASK][UPDATE][{}] User is authorized to update task with identifier {}", loggedUser,
		taskIdentifier);

	updateTaskFields(task, taskDTO);

	log.info("[TASK][UPDATE][{}] Updating task with new details: title={}, status={}, deadline={}", loggedUser,
		taskDTO.getTitle(), taskDTO.getStatus(), taskDTO.getDeadline());

	taskRepository.save(task);
	log.info("[TASK][UPDATE][{}] Task with identifier {} successfully updated.", loggedUser, task.getIdentifier());

	TaskDTO updatedTaskDTO = taskAdapter.toDTO(task);

	return responseAdapter.toSuccess(updatedTaskDTO, MessageKeyEnum.TASK_UPDATED, HttpStatus.OK);
    }
    
    private void updateTaskFields(Task task, TaskDTO taskDTO) {
	if (taskDTO.getTitle() != null) {
	    task.setTitle(taskDTO.getTitle());
	}

	if (taskDTO.getStatus() != null) {
	    task.setStatus(taskDTO.getStatus());
	}

	if (taskDTO.getDeadline() != null) {
	    task.setDeadline(taskDTO.getDeadline());
	}

	task.setDescription(taskDTO.getDescription());
	task.setUpdatedOn(LocalDateTime.now());
    }

    @Override
    public ApiResponse<Void> deleteTaskByIdentifier(String loggedUser, String taskIdentifier) {
	
        log.info("[TASK][DELETE][{}] Starting delete process for task with identifier: {}", loggedUser, taskIdentifier);

        log.info("[TASK][DELETE][{}] Retrieving user for delete task with identifier: {}", loggedUser, taskIdentifier);
        User user = userService.getUserByUsernameOrEmail(loggedUser);

        Task task = taskRepository.findByIdentifier(taskIdentifier)
            .orElseThrow(() -> {
                log.error("[TASK][DELETE][{}] Task with identifier {} not found", loggedUser, taskIdentifier);
                return new TaskNotFoundException(taskIdentifier);
            });
        log.info("[TASK][DELETE][{}] Task found with identifier: {}", loggedUser, task.getIdentifier());

        if (!task.getAssignedTo().getIdentifier().equals(user.getIdentifier())) {
            log.warn("[TASK][DELETE][{}] User is not authorized to delete task with identifier {}", loggedUser, taskIdentifier);
            throw new ForbiddenException("Delete task with identifier: " + taskIdentifier);
        }
        log.info("[TASK][DELETE][{}] User is authorized to delete task with identifier {}", loggedUser, taskIdentifier);

        task.logicDelete();

        taskRepository.save(task);
        log.info("[TASK][DELETE][{}] Task with identifier {} has been logically deleted.", loggedUser, task.getIdentifier());

        return responseAdapter.toSuccess(null, MessageKeyEnum.TASK_DELETED, HttpStatus.OK);
    }


    @Override
    public ApiResponse<TaskDTO> getTaskByIdentifier(String loggedUser, String identifier) {
	
        log.info("[TASK][GET][{}] Getting task for user with task identifier: {}", loggedUser, identifier);
        
        log.info("[TASK][GET][{}] Retrieving user for get task with identifier: {}", loggedUser, identifier);
	User user = userService.getUserByUsernameOrEmail(loggedUser);
	
	log.info("[TASK][GET][{}] Querying tasks with identifier: {}", loggedUser, identifier);
	Task task = taskRepository.findByIdentifier(identifier)
		.orElseThrow(() -> { 
		    log.info("[TASK][GET][{}] No task found with identifier: {}", loggedUser, identifier);
		    throw new TaskNotFoundException(identifier);});

	if(!task.getAssignedTo().getIdentifier().equals(user.getIdentifier())) {
	    log.warn("[TASK][GET][{}] User is not authorized to get task with identifier {}", loggedUser, identifier);
	    throw new ForbiddenException("Get task with identifier: " + identifier);
	}
	
	log.info("[TASK][GET][{}] Successfully retrieved task with identifier: {}", loggedUser, identifier);
	
	return responseAdapter.toSuccess(taskAdapter.toDTO(task), MessageKeyEnum.TASK_RETRIEVED, HttpStatus.OK);
    }

    @Override
    public ApiResponse<TaskDTO> updateTaskStatus(String loggedUser, String identifier, TaskUpdateStatusDTO taskDTO) {

	log.info("[TASK][UPDATE][STATUS][{}] Starting update status process for task with identifier: {}", loggedUser,
		identifier);

	log.info("[TASK][UPDATE][STATUS][{}] Retrieving user for update task with identifier: {}", loggedUser,
		identifier);
	User user = userService.getUserByUsernameOrEmail(loggedUser);

	Task task = taskRepository.findByIdentifier(identifier)
		.orElseThrow(() -> {
		    log.error("[TASK][UPDATE][STATUS][{}] Task with identifier {} not found", loggedUser, identifier);
		    return new TaskNotFoundException(identifier);
		});

	if (taskDTO.status() == null) {
	    throw new TaskMissingCreateParameters("[status]");
	}
	
	log.info("[TASK][UPDATE][STATUS][{}] Task found with identifier: {}", loggedUser, task.getIdentifier());

	if (!task.getAssignedTo().getIdentifier().equals(user.getIdentifier())) {
	    log.warn("[TASK][UPDATE][STATUS][{}] User is not authorized to update task with identifier {}", loggedUser, identifier);
	    throw new ForbiddenException("Update task with identifier: " + identifier);
	}
	
	log.info("[TASK][UPDATE][STATUS][{}] Updating task with new status: status={}", loggedUser, task.getStatus());
	task.setStatus(taskDTO.status());
	taskRepository.save(task);
	log.info("[TASK][UPDATE][STATUS][{}] Task with identifier {} successfully updated.", loggedUser, task.getIdentifier());

	return responseAdapter.toSuccess(taskAdapter.toDTO(task), MessageKeyEnum.TASK_STATUS_UPDATED, HttpStatus.OK);
	
    }

}
