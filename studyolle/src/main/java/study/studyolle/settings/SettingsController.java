package study.studyolle.settings;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import study.studyolle.account.CurrentUser;
import study.studyolle.domain.Account;


@Controller
public class SettingsController {

    @GetMapping("settings/profile")
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(Profile.from(account));
        return "settings/profile";
    }
}
