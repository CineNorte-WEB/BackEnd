package com.tave.camchelin.domain.review_analysis.repository;

import com.tave.camchelin.domain.review_analysis.entity.Model2Results;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Model2ResultsRepository extends JpaRepository<Model2Results, Long> {
    Model2Results findByStoreNameAndCategoryAndSentiment(String storename, String category, String sentiment);

    List<Model2Results> findByStoreName(String storeName);;
}