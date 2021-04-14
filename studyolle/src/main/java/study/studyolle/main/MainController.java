package study.studyolle.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import study.studyolle.account.CurrentUser;
import study.studyolle.domain.Account;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
