package com.spring.jpa.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserInput {
    @Email(message = "이메일 형식에 맞게 입력하세요.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String username;

    @Size(min = 4, message = "비밀번호는 4자 이상 입력해야 합니다.")
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @Size(max = 20, message = "연락처는 최대20자까지 입력해야 합니다.")
    @NotBlank(message = "연락처는 필수 항목입니다.")
    private String phone;
}
