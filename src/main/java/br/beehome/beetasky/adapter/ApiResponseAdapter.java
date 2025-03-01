package br.beehome.beetasky.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.beehome.beetasky.common.MessageKeyEnum;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

@Component
public class ApiResponseAdapter {
	
	public <T> ApiResponse<T> toSucccessApiResponse(T content, MessageKeyEnum messageKey, HttpStatus status) {
        return new ApiResponse<>("1", "success", content, status);
    }
	
	public <T> ApiResponse<T> toErrorApiResponse(ExceptionMessageKeyEnum messageKey, HttpStatus status) {
        return new ApiResponse<>("1", "sucess", status);
    }

}
