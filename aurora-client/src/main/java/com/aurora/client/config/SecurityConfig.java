package com.aurora.client.config;

import com.aurora.client.common.CommonResult;
import com.aurora.client.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.aurora.client.common.enumeration.ResultCode._401;
import static com.aurora.client.common.enumeration.ResultCode._403;

/**
 * Spring Security 配置
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录注册接口 允许匿名访问
                .antMatchers("/user/signIn", "/user/signUp").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 添加过滤器
//        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                // 访问被拒绝时的处理
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    CommonResult<Object> result = new CommonResult<>(_403);
                    ResponseUtils.response(response, result.toJsonString());
                })
                // 访问未认证时的处理
                .authenticationEntryPoint((request, response, authException) -> {
                    CommonResult<Object> result = new CommonResult<>(_401);
                    ResponseUtils.response(response, result.toJsonString());
                });

        return http.build();
    }

    /**
     * 注入BCrypt算法编码器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
