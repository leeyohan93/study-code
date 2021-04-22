package study.studyolle.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import study.studyolle.WithAccount;
import study.studyolle.account.application.AccountService;
import study.studyolle.account.domain.AccountRepository;
import study.studyolle.account.domain.Account;
import study.studyolle.account.domain.AccountTag;
import study.studyolle.settings.form.TagForm;
import study.studyolle.tag.Tag;
import study.studyolle.tag.TagRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @WithAccount(value = "yohan")
    @DisplayName("계정의 태그 수정폼")
    @Test
    void updateTagForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_TAGS_URL))
                .andExpect(view().name(SettingsController.SETTINGS_TAGS_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("tags"))
                .andExpect(model().attributeExists("whitelist"));
    }

    @WithAccount(value = "yohan")
    @DisplayName("계정에 태그 추가")
    @Test
    void addTag() throws Exception {
        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andExpect(status().isOk());

        Tag tag = tagRepository.findByTitle("newTag").get();
        boolean result = accountRepository.findByNickname("yohan")
                .getAccountTags()
                .getAccountTags()
                .stream()
                .map(AccountTag::getTag)
                .collect(Collectors.toList())
                .contains(tag);
        assertTrue(result);
    }

    @WithAccount(value = "yohan")
    @DisplayName("계정에 태그 삭제")
    @Test
    void removeTag() throws Exception {
        Account yohan = accountRepository.findByNickname("yohan");
        Tag newTag = tagRepository.save(Tag.builder().title("newTag").build());
        accountService.addTag(yohan, newTag);

        List<Tag> tags = getTags(yohan);
        assertTrue(tags.contains(newTag));

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL + "/remove")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tagForm))
        .with(csrf()))
        .andExpect(status().isOk());

        List<Tag> newTags = getTags(yohan);
        assertFalse(newTags.contains(newTag));
    }

    private List<Tag> getTags(Account account) {
        return account.getAccountTags()
                .getAccountTags()
                .stream()
                .map(AccountTag::getTag)
                .collect(Collectors.toList());
    }

    @WithAccount(value = "yohan")
    @DisplayName("닉네임 수정폼")
    @Test
    void updateNicknameForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @WithAccount(value = "yohan")
    @DisplayName("닉네임 수정하기 - 입력값 정상")
    @Test
    void updateNickname() throws Exception {
        String newNickname = "yohan2";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                .param("nickname", newNickname)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(flash().attributeExists("message"));

        Account yohan2 = accountRepository.findByNickname("yohan2");
        assertNotNull(yohan2);
    }

    @WithAccount(value = "yohan")
    @DisplayName("닉네임 수정하기 - 입력값 에러")
    @Test
    void updateNickname_error() throws Exception {
        String duplicatedNickname = "yohan";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                .param("nickname", duplicatedNickname)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"))
                .andExpect(model().hasErrors());
    }

    @WithAccount(value = "yohan")
    @DisplayName("프로필 수정폼")
    @Test
    void updateProfileForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithAccount(value = "yohan")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        String bio = "짧은 소개를 수정";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));

        Account yohan = accountRepository.findByNickname("yohan");
        assertEquals(bio, yohan.getBio());
    }

    @WithAccount(value = "yohan")
    @DisplayName("프로필 수정하기 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception {
        String bio = "너무 길게 소개를 수정하는 경우. 너무 길게 소개를 수정하는 경우. 너무 길게 소개를 수정하는 경우. 너무 길게 소개를 수정하는 경우.";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account yohan = accountRepository.findByNickname("yohan");
        assertNull(yohan.getBio());
    }
}