package study.studyolle.settings;

import lombok.Data;
import lombok.NoArgsConstructor;
import study.studyolle.domain.Account;

@Data
@NoArgsConstructor
public class Profile {

    private String bio;
    private String url;
    private String occupation;
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
