package com.aaa.zhang.service;


import com.aaa.zhang.entity.SysAuthUser;


/**
 * 2 * @Author: ZhangShuai
 * 3 * @Date: 2020/6/12 17:03
 * 4
 */
public interface AuthUserService {


    /**
     * 根据用户名去查询 当前用户是否存在
     *
     * @param name
     * @return
     */
    SysAuthUser findByUserName(String name);



}
