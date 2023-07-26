package com.api.note.unit.services.auth;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.note.repositories.UserRepository;
import com.api.note.services.auth.AuthService;
import com.api.note.services.auth.JwtService;
import com.api.note.services.util.BadRequestException;

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
        String userIdMock = UUID.randomUUID().toString();
        BDDMockito.when(this.jwtService.validateJwt(ArgumentMatchers.any())).thenReturn(userIdMock);

        String jwt = "jwt";

        String userId = this.authService.authenticate(jwt);

        Assertions.assertThat(userId).isEqualTo(userIdMock);
    }
}
