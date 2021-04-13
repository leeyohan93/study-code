package study.studyolle.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(of = "id")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private boolean emailVerified;

    private String emailCheckToken;

    private LocalDateTime joinedAt;

    private String bio;

    private String url;

    private String occupation;

    private String location;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage;

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdateResultByEmail;

    private boolean studyUpdateResultByWeb;

    @Builder
    public Account(final Long id, final String email, final String nickname, final String password,
                   final boolean emailVerified, final String emailCheckToken, final LocalDateTime joinedAt,
                   final String bio, final String url, final String occupation, final String location,
                   final String profileImage, final boolean studyCreatedByEmail, final boolean studyCreatedByWeb,
                   final boolean studyEnrollmentResultByEmail, final boolean studyEnrollmentResultByWeb,
                   final boolean studyUpdateResultByEmail, final boolean studyUpdateResultByWeb) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.emailVerified = emailVerified;
        this.emailCheckToken = emailCheckToken;
        this.joinedAt = joinedAt;
        this.bio = bio;
        this.url = url;
        this.occupation = occupation;
        this.location = location;
        this.profileImage = profileImage;
        this.studyCreatedByEmail = studyCreatedByEmail;
        this.studyCreatedByWeb = studyCreatedByWeb;
        this.studyEnrollmentResultByEmail = studyEnrollmentResultByEmail;
        this.studyEnrollmentResultByWeb = studyEnrollmentResultByWeb;
        this.studyUpdateResultByEmail = studyUpdateResultByEmail;
        this.studyUpdateResultByWeb = studyUpdateResultByWeb;
    }

    public void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    public void completeSignUp() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(final String token) {
        return this.emailCheckToken.equals(token);
    }
}
