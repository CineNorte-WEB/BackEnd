package com.tave.camchelin.global.callapi;

import com.tave.camchelin.domain.review_keywords.dto.Model2RequestDto;
import com.tave.camchelin.domain.review_keywords.dto.Model2ResponseDto;
import com.tave.camchelin.domain.review_keywords.dto.Model1RequestDto;
import com.tave.camchelin.domain.review_keywords.dto.Model1ResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class CallApiService {

    private final WebClient webClient;
    private static final String MODEL1_URL = "http://172.17.0.3:8001/generate";
    private static final String MODEL2_URL = "http://172.17.0.4:8000/analyze";

    public CallApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Model1ResponseDto callModel1Api(Model1RequestDto requestDto) {
        try {
            return webClient.post()
                    .uri(MODEL1_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8) // UTF-8 강제 설정
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(Model1ResponseDto.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to call Model1 API: " + e.getMessage(), e);
        }
    }

    public Model2ResponseDto callModel2Api(Model2RequestDto requestDto) {
        try {
            System.out.println("🔍 Sending request to Model2 API: " + requestDto);

            Model2ResponseDto response = webClient.post()
                    .uri(MODEL2_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .acceptCharset(StandardCharsets.UTF_8) // UTF-8 강제 설정
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(Model2ResponseDto.class)
                    .block();

            if (response == null) {
                System.err.println("🚨 Model2 API returned null response");
            }

            return response;
        } catch (Exception e) {
            System.err.println("🚨 Model2 API 호출 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to call Model2 API: " + e.getMessage(), e);
        }
    }
}
