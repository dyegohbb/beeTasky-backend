package br.beehome.beetasky.service;

import br.beehome.beetasky.dto.AuthRequest;
import br.beehome.beetasky.dto.TokenDTO;
import br.beehome.beetasky.dto.core.ApiResponse;

public interface AuthService {
    
    public ApiResponse<TokenDTO> authenticate(AuthRequest authRequest);

}
