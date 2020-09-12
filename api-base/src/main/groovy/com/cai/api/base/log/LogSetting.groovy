package com.cai.api.base.log

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = 'app.log')
class LogSetting {
    boolean isUse

    String filePath

    String prefix

    long maxFileSize
}
