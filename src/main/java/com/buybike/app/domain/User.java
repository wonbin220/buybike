package com.buybike.app.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long num;
    private String userId;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime regDt;
    private Role role; // 사용자 권한 (USER, ADMIN)

    public static User createUser(UserFormDto userFormDto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUserId(userFormDto.getUserId());
        user.setUsername(userFormDto.getUsername());
        user.setPhone(userFormDto.getPhone());
        user.setEmail(userFormDto.getEmail());
        user.setAddress(userFormDto.getAddress());
        String password = passwordEncoder.encode(userFormDto.getPassword());
        user.setPassword(password);
        user.setRole(Role.USER);
        return user;
    }
}
