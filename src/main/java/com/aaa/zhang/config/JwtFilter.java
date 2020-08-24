package com.aaa.zhang.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aaa.zhang.util.JwtUser;
import com.aaa.zhang.util.RedisMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * jwt filter用来过滤 输入的token 是否有效
 */
@Component
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RedisMethod redisMethod;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        //设置header信息 从token后面来取 生成的token
        final String requestTokenHeader = request.getHeader("token");
        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null ) {
            jwtToken = requestTokenHeader;
            try {
                username = jwtUtils.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                //抛出异常 告诉当前令牌无效了
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                //抛出错误码 前端根据错误码 跳转到登录页面 去重新刷新令牌
                System.out.println("JWT Token has expired");
            }
        }
        //每次都会走这个 并且每次都会去刷新一个数据库
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //这里我们把用户信息放到redis中 然后每次如果token成功去根据redis的信息 重新制造一下 放到SecurityContextHolder中
            UserDetails userDetails = redisMethod.getResult(username);
            //我们拿着用户名 去请求一次 Security 然后我们手动设置到 Security中
            if (jwtUtils.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 去验证当前 密码和账号是否正确
                // 如果认证通过了 那么就
                // 设置到Security中
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
