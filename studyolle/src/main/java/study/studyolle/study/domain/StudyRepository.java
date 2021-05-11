package study.studyolle.study.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long> {

    boolean existsByPath(String path);


    @EntityGraph(attributePaths = {"tags", "zones", "managers", "members"})
    Study findWithAllByPath(String path);

    Study findByPath(String path);
}
