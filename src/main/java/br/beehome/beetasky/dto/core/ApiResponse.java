package br.beehome.beetasky.dto.core;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiResponse<T> {
	
	private String apiVersion;
	private T content;
	private String message;
	private String errorCode;
	private LocalDateTime timestamp;
	private boolean success;
	
	@JsonIgnore
	private HttpStatus status;
	
	public ApiResponse(String apiVersion, String message, T content, HttpStatus status) {
		this.success = true;
        this.apiVersion = apiVersion;
        this.message = message;
        this.content = content;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
	
	public ApiResponse(String apiVersion, String message, HttpStatus status) {
		this.success = false;
        this.apiVersion = apiVersion;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
	
}
