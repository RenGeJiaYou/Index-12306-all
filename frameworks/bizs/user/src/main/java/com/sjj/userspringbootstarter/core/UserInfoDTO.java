package com.sjj.userspringbootstarter.core;

/*
 * 用户信息 DTO
 *
 * 只有 4 个必备字段：用户ID，用户名。用户实名，token
 *
 * @Author Island_World
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private String userId;
    private String username;
    private String realName;
    private String token;
}
