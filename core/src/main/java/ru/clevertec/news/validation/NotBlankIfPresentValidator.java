package ru.clevertec.news.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankIfPresentValidator implements ConstraintValidator<NotBlankIfPresent, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null) {
            return true;
        }

        return !s.isBlank();
    }
}
