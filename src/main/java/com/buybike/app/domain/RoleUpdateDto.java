package com.buybike.app.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUpdateDto {
    private String memberId;
    private Role role;
}
