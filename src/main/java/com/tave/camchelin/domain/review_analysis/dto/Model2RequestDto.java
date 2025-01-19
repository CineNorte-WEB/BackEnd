package com.tave.camchelin.domain.review_analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Model2RequestDto(
        @JsonProperty("store_name")String storename,
        @JsonProperty("keywords")List<String> keywords
){
}