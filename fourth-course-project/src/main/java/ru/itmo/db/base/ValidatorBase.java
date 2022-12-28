package ru.itmo.db.base;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidatorBase <T>{
    private ValidatorFactory factory;
    private Validator validator;

    public ValidatorBase() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public boolean validate(T validateObject) {
        Set<ConstraintViolation<T>> violations = validator.validate(validateObject);
        if (violations.isEmpty()) return true;

        for (ConstraintViolation<T> violation : violations) {
            System.out.println((violation.getMessage()));
        }
        return false;
    }
}
