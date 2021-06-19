package study.studyolle.study.domain;

import lombok.*;
import study.studyolle.account.domain.Account;
import study.studyolle.account.domain.UserAccount;
import study.studyolle.jone.domain.Zone;
import study.studyolle.tag.domain.Tag;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Study {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String path;

    private String title;

    private String shortDescription;

    private LocalDateTime publishedDateTime;

    private LocalDateTime closedDateTime;

    private LocalDateTime recruitingUpdatedDateTime;

    private boolean recruiting;

    private boolean published;

    private boolean closed;

    private boolean useBanner;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fullDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToMany
    private Set<Account> managers = new HashSet<>();

    @ManyToMany
    private Set<Account> members = new HashSet<>();

    @ManyToMany
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany
    private Set<Zone> zones = new HashSet<>();

    @Builder
    public Study(Long id, String path, String title, String shortDescription, LocalDateTime publishedDateTime,
                 LocalDateTime closedDateTime, LocalDateTime recruitingUpdatedDateTime, boolean recruiting,
                 boolean published, boolean closed, boolean useBanner, String fullDescription, String image) {
        this.id = id;
        this.path = path;
        this.title = title;
        this.shortDescription = shortDescription;
        this.publishedDateTime = publishedDateTime;
        this.closedDateTime = closedDateTime;
        this.recruitingUpdatedDateTime = recruitingUpdatedDateTime;
        this.recruiting = recruiting;
        this.published = published;
        this.closed = closed;
        this.useBanner = useBanner;
        this.fullDescription = fullDescription;
        this.image = image;
    }

    public void addManger(Account account) {
        managers.add(account);
    }

    public boolean isJoinable(UserAccount userAccount) {
        Account account = userAccount.getAccount();
        return this.isPublished() && this.isRecruiting()
                && !this.members.contains(account) && !this.managers.contains(account);

    }

    public boolean isMember(UserAccount userAccount) {
        return this.members.contains(userAccount.getAccount());
    }

    public boolean isManager(UserAccount userAccount) {
        return isManager(userAccount.getAccount());
    }

    public boolean isManager(Account account) {
        return this.managers.contains(account);
    }

    public void publish() {
        if (this.closed || this.published) {
            throw new IllegalStateException("스터디를 공개할 수 없는 상태입니다. 스터디를 이미 공개했거나 종료했습니다.");
        }
        this.published = true;
        this.publishedDateTime = LocalDateTime.now();
    }

    public void close() {
        if (!this.published || this.closed) {
            throw new IllegalStateException("스터디를 종료할 수 없습니다. 스터디를 공개하지 않았거나 이미 종료한 스터디입니다.");
        }
        this.closed = true;
        this.closedDateTime = LocalDateTime.now();
    }

    public void startRecruit() {
        if (!canUpdateRecruiting()) {
            throw new IllegalStateException("인원 모집을 시작할 수 없습니다. 스터디를 공개하거나 한 시간 뒤 다시 시도하세요.");
        }
        this.recruiting = true;
        this.recruitingUpdatedDateTime = LocalDateTime.now();
    }

    public void stopRecruit() {
        if (!canUpdateRecruiting()) {
            throw new IllegalStateException("인원 모집을 멈출 수 없습니다. 스터디를 공개하거나 한 시간 뒤 다시 시도하세요.");
        }
        this.recruiting = false;
        this.recruitingUpdatedDateTime = LocalDateTime.now();
    }

    public boolean canUpdateRecruiting() {
        return this.published && this.recruitingUpdatedDateTime == null || this.recruitingUpdatedDateTime.isBefore(LocalDateTime.now().minusHours(1));
    }

    public boolean isRemovable() {
        return !this.published;
    }
}