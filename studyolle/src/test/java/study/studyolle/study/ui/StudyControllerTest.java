package study.studyolle.study.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import study.studyolle.WithAccount;
import study.studyolle.account.ui.form.ZoneForm;
import study.studyolle.jone.domain.Zone;
import study.studyolle.study.application.StudyService;
import study.studyolle.study.domain.Study;
import study.studyolle.study.domain.StudyRepository;
import study.studyolle.study.ui.form.StudyForm;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static study.studyolle.study.ui.StudyController.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class StudyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyService studyService;

    @WithAccount(value = "yohan")
    @DisplayName("스터디 추가 폼")
    @Test
    void addStudyForm() throws Exception {
        mockMvc.perform(get(ROOT + STUDY))
                .andExpect(view().name(STUDY + FORM))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("studyForm"));
    }

    @WithAccount(value = "yohan")
    @DisplayName("스터디 추가")
    @Test
    void addStudy() throws Exception {
        // when
        mockMvc.perform(post(ROOT + STUDY)
                .param("title", "테스트 스터디")
                .param("path", "test-path")
                .param("fullDescription", "긴 소개")
                .param("shortDescription", "짧은 소개")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/study/test-path"));

        // then
        boolean result = studyRepository.existsByPath("test-path");
        assertTrue(result);
    }

    @WithAccount(value = "yohan")
    @DisplayName("스터디 추가 - 잘못된 입력 (중복된 경로)")
    @Test
    void addStudy_wrong_input() throws Exception {
        // given
        Study study = Study.builder()
                .path("test-path")
                .build();
        studyRepository.save(study);

        // when
        mockMvc.perform(post(ROOT + STUDY)
                .param("title", "테스트 스터디")
                .param("path", "test-path")
                .param("fullDescription", "긴 소개")
                .param("shortDescription", "짧은 소개")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(STUDY + FORM))
                .andExpect(model().hasErrors());
    }

}