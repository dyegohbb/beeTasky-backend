package br.beehome.beetasky.adapter;

import org.springframework.stereotype.Component;

import br.beehome.beetasky.dto.UserDTO;
import br.beehome.beetasky.entity.User;

@Component
public class UserAdapter {
    
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .identifier(user.getIdentifier())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        
	return User.builder()
		.identifier(userDTO.getIdentifier())
		.username(userDTO.getUsername())
		.email(userDTO.getEmail())
		.build();
    }

}
