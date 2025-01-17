package com.tave.camchelin.domain.review_analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Model1ResponseDto(
        @JsonProperty("StoreName") String storeName,
        @JsonProperty("Positive_Keywords") String positiveKeywords,
        @JsonProperty("Negative_Keywords") String negativeKeywords
) { }
