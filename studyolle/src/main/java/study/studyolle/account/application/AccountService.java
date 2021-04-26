package study.studyolle.account.application;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import study.studyolle.account.domain.Account;
import study.studyolle.account.domain.AccountRepository;
import study.studyolle.account.domain.AccountTags;
import study.studyolle.account.domain.UserAccount;
import study.studyolle.account.ui.form.Notifications;
import study.studyolle.account.ui.form.Profile;
import study.studyolle.account.ui.form.SignUpForm;
import study.studyolle.config.AppProperties;
import study.studyolle.jone.domain.Zone;
import study.studyolle.mail.application.EmailService;
import study.studyolle.mail.domain.EmailMessage;
import study.studyolle.tag.domain.Tag;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final TemplateEngine templateEngine;
    private final AppProperties appProperties;

    @Transactional
    public Account processNewAccount(final SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(final SignUpForm signUpForm) {
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(final Account newAccount) {
        Context context = new Context();
        context.setVariable("link", "/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        context.setVariable("nickname", newAccount.getNickname());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "스터디 올래 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());

        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("스터디올래, 회원 가입 인증")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    @Transactional(readOnly = true)
    public void login(final Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }

    public void completeSignUp(final Account account) {
        account.completeSignUp();
        login(account);
    }

    @Override
    public UserDetails loadUserByUsername(final String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if (account == null) {
            throw new UsernameNotFoundException(emailOrNickname);
        }
        return new UserAccount(account);
    }

    public void updateProfile(Account account, Profile profile) {
        modelMapper.map(profile, account);
        accountRepository.save(account);
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account); // merge
    }

    public void updateNotifications(Account account, Notifications notifications) {
        modelMapper.map(notifications, account);
        accountRepository.save(account);
    }

    public void updateNickname(Account account, String nickname) {
        account.setNickname(nickname);
        accountRepository.save(account);
        login(account);
    }

    public void addTag(Account account, Tag tag) {
        accountRepository.findById(account.getId())
                .ifPresent(a -> a.addTag(tag));
    }

    public AccountTags getTags(Account account) {
        return accountRepository.findById(account.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."))
                .getAccountTags();
    }

    public void removeTag(Account account, Tag tag) {
        accountRepository.findWithTags(account.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."))
                .getAccountTags()
                .remove(tag);
    }

    public List<Zone> getZones(Account account) {
        return accountRepository.findById(account.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."))
                .getZones();
    }

    public void addZone(Account account, Zone zone) {
        accountRepository.findById(account.getId())
                .ifPresent(a -> a.addZone(zone));
    }

    public void removeZone(Account account, Zone zone) {
        accountRepository.findById(account.getId())
                .ifPresent(a -> a.remove(zone));
    }
}
