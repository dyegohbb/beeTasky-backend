package br.beehome.beetasky.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.beehome.beetasky.common.MessageKeyEnum;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.exception.core.ExceptionMessageKeyEnum;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiResponseAdapter {

    @Value("${api.version:1.0}")
    private String apiVersion;
    
    private final MessageSource messageSource;

    public <T> ApiResponse<T> toSuccess(T content, MessageKeyEnum messageKey, HttpStatus status) {

	String message = messageSource.getMessage(messageKey.getMsgCode(), null, LocaleContextHolder.getLocale());
	return new ApiResponse<>(apiVersion, message, content, status);
    }

    public <T> ApiResponse<T> toError(ExceptionMessageKeyEnum messageKey, Object[] args) {
	String message = messageSource.getMessage(messageKey.getMsgCode(), args, LocaleContextHolder.getLocale());
	return new ApiResponse<>(apiVersion, message);
    }

}
