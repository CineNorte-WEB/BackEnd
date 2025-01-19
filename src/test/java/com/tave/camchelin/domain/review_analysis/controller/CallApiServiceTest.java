package com.tave.camchelin.domain.review_analysis.controller;

import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;


@SpringBootTest
public class CallApiServiceTest { // 모델 단독 테스트
//
//    @MockBean
//    private WebClient.Builder webClientBuilder;
//
//    @Mock
//    private WebClient webClient;
//
//    @Mock
//    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
//
//    @Mock
//    private WebClient.RequestBodySpec requestBodySpec;
//
//    @Mock
//    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
//
//    @Mock
//    private WebClient.ResponseSpec responseSpec;
//
//    @InjectMocks
//    private CallApiService callApiService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        when(webClientBuilder.build()).thenReturn(webClient);
//    }
//
//    @Test
//    void callModel1Api_ShouldReturnResponseModelDto() { // 모델1 단독 테스트
//        // Given
//        RequestModelDto requestDto = new RequestModelDto("Test Store", "Great food");
//        ResponseModelDto mockResponse = new ResponseModelDto("Test Store", "Delicious", "Expensive");
//
//        // Mock 설정
//        when(webClient.post()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(eq("http://127.0.0.1:8001/analyze"))).thenReturn(requestBodySpec);
//        when(requestBodySpec.contentType(eq(MediaType.APPLICATION_JSON))).thenReturn(requestBodySpec);
//        when(requestBodySpec.bodyValue(eq(requestDto))).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(ResponseModelDto.class)).thenReturn(Mono.just(mockResponse));
//
//        // When
//        ResponseModelDto response = callApiService.callModel1Api(requestDto);
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.Positive_Keywords()).isEqualTo("Delicious");
//        assertThat(response.Negative_Keywords()).isEqualTo("Expensive");
//    }
//
//    @Test
//    void callModel2Api_ShouldReturnModel2ResponseDto() { // 모델2 단독 테스트
//        // Given
//        Model2RequestDto requestDto = new Model2RequestDto("Test Store", List.of("Delicious", "Friendly"));
//        Model2ResponseDto mockResponse = new Model2ResponseDto(
//                "Test Store",
//                List.of(new Model2ResponseDto.CategoryResult("Food", "Delicious", "Best food ever"))
//        );
//
//        when(webClient.post()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(eq("http://127.0.0.1:8000/analyze"))).thenReturn(requestBodySpec);
//        when(requestBodySpec.contentType(eq(MediaType.APPLICATION_JSON))).thenReturn(requestBodySpec);
//        when(requestBodySpec.bodyValue(eq(requestDto))).thenReturn(requestBodySpec);
//        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(Model2ResponseDto.class)).thenReturn(Mono.just(mockResponse));
//
//        // When
//        Model2ResponseDto response = callApiService.callModel2Api(requestDto);
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.StoreName()).isEqualTo("Test Store");
//        assertThat(response.results() != null && response.results().size() == 1).isTrue();
//        assertThat(response.results().get(0).category()).isEqualTo("Food");
//    }
}
