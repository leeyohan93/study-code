package study.studyolle.account.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import study.studyolle.account.application.AccountService;
import study.studyolle.account.domain.Account;
import study.studyolle.account.domain.AccountTags;
import study.studyolle.account.domain.CurrentAccount;
import study.studyolle.account.ui.form.*;
import study.studyolle.account.ui.validator.NicknameFormValidator;
import study.studyolle.account.ui.validator.PasswordFormValidator;
import study.studyolle.jone.domain.Zone;
import study.studyolle.jone.domain.ZoneRepository;
import study.studyolle.tag.domain.Tag;
import study.studyolle.tag.domain.TagRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static study.studyolle.account.ui.SettingsController.ROOT;
import static study.studyolle.account.ui.SettingsController.SETTINGS;

@RequiredArgsConstructor
@Controller
@RequestMapping(ROOT + SETTINGS)
public class SettingsController {

    static final String ROOT = "/";
    static final String SETTINGS = "settings";
    static final String PROFILE = "/profile";
    static final String PASSWORD = "/password";
    static final String NOTIFICATIONS = "/notifications";
    static final String ACCOUNT = "/account";
    static final String TAGS = "/tags";
    static final String ZONES = "/zones";

    private final AccountService accountService;
    private final TagRepository tagRepository;
    private final ZoneRepository zoneRepository;
    private final NicknameFormValidator nicknameValidator;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @InitBinder("passwordForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("nicknameForm")
    public void nicknameFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(nicknameValidator);
    }

    @GetMapping(PROFILE)
    public String profileUpdateForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Profile.class));
        return SETTINGS + PROFILE;
    }

    @PostMapping(PROFILE)
    public String updateProfile(@CurrentAccount Account account,
                                @Valid Profile profile,
                                Errors errors,
                                Model model,
                                RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + PROFILE;
        }
        accountService.updateProfile(account, profile);
        attributes.addFlashAttribute("message", "프로필을 수정했습니다.");
        return "redirect:/" + SETTINGS + PROFILE;
    }

    @GetMapping(PASSWORD)
    public String updatePasswordForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new PasswordForm());
        return SETTINGS + PASSWORD;
    }

    @PostMapping(PASSWORD)
    public String updatePassword(@CurrentAccount Account account,
                                 @Valid PasswordForm passwordForm,
                                 Errors errors,
                                 Model model,
                                 RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + PASSWORD;
        }

        accountService.updatePassword(account, passwordForm.getNewPassword());
        attributes.addFlashAttribute("message", "프로필을 수정했습니다.");
        return "redirect:/" + SETTINGS + PASSWORD;
    }

    @GetMapping(NOTIFICATIONS)
    public String updateNotificationsForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, Notifications.class));
        return SETTINGS + NOTIFICATIONS;
    }

    @PostMapping(NOTIFICATIONS)
    public String updateNotifications(@CurrentAccount Account account,
                                      @Valid Notifications notifications,
                                      Errors errors,
                                      Model model,
                                      RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + NOTIFICATIONS;
        }

        accountService.updateNotifications(account, notifications);
        attributes.addFlashAttribute("message", "알림 설정을 변경했습니다.");
        return "redirect:/" + SETTINGS + NOTIFICATIONS;
    }

    @GetMapping(ACCOUNT)
    public String updateAccountForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(modelMapper.map(account, NicknameForm.class));
        return SETTINGS + ACCOUNT;
    }

    @PostMapping(ACCOUNT)
    public String updateAccount(@CurrentAccount Account account,
                                @Valid NicknameForm nicknameForm,
                                Errors errors,
                                Model model,
                                RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS + ACCOUNT;
        }

        accountService.updateNickname(account, nicknameForm.getNickname());
        attributes.addFlashAttribute("message", "닉네임을 변경했습니다.");
        return "redirect:/" + SETTINGS + ACCOUNT;
    }

    @GetMapping(TAGS)
    public String updateTagsForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        AccountTags accountTags = accountService.getTags(account);
        List<String> tags = accountTags.getAccountTags()
                .stream()
                .map(accountTag -> accountTag.getTag().getTitle())
                .collect(Collectors.toList());
        model.addAttribute("tags", tags);


        model.addAttribute("whitelist", tagWhiteList());
        return SETTINGS + TAGS;
    }

    private String tagWhiteList() {
        List<String> allTags = tagRepository.findAll()
                .stream()
                .map(Tag::getTitle)
                .collect(Collectors.toList());
        try {
            return objectMapper.writeValueAsString(allTags);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @PostMapping(TAGS)
    @ResponseBody
    public ResponseEntity<Object> addTags(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        String tagTitle = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(tagTitle)
                .orElseGet(() -> tagRepository.save(Tag.builder().title(tagTitle).build()));

        accountService.addTag(account, tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping(TAGS + "/remove")
    @ResponseBody
    public ResponseEntity<Object> deleteTags(@CurrentAccount Account account, @RequestBody TagForm tagForm) {
        String tagTitle = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(tagTitle)
                .orElseThrow(() -> new IllegalArgumentException(tagTitle + " 존재하지 않는 태그입니다."));

        accountService.removeTag(account, tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping(ZONES)
    public String updateZonesForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);

        List<Zone> zones = accountService.getZones(account);
        List<String> zoneFormats = zones.stream()
                .map(Zone::toString)
                .collect(Collectors.toList());
        model.addAttribute("zones", zoneFormats);
        model.addAttribute("whitelist", zoneWhiteList());

        return SETTINGS + ZONES;
    }

    private String zoneWhiteList() {
        List<String> allZones = zoneRepository.findAll()
                .stream()
                .map(Zone::toString)
                .collect(Collectors.toList());
        try {
            return objectMapper.writeValueAsString(allZones);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    @PostMapping(ZONES)
    @ResponseBody
    public ResponseEntity<Object> addZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.addZone(account, zone);
        return ResponseEntity.ok().build();
    }

    @PostMapping(ZONES + "/remove")
    @ResponseBody
    public ResponseEntity<Object> removeZone(@CurrentAccount Account account, @RequestBody ZoneForm zoneForm) {
        Zone zone = zoneRepository.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName());
        if (zone == null) {
            return ResponseEntity.badRequest().build();
        }

        accountService.removeZone(account, zone);
        return ResponseEntity.ok().build();
    }
}
