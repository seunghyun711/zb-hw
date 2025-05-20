package com.spring.jpa.extra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.jpa.common.model.ResponseResult;
import com.spring.jpa.extra.model.AirInput;
import com.spring.jpa.extra.model.OpenApiResult;
import com.spring.jpa.extra.model.PharmacySearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ApiExtraController {

    /**
     * RestTemplate을 이용한 공공 데이터 포털의 공공 api와 연동하여 전국 약국 목록을 가져오는 api
     */
//    @GetMapping("/api/extra/pharmacy")
//    public String pharmacy() {
//        String apiKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
//        String url = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown?serviceKey=%s&pageNo=1&numOfRows=10";
//        String apiResult = "";
//
//        try {
//            URI uri = new URI(String.format(url, apiKey));
//
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//            String result = restTemplate.getForObject(uri, String.class);
//
//            apiResult = result;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return apiResult;
//    }

    /**
     * RestTemplate을 이용한 공공 데이터 포털의 공공 api와 연동하여 전국 약국 목록을 가져오는 api
     * 결과 데이터를 모델로 매핑하여 처리
     */
//    @GetMapping("/api/extra/pharmacy")
//    public ResponseEntity<?> pharmacy() {
//        String apiKey = "xxxxxxxxxxxxxxxxxxxxxxxxx";
//        String url = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown?serviceKey=%s&pageNo=1&numOfRows=10";
//        String apiResult = "";
//
//        try {
//            URI uri = new URI(String.format(url, apiKey));
//
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//            String result = restTemplate.getForObject(uri, String.class);
//
//            apiResult = result;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        OpenApiResult jsonResult = null;
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            jsonResult = objectMapper.readValue(apiResult, OpenApiResult.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        return ResponseResult.success(jsonResult);
//    }


    /**
     * RestTemplate를 이용한 공공 데이터 포털의 공겅 api와 연동하여 전국 약국목록을 가져오는 api
     * 회원가입 후 이용 가능
     * 전국 약국 정보 조히 서비스 키워드 검색이후 활용신청 후 조회 가능
     * 시도/구군 단위 검색기능에 대한 구현 추가
     * 결과 데이터를 모델로 매핑하여 처리
     */
    @GetMapping("/api/extra/pharmacy")
    public ResponseEntity<?> pharmacy(@RequestBody PharmacySearch pharmacySearch) {
        String apiKey = "CbZbKV1CUPs3LkTl6PMZrD4I2vDI6wO7LnKJxUL2LIh%2BtYwRXWPf2%2FjAg8tM%2FkU1FlN9159Uf6FM%2BjJVkuy6%2Fw%3D%3D";
        String url = String.format("http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown?serviceKey=%s&pageNo=1&numOfRows=10", apiKey);
        String apiResult = "";

        try {
            url += String.format("&Q0=%s&Q1=%s"
                    , URLEncoder.encode(pharmacySearch.getSearchSido(), "UTF-8")
                    , URLEncoder.encode(pharmacySearch.getSearchGugun(), "UTF-8"));
            URI uri = new URI(url);

            log.info("url : ", uri);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String result = restTemplate.getForObject(uri, String.class);

            apiResult = result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        OpenApiResult jsonResult = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonResult = objectMapper.readValue(apiResult, OpenApiResult.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ResponseResult.success(jsonResult);
    }

    /**
     * 미세먼지 정보 조회(공공 API)를 통해서 내용을 내리는 api
     */
    @GetMapping("/api/extra/air")
    public String air(@RequestBody AirInput airInput) {

        String apiKey = "xxxxxxxxxxxxxxxxxxxxxxx";
        String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=%s&pageNo=1&numOfRows=10&sidoName=%s";

        String apiResult = "";

        try {
            URI uri = new URI(String.format(url, apiKey, URLEncoder.encode(airInput.getSearchSido(), "UTF-8")));

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String result = restTemplate.getForObject(uri, String.class);

            apiResult = result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return apiResult;
    }

}
