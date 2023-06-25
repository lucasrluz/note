package com.api.note.unit.services.user;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.note.domains.user.exceptions.InvalidUserDomainException;
import com.api.note.dtos.user.UserDTOSaveRequest;
import com.api.note.dtos.user.UserDTOSaveResponse;
import com.api.note.models.UserModel;
import com.api.note.repositories.UserRepository;
import com.api.note.services.user.UserService;
import com.api.note.services.util.BadRequestException;
import com.api.note.utils.builders.user.UserDTOSaveRequestBuilder;
import com.api.note.utils.builders.user.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class UserServiceSaveTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void esperoQueSalveUmNovoUsuarioNoSistema() throws InvalidUserDomainException, BadRequestException {
        // Mocks
        BDDMockito.when(this.userRepository.existsByEmail(ArgumentMatchers.any())).thenReturn(false);

        UserModel userModelMock = UserModelBuilder.createWithValidData();
        userModelMock.userId = UUID.randomUUID();
        BDDMockito.when(this.userRepository.save(ArgumentMatchers.any())).thenReturn(userModelMock);

        // Teste principal
        UserDTOSaveRequest userDTOSaveRequest = UserDTOSaveRequestBuilder.createWithValidData();

        UserDTOSaveResponse userDTOSaveResponse = this.userService.save(userDTOSaveRequest);
        
        Assertions.assertThat(userDTOSaveResponse.userId).isEqualTo(userModelMock.userId.toString());
        Assertions.assertThat(userDTOSaveResponse.name).isEqualTo(userModelMock.name);
        Assertions.assertThat(userDTOSaveResponse.email).isEqualTo(userModelMock.email);
    }

    @Test
    public void esperoQueRetorneUmErroDeEmailJaCadastradoNoSistema() throws InvalidUserDomainException, BadRequestException {
        // Mocks
        BDDMockito.when(this.userRepository.existsByEmail(ArgumentMatchers.any())).thenReturn(true);

        // Teste principal
        UserDTOSaveRequest userDTOSaveRequest = UserDTOSaveRequestBuilder.createWithValidData();
        
        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.userService.save(userDTOSaveRequest))
            .withMessage("Error: Este e-mail já está cadastrado no sistema");
    }
}
