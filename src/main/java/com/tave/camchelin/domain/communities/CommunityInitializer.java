package com.tave.camchelin.domain.communities;

import com.tave.camchelin.domain.communities.entity.Community;
import com.tave.camchelin.domain.communities.repository.CommunityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommunityInitializer implements CommandLineRunner {

    private final CommunityRepository communityRepository;

    public CommunityInitializer(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (communityRepository.count() == 0) { // 데이터가 없는 경우에만 초기화
            communityRepository.save(Community.builder().name("boardPost").build());
            communityRepository.save(Community.builder().name("reviewPost").build());
        }
    }
}
