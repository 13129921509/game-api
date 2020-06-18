package com.cai.api.base.domain

import com.cai.general.core.BaseEntity

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class BaseApiDomain extends BaseEntity{

    String created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

}
