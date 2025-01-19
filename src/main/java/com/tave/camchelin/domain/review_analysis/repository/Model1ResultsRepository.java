package com.tave.camchelin.domain.review_analysis.repository;

import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Model1ResultsRepository extends JpaRepository<Model1Results, Long> {

    // storeName을 기준으로 Model1Results 조회
    Optional<Model1Results> findFirstByStoreNameOrderByIdDesc(String storeName);
}
