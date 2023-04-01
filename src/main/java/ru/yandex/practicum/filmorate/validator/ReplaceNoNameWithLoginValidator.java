package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.messages.ValidationExceptionMessages;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class ReplaceNoNameWithLoginValidator implements ConstraintValidator<ReplaceNoNameWithLogin, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        if (user.getName() == null || "".equals(user.getName())) {
            user.setName(user.getLogin());
            log.debug(ValidationExceptionMessages.LOGIN_TO_NAME.toString());
        }
        return true;
    }
}
