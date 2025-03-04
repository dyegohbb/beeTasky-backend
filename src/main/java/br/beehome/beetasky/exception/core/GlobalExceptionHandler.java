package br.beehome.beetasky.exception.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.beehome.beetasky.adapter.ApiResponseAdapter;
import br.beehome.beetasky.dto.core.ApiResponse;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponseAdapter apiResponseAdapter;
    
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException customException) {
	
	ApiResponse<Void> error = apiResponseAdapter.toError(customException.getMessageKeyEnum(), customException.getStatus());
        return new ResponseEntity<>(error, error.getStatus());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception exception) {
	ApiResponse<Void> error = apiResponseAdapter.toError(ExceptionMessageKeyEnum.GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, error.getStatus());
    }
    
}
