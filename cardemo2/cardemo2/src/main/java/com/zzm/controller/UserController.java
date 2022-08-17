package com.zzm.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzm.common.Constants;
import com.zzm.common.Result;
import com.zzm.entity.User;
import com.zzm.entity.dto.UserDTO;
import com.zzm.service.IUserService;
import com.zzm.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chang哥哥
 * @since 2022-06-20
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        System.out.println("111");
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误 ");
        }
        UserDTO dto = userService.login(userDTO);
        System.out.println(dto);
        return Result.success(dto);
    }

    // 登录
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400,"参数错误 ");
        }
        User user = userService.register(userDTO);
        return Result.success(user);
    }

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {

        return Result.success(userService.saveOrUpdate(user));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeByIds(ids));
    }


    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findUser(@PathVariable String username) {
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("username",username);
        return Result.success(userService.getOne(objectQueryWrapper));
    }


}

