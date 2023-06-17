package com.api.note.domains.note;

import java.util.Set;

import com.api.note.domains.note.exceptions.InvalidNoteDomainException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;

public class NoteDomain {
    @NotBlank
    private String title;
    
    @NotBlank
    private String content;

    private NoteDomain(@NotBlank String title, @NotBlank String content) {
        this.title = title;
        this.content = content;
    }

    public static NoteDomain validate(String title, String content) throws InvalidNoteDomainException {
        try {
            NoteDomain noteDomain = new NoteDomain(title, content);

            validation(noteDomain);
            
            return noteDomain;
        } catch (ConstraintViolationException exception) {
            String message = exception.getMessage();

            throw new InvalidNoteDomainException(message);
        }
    }

    private static void validation(NoteDomain noteDomain) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        final Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<NoteDomain>> constraintViolations = validator.validate(noteDomain);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
