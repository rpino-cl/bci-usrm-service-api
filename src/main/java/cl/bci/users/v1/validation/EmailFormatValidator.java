package cl.bci.users.v1.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

public class EmailFormatValidator implements ConstraintValidator<ValidEmail, String> {

    @Value("${custom.security.email-regex:}")
    private String emailRegex;

    private Pattern compiledPattern;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true;
        }
        if (this.emailRegex == null || this.emailRegex.trim().isEmpty()) {
            return true;
        }
        if (this.compiledPattern == null) {
            this.compiledPattern = Pattern.compile(this.emailRegex);
        }
        return this.compiledPattern.matcher(email).matches();
    }

}
