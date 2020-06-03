package com.cai.api.base.log

import org.bson.Document

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class Log{

    String documentName

    String created = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

}
