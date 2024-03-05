package com.ZeroWaveV2.FireAlertV2.config;

import com.ZeroWaveV2.FireAlertV2.model.FireTeam;
import com.ZeroWaveV2.FireAlertV2.repository.FireTeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FireTeamInitializer {

    private final FireTeamRepository fireTeamRepository;

    public FireTeamInitializer(FireTeamRepository fireTeamRepository) {
        this.fireTeamRepository = fireTeamRepository;
    }

    @Bean
    CommandLineRunner initFireTeamData() {
        return args -> {
            fireTeamRepository.save(new FireTeam("출동 1팀", 5));
            fireTeamRepository.save(new FireTeam("출동 2팀", 5));
            fireTeamRepository.save(new FireTeam("출동 3팀", 5));
            fireTeamRepository.save(new FireTeam("응급 1팀", 4));
            fireTeamRepository.save(new FireTeam("응급 2팀", 4));
            fireTeamRepository.save(new FireTeam("응급 3팀", 4));
            // 이하 동일한 패턴으로 데이터 추가
        };
    }
}
