package com.dlshopping.shoppingcart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJdbcHttpSession
public class WebConfiguration implements WebMvcConfigurer {

}
