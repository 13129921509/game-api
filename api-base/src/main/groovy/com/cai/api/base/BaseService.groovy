package com.cai.api.base

import com.cai.api.base.log.Log
import com.cai.api.base.log.LogHelper
import com.cai.api.base.log.MongoLogHelper
import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.mongo.service.MongoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
abstract class BaseService {

    @Autowired
    MongoService mgSvc

    @Value('${mongo.database}')
    String db


    ResponseMessage refresh(){
        afterRefresh()
    }

    ResponseMessage insertLog(LogHelper logHelper, List<Log> logs, Closure closure) {
        ResponseMessage rsp = closure.call()
        if (logs.size() > 0) {
            logs.each {
                mgSvc.insertWithExpireDays(it.documentName as String, ConvertUtil.JSON.convertValue(it, Map),30L)
            }
        }
        return rsp
    }

    abstract ResponseMessage afterRefresh()
}
