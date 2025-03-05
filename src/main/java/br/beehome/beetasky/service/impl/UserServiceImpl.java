package br.beehome.beetasky.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import br.beehome.beetasky.exception.DuplicatedUserException;
import br.beehome.beetasky.exception.ForbiddenException;
import br.beehome.beetasky.exception.UserMissingCreateParameters;
import br.beehome.beetasky.exception.UserNotFoundException;
import br.beehome.beetasky.exception.UserNullException;
import br.beehome.beetasky.repository.jpa.UserRepository;
import br.beehome.beetasky.service.UserService;
import io.micrometer.common.util.StringUtils;
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

	validateRequiredFieldsToCreate(userRequest);
	
	String username = userRequest.username();
	String email = userRequest.email();
	
	log.info("[USER][CREATE] Starting User creation for user with details: username={}, email={}", username, email);
	
	if (userRepository.existsByUsernameOrEmail(username, email)) {
	    log.warn("[USER][CREATE] User creation for user with details: username={}, email={} - Duplicated username or email", username, email);
	    throw new DuplicatedUserException();
	}
	
	log.info("[USER][CREATE] User with details: username={}, email={} is unique, proceeding with creation", username, email);
	User user = userAdapter.toEntity(userRequest);
        userRepository.save(user);

        log.info("[USER][CREATE][{}] User with details: username={}, email={} successfully created", user.getUsername(), username, email);
        return apiResponseAdapter.toSuccess(userAdapter.toDTO(user), MessageKeyEnum.USER_CREATED, HttpStatus.CREATED);
    }

    private void validateRequiredFieldsToCreate(UserCreateRequest userRequest) {
	if (userRequest == null) {
	    throw new UserNullException();
	}
	
	List<String> requiredFieldsMissing = new ArrayList<>();

	String username = userRequest.username();
	if (StringUtils.isBlank(username)) {
	    requiredFieldsMissing.add("username");
	}

	if (StringUtils.isBlank(userRequest.email())) {
	    requiredFieldsMissing.add("email");
	}

	if (StringUtils.isBlank(userRequest.password())) {
	    requiredFieldsMissing.add("password");
	}
	
	if(!requiredFieldsMissing.isEmpty()) {
	    log.info("[USER][CREATE] User creation failed - Missing required parameters: {}", requiredFieldsMissing);
	    throw new UserMissingCreateParameters(requiredFieldsMissing.toString());
	}	
    }

    @Override
    public ApiResponse<UserDTO> updateUser(String loggedUsername, UserUpdateRequest userRequest, String identifier) {
        log.info("[USER][UPDATE][{}] Starting update process for user with identifier: {}", loggedUsername, identifier);

        User loggedUser = userRepository.findByUsernameOrEmail(loggedUsername).orElseThrow(() -> {
            log.error("[USER][UPDATE][{}] User with identifier {} not found", loggedUsername,
        	    identifier);
            throw new UserNotFoundException(identifier);
        });
        
        log.info("[USER][UPDATE][{}] Retrieving logged user to update user", loggedUsername);
        User user = userRepository.findByIdentifier(identifier).orElseThrow(() -> {
            log.error("[USER][UPDATE][{}] User with identifier {} not found", loggedUsername,
        	    identifier);
            throw new UserNotFoundException(identifier);
        });
        
        if (!loggedUser.getIdentifier().equals(identifier)) {
            log.warn("[USER][UPDATE][{}] User is not authorized to update user with identifier {}.", loggedUsername, identifier);
            throw new ForbiddenException("Update user with identifier: " + identifier);
        }
        
	if (userRepository.existsByUsernameOrEmailAndIdentifierNot(userRequest.username(), userRequest.email(),
		identifier)) {
	    log.warn(
		    "[USER][UPDATE] User update for user with details: username={}, email={} - Duplicated username or email",
		    user.getUsername(), user.getEmail());
	    throw new DuplicatedUserException();
	}
        
        updateUserFields(loggedUsername, user, userRequest);
        
        log.info("[USER][UPDATE][{}] Updating user information with new values.", loggedUsername);
        userRepository.save(user);
        log.info("[USER][UPDATE][{}] User update successful.", loggedUsername);
        
        return apiResponseAdapter.toSuccess(userAdapter.toDTO(user), MessageKeyEnum.USER_UPDATED, HttpStatus.OK);
    }
    
    private void updateUserFields(String loggedUsername, User user, UserUpdateRequest userRequest) {
	if (userRequest.username() != null && !userRequest.username().isBlank()) {
            user.setUsername(userRequest.username());
        }
        
        if (userRequest.email() != null && !userRequest.email().isBlank()) {
            user.setEmail(userRequest.email());
        }
        
        if (userRequest.password() != null && !userRequest.password().isBlank()) {
            if (!passwordEncoder.matches(userRequest.currentPassword(), user.getPassword())) {
                log.warn("[USER][UPDATE][{}] User attempted to change password with incorrect current password.", loggedUsername);
                throw new ForbiddenException("Update user with identifier: " + user.getIdentifier());
            }

            user.setPassword(passwordEncoder.encode(userRequest.password()));
        }
        
        user.setUpdatedOn(LocalDateTime.now());

    }

    @Override
    public ApiResponse<Void> deleteUserByIdentifier(String loggedUsername, String identifier) {
        log.info("[USER][DELETE][{}] Starting delete process for user with identifier {}.", loggedUsername, identifier);

        log.info("[USER][DELETE][{}] Retrieving user for delete user with identifier {}.", loggedUsername, identifier);
        User loggedUser = getUserByUsernameOrEmail(loggedUsername);

        if (!loggedUser.getIdentifier().equals(identifier)) {
            log.warn("[USER][DELETE][{}] User is not authorized to delete user with identifier {}.", loggedUsername, identifier);
            throw new ForbiddenException("Delete user with identifier: " + identifier);
        }

        log.info("[USER][DELETE][{}] Deleting user with identifier {}.", loggedUsername, identifier);
        loggedUser.logicDelete();
        userRepository.save(loggedUser);

        log.info("[USER][DELETE][{}] User with identifier {} successfully logically deleted.", loggedUsername, identifier);

        return apiResponseAdapter.toSuccess(null, MessageKeyEnum.USER_DELETED, HttpStatus.OK);
    }

    @Override
    public User getUserByUsernameOrEmail(String username) {
        log.info("[USER][GET][{}] Getting logged user by username or email: {}", username, username);

        User user = userRepository.findByUsernameOrEmail(username)
            .orElseThrow(() -> {
                log.warn("[USER][GET][{}] User not found with username or email: {}", username, username);
                return new UserNotFoundException(username);
            });

        log.info("[USER][GET][{}] Successfully retrieved user with username or email: {}", username, username);

        return user;
    }


}
