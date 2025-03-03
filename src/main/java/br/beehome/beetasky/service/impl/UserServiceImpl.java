package br.beehome.beetasky.service.impl;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.beehome.beetasky.adapter.ApiResponseAdapter;
import br.beehome.beetasky.adapter.UserAdapter;
import br.beehome.beetasky.common.MessageKeyEnum;
import br.beehome.beetasky.dto.UserCreateRequest;
import br.beehome.beetasky.dto.UserDTO;
import br.beehome.beetasky.dto.UserUpdateRequest;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.entity.User;
import br.beehome.beetasky.exception.ExceptionMessageKeyEnum;
import br.beehome.beetasky.exception.UserNotFoundException;
import br.beehome.beetasky.repository.jpa.UserRepository;
import br.beehome.beetasky.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final ApiResponseAdapter apiResponseAdapter;
    private final UserAdapter userAdapter;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public ApiResponse<UserDTO> createUser(UserCreateRequest userRequest) {
	User user = userRepository.findByUsernameOrEmail(userRequest.username()).map(existingUser -> {
	    existingUser.activeUser();
	    return existingUser;
	}).orElseGet(() -> userAdapter.toEntity(userRequest));

	userRepository.save(user);
	return apiResponseAdapter.toSuccess(userAdapter.toDTO(user), MessageKeyEnum.SUCCESS, HttpStatus.CREATED);
    }

    @Override
    public ApiResponse<UserDTO> updateUser(String loggedUsername, UserUpdateRequest userRequest) {
        User user = getUserByUsernameOrEmail(loggedUsername);

        if (userRequest.username() != null && !userRequest.username().isBlank()) {
            user.setUsername(userRequest.username());
        }
        
        if (userRequest.email() != null && !userRequest.email().isBlank()) {
            user.setEmail(userRequest.email());
        }

        if (userRequest.password() != null && !userRequest.password().isBlank()) {
            if (!passwordEncoder.matches(userRequest.currentPassword(), user.getPassword())) {
                return apiResponseAdapter.toError(ExceptionMessageKeyEnum.GENERIC_ERROR, HttpStatus.BAD_REQUEST);
            }

            user.setPassword(passwordEncoder.encode(userRequest.password()));
        }
        
        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);
        return apiResponseAdapter.toSuccess(userAdapter.toDTO(user), MessageKeyEnum.SUCCESS, HttpStatus.OK);
    }

    @Override
    public ApiResponse<Void> deleteUserByIdentifier(String loggedUsername, String identifier) {
        User loggedUser = getUserByUsernameOrEmail(loggedUsername);

        if (!loggedUser.getIdentifier().equals(identifier)) {
            return apiResponseAdapter.toError(ExceptionMessageKeyEnum.FORBIDDEN, HttpStatus.FORBIDDEN);
        }

        loggedUser.logicDelete();
        userRepository.save(loggedUser);

        return apiResponseAdapter.toSuccess(null, MessageKeyEnum.SUCCESS, HttpStatus.OK);
    }

    @Override
    public User getUserByUsernameOrEmail(String username) {
	return userRepository.findByUsernameOrEmail(username).orElseThrow(() -> new UserNotFoundException(username));
    }
    

}
