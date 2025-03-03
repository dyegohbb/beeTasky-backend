package br.beehome.beetasky.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.beehome.beetasky.adapter.ApiResponseAdapter;
import br.beehome.beetasky.dto.AuthRequest;
import br.beehome.beetasky.dto.TokenDTO;
import br.beehome.beetasky.dto.core.ApiResponse;
import br.beehome.beetasky.exception.ExceptionMessageKeyEnum;
import br.beehome.beetasky.security.JwtUtil;
import br.beehome.beetasky.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final ApiResponseAdapter apiResponseAdapter;
    private final JwtUtil jwtUtil;
    
    @Override
    public ApiResponse<TokenDTO> authenticate(AuthRequest authRequest) {

	try {
	    Authentication authentication = authenticationManager.authenticate(
		    new UsernamePasswordAuthenticationToken(authRequest.getUsernameOrEmail(), authRequest.password()));

	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    return jwtUtil.generateToken(userDetails.getUsername());
	} catch (Exception e) {
	    return apiResponseAdapter.toError(ExceptionMessageKeyEnum.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
	}
    }

}
