package study.studyolle.jone.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long > {

    Zone findByCityAndProvince(String cityName, String provinceName);
}
