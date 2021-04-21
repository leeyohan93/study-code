package study.studyolle.account.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.studyolle.tag.Tag;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class AccountTags {

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountTag> accountTags = new ArrayList<>();

    public void add(AccountTag tag) {
        accountTags.add(tag);
    }

    public void remove(Tag tag) {
        accountTags.removeIf(accountTag -> accountTag.getTag().equals(tag));
    }
}
