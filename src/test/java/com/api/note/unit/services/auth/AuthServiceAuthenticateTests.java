package com.api.note.unit.services.auth;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.note.dtos.auth.AuthenticateDTORequest;
import com.api.note.models.UserModel;
import com.api.note.repositories.UserRepository;
import com.api.note.services.auth.AuthService;
import com.api.note.services.auth.JwtService;
import com.api.note.services.util.BadRequestException;
import com.api.note.utils.builders.auth.AuthenticateDTORequestBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class AuthServiceAuthenticateTests {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Test
    public void esperoQueOJwtEoEmailInformadosSejamValidos() throws BadRequestException {
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        BDDMockito.when(this.jwtService.validateJwt(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);

        AuthenticateDTORequest authenticateDTORequest = AuthenticateDTORequestBuilder.createWithValidData();

        boolean authenticateResponse = this.authService.authenticate(authenticateDTORequest);

        Assertions.assertThat(authenticateResponse).isEqualTo(true);
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontrado() throws BadRequestException {
        Optional<UserModel> userModelOptionalMock = Optional.empty();
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        BDDMockito.when(this.jwtService.validateJwt(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);

        AuthenticateDTORequest authenticateDTORequest = AuthenticateDTORequestBuilder.createWithValidData();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.authService.authenticate(authenticateDTORequest))
            .withMessage("Error: usuário não encontrado");
    }
}
