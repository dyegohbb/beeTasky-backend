package br.beehome.beetasky.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.beehome.beetasky.common.MessageKeyEnum;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;

@Component
public class ApiResponseAdapter {

    @Value("${api.version:1.0}")
    private String apiVersion;

    public <T> ApiResponse<T> toSuccess(T content, MessageKeyEnum messageKey, HttpStatus status) {
	return new ApiResponse<>(apiVersion, messageKey.getMsg(), content, status);
    }

    public <T> ApiResponse<T> toError(ExceptionMessageKeyEnum messageKey, HttpStatus status) {
	return new ApiResponse<>(apiVersion, messageKey.getMsg(), status);
    }

}
