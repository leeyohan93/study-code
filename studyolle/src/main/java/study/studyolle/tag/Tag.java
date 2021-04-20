package study.studyolle.tag;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Builder
    public Tag(String title) {
        this.title = title;
    }
}
