package study.studyolle.settings;

import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.studyolle.account.AccountService;
import study.studyolle.account.CurrentUser;
import study.studyolle.domain.Account;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class SettingsController {

    private static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";
    private static final String SETTINGS_PROFILE_URL = "/settings/profile";

    private final AccountService accountService;

    @GetMapping(SETTINGS_PROFILE_URL)
    public String profileUpdateForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(Profile.from(account));
        return SETTINGS_PROFILE_VIEW_NAME;
    }

    @PostMapping(SETTINGS_PROFILE_URL)
    public String updateProfile(@CurrentUser Account account,
                                @Valid Profile profile,
                                Errors errors,
                                Model model,
                                RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PROFILE_VIEW_NAME;
        }
        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message","프로필을 수정했습니다.");
        return "redirect:" + SETTINGS_PROFILE_URL;
    }
}
