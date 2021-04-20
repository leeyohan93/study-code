package study.studyolle.account.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AccountTags {

    @OneToMany(mappedBy = "account", cascade = CascadeType.PERSIST)
    private List<AccountTag> accountTags = new ArrayList<>();

    public void add(AccountTag tag) {
        accountTags.add(tag);
    }
}
