package com.tave.camchelin.domain.review_analysis.service;

import com.tave.camchelin.domain.places.entity.Place;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//public class ReviewAnalysisStoreService {
//
//    private final ReviewAnalysisStoreRepository repository;
//
//    public ReviewAnalysisStoreService(ReviewAnalysisStoreRepository repository) {
//        this.repository = repository;
//    }
//
//    // 리뷰 분석 결과 저장
//    public ReviewAnalysis saveReviewAnalysis(Place place, List<String> positiveKeywords, List<String> negativeKeywords) {
//        ReviewAnalysis reviewAnalysis = ReviewAnalysis.builder()
//                .place(place)
//                .positiveKeywords(positiveKeywords)
//                .negativeKeywords(negativeKeywords)
//                .build();
//        return repository.save(reviewAnalysis);
//    }
//
//    // 모든 리뷰 분석 결과 조회
//    public List<ReviewAnalysis> getAllReviewAnalyses() {
//        return repository.findAll();
//    }
//
//    // 특정 장소의 리뷰 분석 결과 조회
//    public List<ReviewAnalysis> getReviewAnalysisByPlace(Place place) {
//        return repository.findAll().stream()
//                .filter(review -> review.getPlace().equals(place))
//                .toList();
//    }
//}
