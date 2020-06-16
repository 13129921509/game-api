package com.cai.api.base

import com.cai.ais.core.send.AisSend
import com.cai.api.base.log.Log
import com.cai.api.base.log.LogHelper
import com.cai.api.base.log.MongoLogHelper
import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.log.ErrorLogManager
import com.cai.general.util.log.LogParser
import com.cai.general.util.response.ResponseMessage
import com.cai.mongo.service.MongoService
import jdk.nashorn.internal.objects.annotations.Constructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct

@Service
abstract class BaseService {

    @Autowired
    MongoService mgSvc

    @Autowired
    ErrorLogManager exceptionManager

    @Autowired
    AisSend aisSend

    @Value('${mongo.database}')
    String db

    @PostConstruct
    void init(){
        setDb()
    }

    void setDb(){
        this.db = db
    }

    void changeDb(String db){
        mgSvc.setDatabase(db)
    }

    ResponseMessage refresh(){
        afterRefresh()
    }

    ResponseMessage insertLog(LogHelper logHelper, List<Log> logs, Closure closure) {
        ResponseMessage rsp = closure.call()
        if (logs.size() > 0) {
            logs.each {
                logHelper.insertLog(it)
            }
        }
        return rsp
    }

    abstract ResponseMessage toApiData(Map date, String collection)
    abstract ResponseMessage afterRefresh()
}
