package com.spring.jpa.extra.controller;

import com.spring.jpa.common.model.ResponseResult;
import com.spring.jpa.extra.model.KakaoTranslateInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@RestController
public class ApiExtraKakaoController {

    /**
     * KAKAO OPEN API를 활용한 게시글 번역서비스를 구현하는 api
     */
    @GetMapping("/api/extra/kakao/translate")
    public ResponseEntity<?> translate(@RequestBody KakaoTranslateInput kakaoTrnaslateInput) {

        String restApiKey = "xxxxxxxxxxxxxxx";
        String url = "https://dapi.kakao.com/v2/translation/translate";

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("src_lang", "kr");
        parameters.add("target_lang", "en");
        parameters.add("query", kakaoTrnaslateInput.getText());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "KakaoAK " + restApiKey);

        HttpEntity formEntity = new HttpEntity<>(parameters, headers);

        restTemplate.postForEntity(url, formEntity, String.class);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, formEntity, String.class);

        return ResponseResult.success(responseEntity.getBody());
    }
}
