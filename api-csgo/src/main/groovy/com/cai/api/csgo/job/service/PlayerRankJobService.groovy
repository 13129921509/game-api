package com.cai.api.csgo.job.service

import com.cai.ais.config.AisMessage
import com.cai.api.base.BaseService
import com.cai.api.base.domain.ApiLog
import com.cai.api.base.log.LogHelper
import com.cai.api.base.log.MongoLogHelper
import com.cai.api.csgo.job.constants.JobConstants
import com.cai.general.util.http.HttpUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.text.MessageFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.IntStream

@Service
class PlayerRankJobService extends BaseService{

    Logger log = LoggerFactory.getLogger(PlayerRankJobService.class)

    @Override
    ResponseMessage refresh() {
        return doRefresh()
    }

    @Override
    void setDb() {
        changeDb(JobConstants.DB)
    }

    @Override
    ResponseMessage afterRefresh() {
        return ResponseMessageFactory.success()
    }

    private Object doRefresh(){
        refreshPlayerRank()
    }

    private ResponseMessage refreshPlayerRank(){
        String resourceUrl = JobConstants.PlayerRank.teamRankTableResource
        List<ApiLog> logs = []
        String url
        String message
        this.insertLog(logs){
            JobConstants.PlayerRank.maxPage.times{
                message = null
                try{
                    url = MessageFormat.format(resourceUrl, it)
                    Map result = HttpUtil.getToEntity(url, Map.class)
                    if (!result.data) return
                    List data = result.data as List
                    data.each {Map value->
                        value.put('created', LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
                        mgSvc.insertWithExpireDays(db, JobConstants.PlayerRank.ORIGIN_COLLECTION, value, 30)
                        AisMessage msg = new AisMessage()
                        msg.setBody(value)
                        // 做异步处理
                        aisSend.send(msg, "api.csgo.player.refresh")
//                        toApiData(value, JobConstants.TeamRank.COLLECTION)
                    }
                    log.error("${LocalDate.now()}---$url end")
                    return ResponseMessageFactory.success()
                }catch(Throwable t){
                    // todo 暂不处理
                    t.printStackTrace()
                    exceptionManager.logException(null, t)
                    return ResponseMessageFactory.error(t.message)
                }finally{
                    ApiLog apiLog = new ApiLog()
                    apiLog.api = JobConstants.PlayerRank.API_NAME
                    apiLog.msg = message?message:"success"
                    apiLog.url = url
                    logs.add(apiLog)
                }
            }
            return ResponseMessageFactory.success()
        }
    }
}