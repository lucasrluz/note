package com.api.note.services.auth;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.api.note.dtos.auth.AuthenticateDTORequest;
import com.api.note.dtos.auth.LoginDTORequest;
import com.api.note.dtos.auth.LoginDTOResponse;
import com.api.note.models.UserModel;
import com.api.note.repositories.UserRepository;
import com.api.note.services.util.BadRequestException;
import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;

@Service
public class AuthService {
    private UserRepository userRepository;
    private JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public LoginDTOResponse login(@RequestBody LoginDTORequest loginDTORequest) throws BadRequestException {
        Optional<UserModel> findUserModelByEmail = this.userRepository.findByEmail(loginDTORequest.email);

        if (findUserModelByEmail.isEmpty()) {
            throw new BadRequestException("Error: E-mail ou senha inválido");
        }

        Result validatePasswordResponse = BCrypt.verifyer().verify(
            loginDTORequest.password.toCharArray(),
            findUserModelByEmail.get().password
        );
    
        if (!validatePasswordResponse.verified) {
            throw new BadRequestException("Error: E-mail ou senha inválido");
        }

        String jwt = jwtService.generateJwt(loginDTORequest.email);

        return new LoginDTOResponse(jwt);
    }

    public boolean authenticate(AuthenticateDTORequest authenticateDTORequest) throws BadRequestException {
        Optional<UserModel> findUserByUserIdResponse = this.userRepository.findById(
            UUID.fromString(authenticateDTORequest.userId)
        );
    
        if (findUserByUserIdResponse.isEmpty()) {
            throw new BadRequestException("Error: usuário não encontrado");
        }

        return this.jwtService.validateJwt(authenticateDTORequest.jwt, findUserByUserIdResponse.get().email);
    }
}
