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
         http.authorizeRequests()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/viewScores", "/scoreForm", "/addScoreForm", "/form", "/viewAllScores", "/editScoreForm", "/viewDetails*",
                "/contact", "/login/**", "/logout").access("hasRole('ROLE_USER')")
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/resources/", "/login/**", "/logout").permitAll()
                .and()
                .formLogin().permitAll()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
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
                .deleteCookies("JSESSIONID")
                .and()
                .rememberMe()
                .key("uniqueAndSecret");

                return http.build();
    }

    @Autowired
    private CustomOAuth2UserService oauth2UserService;

    @Autowired
    private OAuthLoginSuccessHandler oauthLoginSuccessHandler;

    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;
}
