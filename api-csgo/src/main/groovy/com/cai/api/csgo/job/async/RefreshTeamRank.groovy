package com.cai.api.csgo.job.async

import com.cai.ais.annotation.ConsumerListener
import com.cai.ais.config.AisMessage
import com.cai.ais.config.AisService
import com.cai.api.csgo.domain.NetEntity
import com.cai.api.csgo.domain.TeamRankDomain
import com.cai.api.csgo.job.constants.JobConstants
import com.cai.api.csgo.job.service.TeamRankService
import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired

@ConsumerListener(queue = "api.refresh.team-rank", exchangeName = "api.csgo.team.refresh")
class RefreshTeamRank extends AisService<AisMessage> {

    @Autowired
    TeamRankService trSvc

    @Autowired
    MongoService mgSvc

    @Override
    Object process(AisMessage msg) {
        try{
            mgSvc.setDatabase(JobConstants.DB)
            Map value = msg.getBody() as Map
            toApiData(value, JobConstants.TeamRank.COLLECTION)
            null
        }catch(Throwable t){
            t.printStackTrace()
            trSvc.exceptionManager.logException(null , t)
        }
    }

    ResponseMessage toApiData(Map date, String collection) {
        TeamRankDomain domain = new TeamRankDomain()
        domain.team_name = date.team_name as String
        domain.team_tag = date.team_tag as String
        domain.team_country_id  = date.team_country_id as String
        domain.team_logo  = new NetEntity(domain.team_name as String, JobConstants.TeamRank.splitImageAddr + date.team_logo as String)
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
        // 先删除后插入
        mgSvc.deletedAndInsert(collection, filter, ConvertUtil.JSON.convertValue(domain,Document))
        InputStream is = new URL(domain.team_logo.addr).openStream()
        mgSvc.gridFsDeleteFileByName(domain.team_logo.name)
        mgSvc.gridFsUploadStream(is , domain.team_logo.name, 'logos', null, null)
        return ResponseMessageFactory.success()

    }
}
