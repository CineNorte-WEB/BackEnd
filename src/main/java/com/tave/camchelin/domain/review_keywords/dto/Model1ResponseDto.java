package com.tave.camchelin.domain.review_keywords.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Model1ResponseDto(
        @JsonProperty("StoreName") String storeName,
        @JsonProperty("Positive_Keywords") List<String> positiveKeywords,
        @JsonProperty("Negative_Keywords") List<String> negativeKeywords
) { }
