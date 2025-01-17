package com.tave.camchelin.domain.review_analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Model2ResponseDto(
        @JsonProperty("StoreName") // JSON 필드명과 매핑
        String storeName,

        @JsonProperty("Results") // JSON 필드명과 매핑
        List<CategoryResult> results) {

    public record CategoryResult(
            @JsonProperty("Category") // JSON 필드명과 매핑
            String category,

            @JsonProperty("Group Keywords") // JSON 필드명과 매핑
            String groupKeywords,

            @JsonProperty("Representative Sentence") // JSON 필드명과 매핑
            String representativeSentence
    ) {}
}