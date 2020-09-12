package com.cai.api.base.log

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.stereotype.Component

@Deprecated
@Component
interface ContentParser {
    String parser(String content)
}