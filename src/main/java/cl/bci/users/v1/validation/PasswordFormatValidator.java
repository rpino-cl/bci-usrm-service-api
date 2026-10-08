package cl.bci.users.v1.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

public class PasswordFormatValidator implements ConstraintValidator<ValidPassword, String> {

    @Value("${custom.security.password-regex:}")
    private String passwordRegex;

    private Pattern compiledPattern;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return true;
        }
        if (this.passwordRegex == null || this.passwordRegex.trim().isEmpty()) {
            return true;
        }
        if (this.compiledPattern == null) {
            this.compiledPattern = Pattern.compile(this.passwordRegex);
        }
        return this.compiledPattern.matcher(password).matches();
    }

}
