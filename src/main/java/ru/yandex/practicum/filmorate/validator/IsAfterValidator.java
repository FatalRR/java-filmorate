package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class IsAfterValidator implements ConstraintValidator<IsAfter, LocalDate> {

    public String value;

    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if (date.isBefore(LocalDate.parse(value, formatter))) {
            log.debug(ValidationExceptionMessages.RELEASE_DATE.toString());
            return false;
        }
        return true;
    }
}