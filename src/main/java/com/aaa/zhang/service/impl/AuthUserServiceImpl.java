package com.aaa.zhang.service.impl;


import com.aaa.zhang.dao.SysAuthUserMapper;
import com.aaa.zhang.entity.SysAuthUser;
import com.aaa.zhang.service.AuthUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2 * @Author: ZhangShuai
 * 3 * @Date: 2020/6/12 17:03
 * 4
 */
@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    SysAuthUserMapper authUserMapper;


    //根据用户名来查询这个用户
    @Override
    public SysAuthUser findByUserName(String name) {
        LambdaQueryWrapper<SysAuthUser> lambda = new QueryWrapper<SysAuthUser>().lambda();
        lambda.eq(SysAuthUser::getUserName, name);
        return authUserMapper.selectOne(lambda);
    }



}
