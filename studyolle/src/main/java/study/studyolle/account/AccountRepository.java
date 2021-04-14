package study.studyolle.account;

import org.springframework.data.jpa.repository.JpaRepository;
import study.studyolle.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Account findByEmail(String email);

    Account findByNickname(String nickname);
}
