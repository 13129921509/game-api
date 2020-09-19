package com.cai.api.base.log

import com.cai.general.util.jackson.ConvertUtil
import com.cai.mongo.service.MongoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.Assert

class MongoLogHelper<T extends Log> implements LogHelper{

    MongoService mongoSvc

    MongoLogHelper(MongoService mongoSvc) {
        this.mongoSvc = mongoSvc
    }

    @Override
    boolean insertLog(Log log) {
        try {
            Assert.notNull(log.documentName,"documentName is not null!!!")
            mongoSvc.insertWithExpireDays(log.documentName, ConvertUtil.JSON.convertValue(log, Map),30L)
            return true
        }catch(Throwable t){
            t.printStackTrace()
            return false
        }

    }
}
