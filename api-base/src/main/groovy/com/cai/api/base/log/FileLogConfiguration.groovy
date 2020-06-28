package com.cai.api.base.log

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Configuration
class FileLogConfiguration {

    @Value('${app.log.filePath:./}')
    String filePath

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "log.isUse", havingValue = "true")
    @ConditionalOnBean(LogHelper)
    FileWriter fileLogAdapter(LogHelper logHelper){
        String pathName = filePath + "/${LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)}.txt"
        return new FileLogAdapter(pathName, logHelper)
    }
}
