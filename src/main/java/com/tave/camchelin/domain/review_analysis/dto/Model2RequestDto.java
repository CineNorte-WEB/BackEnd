package com.tave.camchelin.domain.review_analysis.dto;

import java.util.List;

public record Model2RequestDto(
        String storename,
        List<String> keywords
){
}