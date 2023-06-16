package com.api.note.integration.services.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.api.note.domains.user.exceptions.InvalidUserDomainException;
import com.api.note.dtos.user.UserDTOSaveResponse;
import com.api.note.repositories.UserRepository;
import com.api.note.services.user.UserService;
import com.api.note.services.util.BadRequestException;
import com.api.note.utils.builders.user.UserDTOSaveRequestBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void esperoQueSaveUmNovoUsuarioNoSistema() throws InvalidUserDomainException, BadRequestException {
        UserDTOSaveResponse userDTOSaveResponse = this.userService.save(UserDTOSaveRequestBuilder.createWithValidData());
    
        Assertions.assertThat(userDTOSaveResponse.name).isEqualTo("Foo Bar");

        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmErroDeNomeInvalidoDadoUmValorVazio() {
        Assertions.assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> this.userService.save(UserDTOSaveRequestBuilder.createWithEmptyName()))
            .withMessage("name: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmErroDeEmailInvalidoDadoUmValorVazio() {
        Assertions.assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> this.userService.save(UserDTOSaveRequestBuilder.createWithEmptyEmail()))
            .withMessage("email: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmErroDeEmailInvalidoDadoUmComFormatoInvalido() {
        Assertions.assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> this.userService.save(UserDTOSaveRequestBuilder.createWithInvalidEmailFormat()))
            .withMessage("email: must be a well-formed email address");
    }

    @Test
    public void esperoQueRetorneUmErroDeSenhaInvalidaDadoUmValorVazio() {
        Assertions.assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> this.userService.save(UserDTOSaveRequestBuilder.createWithEmptyPassword()))
            .withMessage("password: must not be blank");
    }

    @Test
    public void esperoQueRetoneUmErroDeEmailJaCadastradoNoSistema() {
        this.userRepository.save(UserModelBuilder.createWithValidData());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.userService.save(UserDTOSaveRequestBuilder.createWithValidData()))
            .withMessage("Error: Este e-mail já está cadastrado no sistema");

        this.userRepository.deleteAll();
    }
}
