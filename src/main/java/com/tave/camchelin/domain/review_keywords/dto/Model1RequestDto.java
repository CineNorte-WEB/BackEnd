package com.tave.camchelin.domain.review_keywords.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Model1RequestDto(
        @JsonProperty("storename") String storeName,  // ✅ FastAPI의 JSON 요청 형식에 맞춤
        @JsonProperty("review") String review
) { }
