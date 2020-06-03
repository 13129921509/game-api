package com.cai.api.base.log

import com.cai.mongo.service.MongoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.Assert

@Component
class MongoLogHelper<T extends Log> implements LogHelper{

    @Autowired
    MongoService mongoSvc

    @Override
    boolean insertLog(Log log) {
        try {
            Assert.notNull(log.documentName,"documentName is not null!!!")
            mongoSvc.insertWithExpireDays(log.documentName as String,log,30)
            return true
        }catch(Throwable t){
            t.printStackTrace()
            return false
        }

    }
}
