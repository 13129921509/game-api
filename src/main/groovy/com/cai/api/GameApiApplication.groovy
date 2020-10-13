package com.cai.api

import com.cai.web.core.AsyncPrintLogConsumer
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.ComponentScan

@ComponentScan("com.cai")
@SpringBootApplication
class GameApiApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GameApiApplication)
    }

    static void main(String[] args) {
        SpringApplication.run(GameApiApplication.class, args)
    }
}
