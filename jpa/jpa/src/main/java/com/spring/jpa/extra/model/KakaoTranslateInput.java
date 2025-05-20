package com.spring.jpa.extra.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoTranslateInput {

    private String text;

}
