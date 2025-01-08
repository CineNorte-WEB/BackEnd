package com.tave.camchelin.global.callapi;

import com.tave.camchelin.domain.review_analysis.dto.RequestModelDto;
import com.tave.camchelin.domain.review_analysis.dto.ResponseModelDto;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class CallApiService {

    private final WebClient webClient;
    private static final String modelUrl  = "http://127.0.0.1:8000/generate";

    public CallApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public static ResponseModelDto mentApi(RequestModelDto requestMentDto) {

        WebClient webClient = WebClient.builder().build();

        return webClient
                .post()
                .uri(modelUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestMentDto)
                .retrieve()
                .bodyToMono(ResponseModelDto.class)
                .block();
    }
}
