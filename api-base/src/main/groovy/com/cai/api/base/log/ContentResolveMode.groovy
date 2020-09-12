package com.cai.api.base.log

import sun.util.resources.LocaleData

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Deprecated
abstract class ContentResolveMode<T> {
    String $time, $content, $number

    abstract String getResult(T pattern)

    class ContentResolveData{

        static String $time = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        static String $content

        static String $number = 0
    }
}
