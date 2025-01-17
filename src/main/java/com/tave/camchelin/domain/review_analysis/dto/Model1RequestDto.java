package com.tave.camchelin.domain.review_analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Model1RequestDto(
        @JsonProperty("storename") String storeName,  // ✅ FastAPI의 JSON 요청 형식에 맞춤
        @JsonProperty("review") String review
) { }
