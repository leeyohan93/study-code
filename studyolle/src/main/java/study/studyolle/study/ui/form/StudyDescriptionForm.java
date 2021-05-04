package study.studyolle.study.ui.form;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class StudyDescriptionForm {

    @NotBlank
    @Length(max = 100)
    private String shortDescription;

    @NotBlank
    private String fullDescription;
}
