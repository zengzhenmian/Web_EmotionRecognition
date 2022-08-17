package com.zzm.service;

import com.zzm.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzm.entity.dto.UserDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chang哥哥
 * @since 2022-06-20
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);
}
