package com.aaa.zhang.controller;

import com.aaa.zhang.config.JwtUtils;
import com.aaa.zhang.dto.LoginDTO;
import com.aaa.zhang.util.JwtUser;
import com.aaa.zhang.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    /**
     *  的认证管理器
     */
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * security 验证 用户名和密码
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("login")
    public Result createAuthenticationToken(@RequestBody LoginDTO loginDTO) throws Exception {
        System.out.println("username:"+loginDTO.getUsername()+",password:"+loginDTO.getPassword());
        Result result = authenticate(loginDTO.getUsername(), loginDTO.getPassword());
        if(result == null)
        {
            final JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(loginDTO.getUsername());
            System.out.println("userDetails = " + userDetails.getId());
            //生成token 返回给前台
            final String token = jwtUtils.generateToken(userDetails);
            return Result.ok("成功",token);
        }
        return result;
    }

    /**
     * 通过usernmae 和password 来调用 security来判断这个用户
     * @param username  用户名
     * @param password  密码
     * @throws Exception
     */
    private Result authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (DisabledException e) {
            //用户被禁用异常
            //throw new Exception("USER_DISABLED", e);
            return Result.fail("当前用户已被禁用");
        } catch (BadCredentialsException e) {
            //用户密码不对异常
            //throw new Exception("INVALID_CREDENTIALS", e);
            return Result.fail("密码不正确");
        }
        return null;
    }


    @GetMapping("loginSuccess")
    public String loginSuccess(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser principal = (JwtUser) authentication.getPrincipal();
        return "当前用户的id是"+principal.getId();
    }
}
