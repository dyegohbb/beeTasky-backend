package br.beehome.beetasky.service;

import br.beehome.beetasky.dto.UserCreateRequest;
import br.beehome.beetasky.dto.UserDTO;
import br.beehome.beetasky.dto.UserUpdateRequest;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.entity.User;

public interface UserService {
    
    public ApiResponse<UserDTO> createUser(UserCreateRequest userRequest);

    public ApiResponse<UserDTO> updateUser(String username, UserUpdateRequest userRequest);

    public ApiResponse<Void> deleteUserByIdentifier(String username, String identifier);

    public User getUserByUsernameOrEmail(String username);
    
}
