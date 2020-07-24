package com.aaa.zhang.security;

import com.aaa.zhang.config.JwtAuthentication;
import com.aaa.zhang.config.JwtFilter;
import com.aaa.zhang.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 2 * @Author: ZhangShuai
 * 3 * @Date: 2020/6/12 17:03
 * 4
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 定义认证入口点
     */
    @Autowired
    private JwtAuthentication jwtAuthentication;

    /**
     * jwt 拦截器 通过这个拦截器 去判断这个token 是否可以使用
     */
    @Autowired
    private JwtFilter jwtFilter;

    /**
     * 登录的路径
     */
    @Value("${jwt.path}")
    private String loginPath;

    /**
     * 使用Security的认证管理器  认证操作security 给我们来做
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 使用security的默认密码加密方式
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 主要是配置这个Bean，用于授权服务器配置中注入
     * @return
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    /**
     * 配置security不拦截的请求  下面可以根据需求自定义 用,号隔开
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 将 check_token 暴露出去，否则资源服务器访问时报 403 错误
        //配置暴露出去的端点  不用登陆就可以访问
        web.ignoring()
                .antMatchers(
                        HttpMethod.POST,
                        loginPath
                )
                .antMatchers( "/GetToken", "/index.html", "/css/**", "/js/**",
                "/images/**",
                "/openid/login",
                "/oauth/check_token", "/queryToken");

    }

    /**
     * 设置security 对登录的一些操作
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        //禁止跨域请求
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthentication)
                //关闭session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //设置授权请求
                .authorizeRequests().antMatchers(loginPath).permitAll()
                //设置这个路径不用拦截
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用页面缓存
        http.headers()
                .frameOptions().sameOrigin()
                .cacheControl();
    }
}


