package study.studyolle.study.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.studyolle.account.domain.Account;
import study.studyolle.study.domain.Study;
import study.studyolle.study.domain.StudyRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;

    public Study createNewStudy(Study study, Account account) {
        study.addManger(account);
        return studyRepository.save(study);
    }
}
