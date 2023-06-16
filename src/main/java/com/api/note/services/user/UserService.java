package com.api.note.services.user;

import org.springframework.stereotype.Service;

import com.api.note.domains.user.UserDomain;
import com.api.note.domains.user.exceptions.InvalidUserDomainException;
import com.api.note.dtos.user.UserDTOSaveRequest;
import com.api.note.dtos.user.UserDTOSaveResponse;
import com.api.note.models.UserModel;
import com.api.note.repositories.UserRepository;
import com.api.note.services.util.BadRequestException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
