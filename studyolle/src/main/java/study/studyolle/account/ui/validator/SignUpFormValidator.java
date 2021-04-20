package study.studyolle.account.ui.validator;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import study.studyolle.account.domain.AccountRepository;
import study.studyolle.account.ui.form.SignUpForm;

@RequiredArgsConstructor
@Component
public class SignUpFormValidator implements Validator {

    private AccountRepository accountRepository;

    @Override
    public boolean supports(final Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;
        if(accountRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email","invalid.email",new Object[]{signUpForm.getEmail()},"이미 등록된 이메일입니다.");
        }

        if(accountRepository.existsByNickname(signUpForm.getNickname())){
            errors.rejectValue("email","invalid.email",new Object[]{signUpForm.getNickname()},"이미 등록된 닉네임입니다.");
        }
    }
}
