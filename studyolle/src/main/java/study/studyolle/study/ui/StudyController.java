package study.studyolle.study.ui;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import study.studyolle.account.domain.Account;
import study.studyolle.account.domain.CurrentAccount;
import study.studyolle.study.application.StudyService;
import study.studyolle.study.domain.Study;
import study.studyolle.study.domain.StudyRepository;
import study.studyolle.study.ui.form.StudyForm;
import study.studyolle.study.ui.validator.StudyFormValidator;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static study.studyolle.study.ui.StudyController.ROOT;
import static study.studyolle.study.ui.StudyController.STUDY;

@RequiredArgsConstructor
@Controller
@RequestMapping(ROOT + STUDY)
public class StudyController {

    static final String ROOT = "/";
    static final String STUDY = "study";
    static final String FORM = "/form";
    static final String VIEW = "/view";

    private final StudyRepository studyRepository;
    private final StudyService studyService;
    private final ModelMapper modelMapper;
    private final StudyFormValidator studyFormValidator;

    @InitBinder("studyForm")
    public void studyFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studyFormValidator);
    }

    @GetMapping
    public String newStudyForm(@CurrentAccount Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new StudyForm());
        return STUDY + FORM;
    }

    @PostMapping
    public String newStudySubmit(@CurrentAccount Account account, @Valid StudyForm studyForm, Errors errors) {
        if (errors.hasErrors()) {
            return STUDY + FORM;
        }

        Study newStudy = studyService.createNewStudy(modelMapper.map(studyForm, Study.class), account);
        return "redirect:/" + STUDY + "/" + URLEncoder.encode(newStudy.getPath(), StandardCharsets.UTF_8);
    }

    @GetMapping("/{path}")
    public String viewStudy(@CurrentAccount Account account, @PathVariable String path, Model model) {
        model.addAttribute(account);
        model.addAttribute(studyRepository.findByPath(path));
        return STUDY + VIEW;
    }

}
