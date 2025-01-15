package com.tave.camchelin.domain.review_analysis.repository;

import com.tave.camchelin.domain.review_analysis.entity.Model1Results;
import com.tave.camchelin.domain.review_analysis.entity.ReviewAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Model1ResultsRepository extends JpaRepository<Model1Results, Long> {
}
