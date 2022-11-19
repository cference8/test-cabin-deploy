package com.cf.cabinescaperoom.config;

import com.cf.cabinescaperoom.config.oAuth.CustomOAuth2UserService;
import com.cf.cabinescaperoom.config.oAuth.OAuthLoginSuccessHandler;
import com.cf.cabinescaperoom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserService service;

    public void configureGlobalInDb(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().and().userDetailsService(service).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .antMatchers("/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .antMatchers("/viewScores", "/scoreForm", "/addScoreForm", "/form", "/contact")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/home", "/createAccount", "/login", "/oauth2/**",
                        "/forgot_password", "/forgot_password_form",
                        "/reset_password_form", "/reset_password", "/contact-style").permitAll()
                .antMatchers("/css/**", "/js/**", "/fonts/**", "/login-style/**", "/images/**", "/vendor/**", "/create-account-style/**").permitAll()
                .anyRequest().hasRole("USER")
                .and()
                .formLogin().permitAll()
                .loginPage("/login")
                .failureUrl("/login?login_error=1")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(databaseLoginSuccessHandler)
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauth2UserService)
                .and()
                .successHandler(oauthLoginSuccessHandler)
                .and()
                .logout().logoutSuccessUrl("/").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error/403")
                .and().build();
    }

    @Autowired
    private CustomOAuth2UserService oauth2UserService;

    @Autowired
    private OAuthLoginSuccessHandler oauthLoginSuccessHandler;

    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
}
