package study.studyolle.account.domain;

import lombok.*;
import study.studyolle.tag.domain.Tag;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AccountTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public AccountTag(Account account, Tag tag) {
        this.account = account;
        this.tag = tag;
    }
}
