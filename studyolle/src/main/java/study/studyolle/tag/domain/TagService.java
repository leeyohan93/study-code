package study.studyolle.tag.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag getTag(String tagTitle){
        return tagRepository.findByTitle(tagTitle)
                .orElseThrow(() -> new IllegalArgumentException(tagTitle + " 존재하지 않는 태그입니다."));
    }

    public List<Tag> getTags(){
        return tagRepository.findAll();
    }

    public Tag findOrCreateNew(String tagTitle){
        return tagRepository.findByTitle(tagTitle)
                .orElseGet(() -> tagRepository.save(Tag.builder()
                        .title(tagTitle)
                        .build()));
    }
}
