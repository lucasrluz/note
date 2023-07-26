package com.api.note.unit.services.auth;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.api.note.services.auth.JwtService;
import com.api.note.services.auth.util.JwtKeys;
import com.api.note.services.util.BadRequestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@ExtendWith(SpringExtension.class)
public class JwtServiceTests {
    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtKeys jwtKeys;

    private String secretKey = "0d577f68867f4aa39c98c69e89fea06e4f2558703f754ab588ecfba34b0ff1dbddc651c8a50b4c5499675850d5193615";

    @Test
    public void esperoQueCrieUmJwtValido() throws BadRequestException {
        BDDMockito.when(this.jwtKeys.getJwtExpirationMs()).thenReturn(5000);
        
        BDDMockito.when(this.jwtKeys.getSecretKey()).thenReturn(
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey))
        );

        String userId = UUID.randomUUID().toString();

        String jwt = this.jwtService.generateJwt(userId);

        Assertions.assertThat(this.jwtService.validateJwt(jwt)).isEqualTo(userId);
    }

    @Test
    public void esperoQueRetorneUmErroDeJwtExpirado() {
        BDDMockito.when(this.jwtKeys.getJwtExpirationMs()).thenReturn(1);
        
        BDDMockito.when(this.jwtKeys.getSecretKey()).thenReturn(
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secretKey))
        );

        String userId = UUID.randomUUID().toString();

        String jwt = this.jwtService.generateJwt(userId);

        Assertions.assertThatExceptionOfType(ExpiredJwtException.class)
            .isThrownBy(() -> this.jwtService.validateJwt(jwt));
    }
}
