package br.beehome.beetasky.dto;

public record AuthRequest(
	String login, 
	String password) {
}
