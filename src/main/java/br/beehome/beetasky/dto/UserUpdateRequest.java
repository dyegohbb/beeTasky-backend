package br.beehome.beetasky.dto;

public record UserUpdateRequest(
	String username, 
	String email, 
	String password,
	String currentPassword) {
}
