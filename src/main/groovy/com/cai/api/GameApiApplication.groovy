package com.cai.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan

@ComponentScan("com.cai")
@SpringBootApplication(exclude = DataSourceAutoConfiguration)
class GameApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GameApiApplication)
    }

    static void main(String[] args) {
        SpringApplication.run(GameApiApplication.class, args)
    }
}
