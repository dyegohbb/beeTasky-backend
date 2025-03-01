package br.beehome.beetasky.service.impl;

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
import br.beehome.beetasky.repository.jpa.TaskRepository;
import br.beehome.beetasky.repository.specifications.TaskSpecification;
import br.beehome.beetasky.service.TaskService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskSpecification taskSpecification;
    private final TaskRepository taskRepository;
    private final TaskAdapter taskAdapter;
    private final ApiResponseAdapter responseAdapter;

    @Override
    public ApiResponse<List<TaskDTO>> listAllTasks(TaskFilterDTO userFilter, Pageable pageable) {
	final Specification<Task> specificationFilter = taskSpecification.filter(userFilter);

	List<TaskDTO> tasks = taskRepository.findAll(specificationFilter, pageable).stream().map(taskAdapter::toDTO)
		.toList();

	return responseAdapter.toSucccessApiResponse(tasks, MessageKeyEnum.SUCCESS,
		tasks.size() > 1 ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

}
