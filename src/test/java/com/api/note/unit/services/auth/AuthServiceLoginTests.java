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

import com.api.note.dtos.auth.LoginDTORequest;
import com.api.note.dtos.auth.LoginDTOResponse;
import com.api.note.models.UserModel;
import com.api.note.repositories.UserRepository;
import com.api.note.services.auth.JwtService;
import com.api.note.services.auth.AuthService;
import com.api.note.services.util.BadRequestException;
import com.api.note.utils.builders.auth.LoginDTORequestBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class AuthServiceLoginTests {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Test
    public void esperoQueRetorneUmJwt() throws BadRequestException {
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        BDDMockito.when(this.jwtService.generateJwt(ArgumentMatchers.any())).thenReturn("jwt");

        LoginDTORequest loginDTORequest = LoginDTORequestBuilder.createWithValidData();

        LoginDTOResponse loginDTOResponse = this.authService.login(loginDTORequest);

        Assertions.assertThat(loginDTOResponse.jwt).isEqualTo("jwt");
    }

    @Test
    public void esperoQueRetorneUmErroDeEmailOuSenhaInvalidoDadoUmEmailInvalido() throws BadRequestException {
        Optional<UserModel> userModelOptionalMock = Optional.empty();
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        LoginDTORequest loginDTORequest = LoginDTORequestBuilder.createWithValidData();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.authService.login(loginDTORequest))
            .withMessage("Error: E-mail ou senha inválido");
    }

    @Test
    public void esperoQueRetorneUmErroDeEmailOuSenhaInvalidoDadoUmaSenhaInvalida() throws BadRequestException {
        UserModel userModelMock = UserModelBuilder.createWithValidData();
        Optional<UserModel> userModelOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(userModelOptionalMock);

        LoginDTORequest loginDTORequest = LoginDTORequestBuilder.createWithValidData();
        loginDTORequest.password = "456";

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.authService.login(loginDTORequest))
            .withMessage("Error: E-mail ou senha inválido");
    }
}
