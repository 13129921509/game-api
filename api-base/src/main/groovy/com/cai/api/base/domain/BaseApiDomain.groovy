package com.cai.api.base.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BaseApiDomain {

    String created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

}
