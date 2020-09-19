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
import org.bson.types.ObjectId
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

    @Autowired
    LogHelper<Log> logHelper

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

    ResponseMessage insertLog( List<Log> logs, Closure closure) {
        ResponseMessage rsp = closure.call()
        if (logs.size() > 0) {
//            logs.each {
////                logHelper.insertLog(it)
//            }
            logs.forEach{
                logHelper.insertLog(it)
            }
        }
        return rsp
    }

    abstract ResponseMessage afterRefresh()

    protected boolean gridFsDeletedAndUploadStream(String name, String addr, bucket = null){
        InputStream is
        ObjectId id
        try{
            is = new URL(addr).openStream()
            if (bucket){
                mgSvc.gridFsDeleteFileByName(name, bucket)
                id = mgSvc.gridFsUploadStream(is , name, bucket, null, null)
            }else {
                mgSvc.gridFsDeleteFileByName(name)
                id = mgSvc.gridFsUploadStream(is , name, null, null, null)
            }
            return id != null
        }catch(Throwable t){
            exceptionManager.logException(null, t)
            return false
        }finally{
            if (is)
                is.close()
        }

    }
}
