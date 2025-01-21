package com.tave.camchelin.domain.review_keywords.service;

import com.tave.camchelin.domain.places.entity.Place;
import com.tave.camchelin.domain.review_keywords.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_keywords.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_keywords.dto.Model1RequestDto;
import com.tave.camchelin.domain.review_keywords.dto.Model1ResponseDto;
import com.tave.camchelin.domain.review_keywords.entity.Model1Results;
import com.tave.camchelin.domain.review_keywords.entity.Model2Results;
import com.tave.camchelin.domain.review_keywords.repository.Model1ResultsRepository;
import com.tave.camchelin.domain.review_keywords.repository.Model2ResultsRepository;
import com.tave.camchelin.global.callapi.CallApiService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewProcessService {

    //@Autowired
    private final Model1ResultsRepository model1Repository;
    //@Autowired
    private final Model2ResultsRepository model2Repository;
    private final CallApiService callApiService;

    public ReviewProcessService(
            Model1ResultsRepository model1Repository,
            Model2ResultsRepository model2Repository,
            CallApiService callApiService
    ) {
        this.model1Repository = model1Repository;
        this.model2Repository = model2Repository;
        this.callApiService = callApiService;
    }

    @Transactional
    public List<Model2Results> analyzeAndSave(Model1RequestDto requestDto, Place place) {
        // Step 1: 모델1 호출 및 저장
        Model1ResponseDto model1Response = callApiService.callModel1Api(requestDto);

        Model1Results model1Result = Model1Results.builder()
                .storeName(requestDto.storeName())
                .positiveKeywords(model1Response.positiveKeywords())
                .negativeKeywords(model1Response.negativeKeywords())
                .build();
        Model1Results savedModel1Result = model1Repository.save(model1Result);

        // Step 2: 모델2 호출 및 저장
        List<String> model2Keywords = new ArrayList<>();
        model2Keywords.addAll(model1Response.positiveKeywords());
        model2Keywords.addAll(model1Response.negativeKeywords());

        Model2RequestDto model2Request = new Model2RequestDto(requestDto.storeName(), model2Keywords);
        Model2ResponseDto model2Response = callApiService.callModel2Api(model2Request);

        List<Model2Results> savedModel2Results = new ArrayList<>();
        for (Model2ResponseDto.CategoryResult result : model2Response.results()) {
            Model2Results model2Result = Model2Results.builder()
            //        .model1Result(savedModel1Result)
                    .storeName(requestDto.storeName())
                    .category(result.category())
                    .groupKeywords(List.of(result.groupKeywords().split(", ")))
                    .representativeSentence(result.representativeSentence())
                    .build();
            savedModel2Results.add(model2Repository.save(model2Result));
        }

        return savedModel2Results;
    }
}