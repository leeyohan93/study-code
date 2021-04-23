package study.studyolle.jone.application;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.studyolle.jone.domain.Zone;
import study.studyolle.jone.domain.ZoneRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    @PostConstruct
    public void initZoneData() throws IOException {
        if (zoneRepository.count() == 0) {
            Resource resource = new ClassPathResource("zones_kr.csv");
            List<Zone> zones = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8)
                    .stream()
                    .map(line -> lineToZone(line))
                    .collect(Collectors.toList());
            zoneRepository.saveAll(zones);
        }
    }

    private Zone lineToZone(String line) {
        String[] split = line.split(",");
        return Zone.builder()
                .city(split[0])
                .localNameOfCity(split[1])
                .province(split[2])
                .build();
    }
}
