package com.api.note.domains.user;

import java.util.Set;
import com.api.note.domains.user.exceptions.InvalidUserDomainException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDomain {
    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public UserDomain(@NotBlank String name, @NotBlank @Email String email, @NotBlank String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static UserDomain validate(String name, String email, String password) throws InvalidUserDomainException {
        try {
            UserDomain userDomain = new UserDomain(name, email, password);

            validation(userDomain);

            return userDomain;
        } catch (ConstraintViolationException exception) {
            String message = exception.getMessage();

            throw new InvalidUserDomainException(message);
        }
    }

    private static void validation(UserDomain userDomain) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        final Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<UserDomain>> constraintViolations = validator.validate(userDomain);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
