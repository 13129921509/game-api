package com.cai.api.csgo.job.service

import com.cai.api.base.BaseService
import com.cai.api.base.domain.ApiLog
import com.cai.api.base.log.MongoLogHelper
import com.cai.api.csgo.domain.TeamRankDomain
import com.cai.api.csgo.job.constants.JobConstants
import com.cai.general.util.http.HttpUtil
import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import com.google.common.collect.Lists
import com.mongodb.client.FindIterable
import com.mongodb.client.result.UpdateResult
import jdk.nashorn.internal.objects.annotations.Constructor
import org.bson.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

import javax.annotation.PostConstruct
import java.text.MessageFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TeamRankService extends BaseService{

    Logger log = LoggerFactory.getLogger(TeamRankService.class)

    @Autowired
    MongoLogHelper<ApiLog> logHelper

    @Override
    ResponseMessage refresh(){
        doRefresh()
        return ResponseMessageFactory.success()
    }

    @Override
    ResponseMessage toApiData(Map date, String collection) {
        TeamRankDomain domain = new TeamRankDomain()
        domain.team_name = date.team_name as String
        domain.team_tag = date.team_tag as String
        domain.team_country_id  = date.team_country_id as String
        domain.team_logo  = date.team_logo as String
        domain.rank  = date.rank as String
        domain.point  = date.point as String
        domain.bonus  = date.bonus as String
        domain.rating  = date.rating as String
        domain.match_total  = date.match_total as String
        domain.kd  = date.kd as String
        domain.win_rate  = date.win_rate as String
        domain.dateline  = date.dateline as String
        Map cups = date.cups
        domain.cup1  = cups.get("1") as String
        domain.cup2  = cups.get("2") as String
        domain.cup3  = cups.get("3") as String

        Document filter = new Document()
        filter.append("team_name", domain.team_name)
        mgSvc.delete(collection, filter)
        mgSvc.insert(collection, ConvertUtil.JSON.convertValue(domain,Document))
        return ResponseMessageFactory.success()

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
        this.insertLog(logHelper, logs){
            JobConstants.TeamRank.maxPage.times {
                message = null
                try{
                    url = MessageFormat.format(resourceUrl, it)
                    Map result = HttpUtil.getToEntity(url, Map.class)
                    if (!result.data) return
                    List data = result.data as List
                    data.each {Map value->
                        value.put('created', LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
                        mgSvc.insertWithExpireDays(db, JobConstants.TeamRank.ORIGIN_COLLECTION, value, 30)
                        toApiData(value, JobConstants.TeamRank.COLLECTION)
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
