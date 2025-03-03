package br.beehome.beetasky.exception.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.beehome.beetasky.adapter.ApiResponseAdapter;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.exception.ExceptionMessageKeyEnum;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiResponseAdapter apiResponseAdapter;
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
	ApiResponse<Void> error = apiResponseAdapter.toError(ExceptionMessageKeyEnum.GENERIC_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(error, error.getStatus());
    }
    
}
