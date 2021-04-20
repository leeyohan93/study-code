package study.studyolle.account.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AccountTags {

    @OneToMany
    private List<AccountTag> accountTags;

    public AccountTags(List<AccountTag> accountTags) {
        this.accountTags = accountTags;
    }
}
