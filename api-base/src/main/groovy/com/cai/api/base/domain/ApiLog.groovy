package com.cai.api.base.domain

import com.cai.api.base.log.Log

class ApiLog extends Log{
    {
        documentName = 'api_log'
    }

    String api
    String url
    String msg

}
