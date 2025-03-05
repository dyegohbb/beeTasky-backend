package br.beehome.beetasky.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.beehome.beetasky.dto.AuthRequest;
import br.beehome.beetasky.dto.TokenDTO;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.exception.UnauthorizedLoginException;
import br.beehome.beetasky.exception.UserMissingAuthParameters;
import br.beehome.beetasky.security.JwtUtil;
import br.beehome.beetasky.service.AuthService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public ApiResponse<TokenDTO> authenticate(AuthRequest authRequest) {
	String usernameOrEmail = authRequest.login();
	validateAuthRequest(authRequest);
	log.info("[AUTH][{}] Auth request validated", usernameOrEmail);
	log.info("[AUTH][{}] Authentication started", usernameOrEmail);
	
	try {
	    Authentication authentication = authenticationManager
		    .authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail, authRequest.password()));
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	    log.info("[AUTH][{}] Authentication successful", usernameOrEmail);
	    log.info("[AUTH][{}] Generating token", usernameOrEmail);

	    ApiResponse<TokenDTO> token = jwtUtil.generateToken(userDetails.getUsername());

	    log.info("[AUTH][{}] Token successfully generated", usernameOrEmail);
	    return token;
	} catch (Exception e) {
	    log.warn("[AUTH][{}] Authentication failed", usernameOrEmail, e);
	    throw new UnauthorizedLoginException();
	}
    }

    private void validateAuthRequest(AuthRequest authRequest) {
	String usernameOrEmail = authRequest.login();
	boolean isUsernameOrEmailBlank = StringUtils.isBlank(usernameOrEmail);
	if (isUsernameOrEmailBlank || StringUtils.isBlank(authRequest.password())) {
	    log.warn("[AUTH][{}] Missing user auth parameters",
		    isUsernameOrEmailBlank ? "UNKNOWN" : usernameOrEmail);
	    throw new UserMissingAuthParameters();
	}
    }
}
