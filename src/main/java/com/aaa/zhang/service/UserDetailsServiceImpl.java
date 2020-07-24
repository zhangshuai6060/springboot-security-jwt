package com.aaa.zhang.service;


import com.aaa.zhang.entity.SysAuthUser;
import com.aaa.zhang.util.JwtUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * 2 * @Author: ZhangShuai
 * 3 * @Date: 2020/6/12 17:03
 * 4 自定义用户认证与授权
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    AuthUserService authUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysAuthUser authUser = authUserService.findByUserName(username);
        // 查出用户权限
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // 构建返回信息
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUsername(authUser.getUserName());
        jwtUser.setPassword(authUser.getPassword());
        jwtUser.setAuthorities(grantedAuthorities);
        jwtUser.setId(authUser.getId());
        return jwtUser;
    }
}
