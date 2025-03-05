package br.beehome.beetasky.adapter;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.beehome.beetasky.dto.UserDTO;
import br.beehome.beetasky.dto.UserCreateRequest;
import br.beehome.beetasky.entity.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAdapter {
    
    private final PasswordEncoder passwordEncoder;
    
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

    public User toEntity(UserCreateRequest userRequest) {
        if (userRequest == null) {
            return null;
        }
        
	return User.builder()
		.identifier(UUID.randomUUID().toString())
		.username(userRequest.username())
		.email(userRequest.email())
		.password(encrypt(userRequest.password()))
		.build();
    }
    
    private String encrypt(String password) {
	return passwordEncoder.encode(password);
    }

}
