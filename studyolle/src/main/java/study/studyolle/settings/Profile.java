package study.studyolle.settings;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import study.studyolle.domain.Account;

@Data
@NoArgsConstructor
public class Profile {

    @Length(max = 35)
    private String bio;

    @Length(max = 50)
    private String url;

    @Length(max = 50)
    private String occupation;

    @Length(max = 50)
    private String location;

    public static Profile from(Account account) {
        return new Profile(
                account.getBio(),
                account.getUrl(),
                account.getOccupation(),
                account.getLocation());
    }

    public Profile(String bio, String url, String occupation, String location) {
        this.bio = bio;
        this.url = url;
        this.occupation = occupation;
        this.location = location;
    }
}
