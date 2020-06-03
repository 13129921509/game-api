package com.cai.api.csgo.job.service

import com.cai.api.base.BaseService
import com.cai.api.base.domain.ApiLog
import com.cai.api.base.log.MongoLogHelper
import com.cai.api.csgo.job.constants.JobConstants
import com.cai.general.util.http.HttpUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.text.MessageFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ApiService extends BaseService{

    Logger log = LoggerFactory.getLogger(ApiService.class)

    @Autowired
    MongoLogHelper<ApiLog> logHelper

    @Override
    ResponseMessage refresh(){
        doRefresh()
        return ResponseMessageFactory.success()
    }

    @Override
    ResponseMessage afterRefresh() {

        return ResponseMessageFactory.success()
    }

    private Object doRefresh(){
        refreshTeamRank()
    }

    /***
     * 刷新战队排行
     * @return
     */
    private ResponseMessage refreshTeamRank(){
        String resourceUrl = JobConstants.TeamRank.teamRankTableResource
        List<ApiLog> logs = []
        String url
        String message
        insertLog(logHelper, logs){
            JobConstants.TeamRank.maxPage.times {
                message = null
                try{
                    url = MessageFormat.format(resourceUrl, it)
                    Map result = HttpUtil.getToEntity(url, Map.class)
                    if (!result.data) return
                    List data = result.data as List
                    data.each {Map value->
                        value.put('created', LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
                        mgSvc.insertWithExpireDays(db, JobConstants.TeamRank.COLLECTION, value, 30)
                    }
                    log.error("${LocalDate.now()}---$url end")
                    return ResponseMessageFactory.success()
                }catch(Throwable t){
                    // todo 暂不处理
                    t.printStackTrace()
                    message = t.message
                    return ResponseMessageFactory.error(t.message)
                }finally{
                    ApiLog apiLog = new ApiLog()
                    apiLog.api = JobConstants.TeamRank.API_NAME
                    apiLog.msg = message?message:"success"
                    apiLog.url = url
                    logs.add(apiLog)
                }
            }
            return ResponseMessageFactory.success()
        }
    }



}
