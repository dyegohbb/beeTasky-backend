package br.beehome.beetasky.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import br.beehome.beetasky.dto.TaskDTO;
import br.beehome.beetasky.dto.TaskFilterDTO;
import br.beehome.beetasky.dto.TaskUpdateStatusDTO;
import br.beehome.beetasky.dto.core.ApiResponse;

public interface TaskService {

    public ApiResponse<List<TaskDTO>> listAllTasksByUser(String loggedUser, TaskFilterDTO userFilter, Pageable pageable);

    public ApiResponse<TaskDTO> createTask(String loggedUser, TaskDTO taskDTO);

    public ApiResponse<TaskDTO> updateTask(String loggedUser, String taskIdentifier, TaskDTO taskDTO);

    public ApiResponse<Void> deleteTaskByIdentifier(String loggedUser, String identifier);

    public ApiResponse<TaskDTO> getTaskByIdentifier(String name, String identifier);

    public ApiResponse<TaskDTO> updateTaskStatus(String name, String identifier, TaskUpdateStatusDTO taskDTO);

}
