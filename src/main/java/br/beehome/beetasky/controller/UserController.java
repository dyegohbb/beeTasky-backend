package br.beehome.beetasky.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.beehome.beetasky.dto.UserCreateRequest;
import br.beehome.beetasky.dto.UserDTO;
import br.beehome.beetasky.dto.UserUpdateRequest;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserCreateRequest userRequest) {

	ApiResponse<UserDTO> user = userService.createUser(userRequest);
	return new ResponseEntity<>(user, user.getStatus());
    }

    @PutMapping("/{identifier}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable String identifier,
	    @RequestBody UserUpdateRequest userRequest, Authentication authentication) {

	ApiResponse<UserDTO> response = userService.updateUser(authentication.getName(), userRequest);

	return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String identifier,
	    Authentication authentication) {

	ApiResponse<Void> response = userService.deleteUserByIdentifier(authentication.getName(), identifier);

	return new ResponseEntity<>(response, response.getStatus());
    }
}
