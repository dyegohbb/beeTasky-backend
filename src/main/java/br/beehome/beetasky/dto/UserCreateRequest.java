package br.beehome.beetasky.dto;

public record UserCreateRequest(
	String username, 
	String email, 
	String password) {
}