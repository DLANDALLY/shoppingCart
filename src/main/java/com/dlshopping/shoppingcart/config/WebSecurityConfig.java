package com.dlshopping.shoppingcart.config;


import com.dlshopping.shoppingcart.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/home","/productList","/shoppingCart","/admin/login", "/css/**", "/js/**", "/images/**","/errorpage","/error")
                        .permitAll()
                        .requestMatchers("/admin/orderList", "/admin/order", "/admin/accountInfo","/admin/logout", "/admin/index")
                        .hasAnyRole("EMPLOYEE", "MANAGER")
                        .requestMatchers("/admin/product","/admin/**","/**")
                        .hasRole("MANAGER")
                        .anyRequest().hasRole("MANAGER")
                ).formLogin(form -> form
                        .loginProcessingUrl("/j_spring_security_check")
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/accountInfo", true)
                        .failureUrl("/admin/login?error=true")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
                ).logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                ).sessionManagement(session -> session
                        //.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        //.expiredUrl("/admin/login?expired=true")
                ).exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/errorpage"));
        return http.build();
    }
}
