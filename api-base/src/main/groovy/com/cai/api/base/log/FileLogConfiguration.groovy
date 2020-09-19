package com.cai.api.base.log

import com.cai.mongo.service.MongoService
import com.cai.redis.RedisLockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Configuration
@ConditionalOnBean(RedisLockService)
class FileLogConfiguration {

    @Value('${app.log.filePath:./}')
    String filePath

    @Autowired
    LogSetting ls

    @Autowired
    RedisLockService relSvc

    @Autowired
    MongoService mgSvc

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "app", name = "log.isUse", havingValue = "true")
    LogHelper<Log> fileLogAdapter(){
        return new FileLogAdapter<Log, Map>(relSvc, ls.filePath, ls.prefix, new MongoLogHelper(mgSvc), ls.maxFileSize, Map.class)
    }

    @Bean
    @ConditionalOnMissingBean(LogHelper)
    LogHelper<Log> mongoLogHelper(){
        return new MongoLogHelper(mgSvc)
    }
}
