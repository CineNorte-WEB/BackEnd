package com.tave.camchelin.domain.review_analysis.dto;

import java.util.List;

public record Model2ResponseDto(
    String StoreName,
    List<CategoryResult> results){
    public record CategoryResult(
            String category,
            String groupKeywords,
            String representativeSentence
    ) {}
}