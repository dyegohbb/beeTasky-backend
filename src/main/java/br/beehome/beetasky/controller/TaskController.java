package br.beehome.beetasky.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.beehome.beetasky.dto.TaskDTO;
import br.beehome.beetasky.dto.TaskFilterDTO;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskDTO>>> findAll(@ModelAttribute TaskFilterDTO filter, Pageable pageable) {
	ApiResponse<List<TaskDTO>> tasks = taskService.listAllTasks(filter, pageable);

	return new ResponseEntity<>(tasks, tasks.getStatus());
    }
}
