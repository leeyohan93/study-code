package study.studyolle.jone.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String localNameOfCity;

    private String province;

    @Builder
    public Zone(Long id, String city, String localNameOfCity, String province) {
        this.id = id;
        this.city = city;
        this.localNameOfCity = localNameOfCity;
        this.province = province;
    }
}
