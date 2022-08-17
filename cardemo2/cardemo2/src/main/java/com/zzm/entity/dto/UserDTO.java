package com.zzm.entity.dto;


import cn.hutool.core.annotation.Alias;
import lombok.Data;


@Data
public class UserDTO {

    @Alias("用户名")
    private String username;
    @Alias("密码")
    private String password;
    @Alias("昵称")
    private String nickname;

    private String token;
}
