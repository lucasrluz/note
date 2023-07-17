package com.api.note.services.user;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.api.note.domains.user.UserDomain;
import com.api.note.domains.user.exceptions.InvalidUserDomainException;
import com.api.note.dtos.user.UserDTOSaveDemoResponse;
import com.api.note.dtos.user.UserDTOSaveRequest;
import com.api.note.dtos.user.UserDTOSaveResponse;
import com.api.note.models.EmailNumberModel;
import com.api.note.models.UserModel;
import com.api.note.repositories.EmailNumberRepository;
import com.api.note.repositories.UserRepository;
import com.api.note.services.util.BadRequestException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class UserService {
    private UserRepository userRepository;
    private EmailNumberRepository emailNumberRepository;

    public UserService(UserRepository userRepository, EmailNumberRepository emailNumberRepository) {
        this.userRepository = userRepository;
        this.emailNumberRepository = emailNumberRepository;
    }

    public UserDTOSaveResponse save(UserDTOSaveRequest userDTOSaveRequest) throws InvalidUserDomainException, BadRequestException {
        UserDomain userDomainValidation = UserDomain.validate(
            userDTOSaveRequest.name,
            userDTOSaveRequest.email,
            userDTOSaveRequest.password
        );

        boolean findUserModelByEmail = this.userRepository.existsByEmail(userDomainValidation.getEmail());

        if (findUserModelByEmail) {
            throw new BadRequestException("Error: Este e-mail já está cadastrado no sistema");
        }

        String hashPassword = BCrypt
            .withDefaults()
            .hashToString(12, userDomainValidation.getPassword().toCharArray());
        
        UserModel userModel = new UserModel(
            userDomainValidation.getName(),
            userDomainValidation.getEmail(),
            hashPassword
        );

        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        return new UserDTOSaveResponse(
            saveUserModelResponse.userId.toString(),
            saveUserModelResponse.name,
            saveUserModelResponse.email
        );
    }

    public UserDTOSaveDemoResponse saveDemo() {
        if (this.emailNumberRepository.findAll().isEmpty()) {
            EmailNumberModel emailNumberModel = new EmailNumberModel(0);

            this.emailNumberRepository.save(emailNumberModel);
        }

        EmailNumberModel emailNumberModel = this.emailNumberRepository.findAll().get(0);

        int emailNumber = emailNumberModel.number;

        EmailNumberModel emailNumberModelForUpdate = new EmailNumberModel(
            emailNumberModel.emailNumberId,
            emailNumberModel.number + 1
        );

        this.emailNumberRepository.save(emailNumberModelForUpdate);

        String email = "userdemo." + String.valueOf(emailNumber) + "@gmail.com";

        Random random = new Random();
        String password = String.valueOf(random.nextInt(100001));

        String hashPassword = BCrypt
            .withDefaults()
            .hashToString(12, password.toCharArray());

        UserModel userModel = new UserModel(
        "User Demo",
            email,
            hashPassword
        );

        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        return new UserDTOSaveDemoResponse(
            saveUserModelResponse.userId.toString(),
            saveUserModelResponse.name,
            saveUserModelResponse.email,
            password
        );
    }
}
