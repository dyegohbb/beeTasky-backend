package br.beehome.beetasky.dto;

public record AuthRequest(
	String username, 
	String email, 
	String password) {
    
    public String getUsernameOrEmail() {
	return username != null ? username : email;
    }
}
