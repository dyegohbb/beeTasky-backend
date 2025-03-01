package br.beehome.beetasky.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import br.beehome.beetasky.dto.TaskDTO;
import br.beehome.beetasky.dto.TaskFilterDTO;
import br.beehome.beetasky.dto.core.ApiResponse;

public interface TaskService {

	public ApiResponse<List<TaskDTO>> listAllTasks(TaskFilterDTO userFilter, Pageable pageable);
	
}
