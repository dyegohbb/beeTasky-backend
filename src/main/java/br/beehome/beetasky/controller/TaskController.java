package br.beehome.beetasky.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.beehome.beetasky.dto.TaskDTO;
import br.beehome.beetasky.dto.TaskFilterDTO;
import br.beehome.beetasky.dto.TaskUpdateStatusDTO;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskDTO>>> listAll(@ParameterObject @ModelAttribute TaskFilterDTO filter, @ParameterObject Pageable pageable, Authentication authentication) {
	ApiResponse<List<TaskDTO>> tasks = taskService.listAllTasksByUser(authentication.getName(), filter, pageable);
	return new ResponseEntity<>(tasks, tasks.getStatus());
    }
    
    @GetMapping("/{identifier}")
    public ResponseEntity<ApiResponse<TaskDTO>> getTask(@PathVariable String identifier, Authentication authentication) {
	ApiResponse<TaskDTO> task = taskService.getTaskByIdentifier(authentication.getName(), identifier);
	return new ResponseEntity<>(task, task.getStatus());
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@RequestBody TaskDTO taskDTO, Authentication authentication) {
        
	ApiResponse<TaskDTO> task = taskService.createTask(authentication.getName(), taskDTO);
	return new ResponseEntity<>(task, task.getStatus());
    }
    
    @PutMapping("/{identifier}")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTask(@PathVariable String identifier, @RequestBody TaskDTO taskDTO,
	    Authentication authentication) {

	ApiResponse<TaskDTO> response = taskService.updateTask(authentication.getName(), identifier, taskDTO);

	return new ResponseEntity<>(response, response.getStatus());
    }
    
    @DeleteMapping("/{identifier}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @PathVariable String identifier,
            Authentication authentication) {

        ApiResponse<Void> response = taskService.deleteTaskByIdentifier(authentication.getName(), identifier);

        return new ResponseEntity<>(response, response.getStatus());
    }
}
