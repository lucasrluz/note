package com.api.note.unit.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.api.note.domains.user.UserDomain;
import com.api.note.domains.user.exceptions.InvalidUserDomainException;

public class UserDomainTests {
    @Test
    public void esperoQueRetorneUmUserDomain() throws InvalidUserDomainException {
        UserDomain userDomain = UserDomain.validate(
            "Foo Bar",
            "foobar@gmail.com",
            "123"
        );

        Assertions.assertThat(userDomain.getName()).isEqualTo("Foo Bar");
        Assertions.assertThat(userDomain.getEmail()).isEqualTo("foobar@gmail.com");
        Assertions.assertThat(userDomain.getPassword()).isEqualTo("123");
    }

    @Test
    public void esperoQueRetorneUmErroDeNomeInvalidoDadoUmValorVazio() {
        Assertions.assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> UserDomain.validate("", "foobar@gmail.com", "123"))
            .withMessage("name: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmErroDeEmailInvalidoDadoUmValorVazio() {
        Assertions.assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> UserDomain.validate("Foo Bar", "", "123"))
            .withMessage("email: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmErroDeEmailInvalidoDadoUmEmailComFormatoInvalido() {
        Assertions.assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> UserDomain.validate("Foo Bar", "@gmail.com", "123"))
            .withMessage("email: must be a well-formed email address");
    }
}
