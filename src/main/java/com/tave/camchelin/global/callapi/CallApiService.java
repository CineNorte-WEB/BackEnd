package com.tave.camchelin.global.callapi;

import com.tave.camchelin.domain.review_analysis.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_analysis.dto.Model2ResponseDto;
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
    private static final String MODEL2_URL = "http://127.0.0.1:8000/analyze";

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

    public Model2ResponseDto callModel2Api(Model2RequestDto requestDto) {
        return webClient
                .post()
                .uri(MODEL2_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(Model2ResponseDto.class)
                .block();
    }
}
