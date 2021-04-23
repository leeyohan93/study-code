package study.studyolle.tag.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import study.studyolle.tag.domain.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTitle(String tagTitle);
}
