package br.beehome.beetasky.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AuthRequest(
	String username, 
	String email, 
	String password) {
    
    @JsonIgnore
    public String getUsernameOrEmail() {
	return username != null ? username : email;
    }
}
