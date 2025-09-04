package com.buybike.app.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberFormDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String memberId;
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String memberName;
    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String phone;
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;
    private Role role;


    public MemberFormDto(String memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
    }
}
