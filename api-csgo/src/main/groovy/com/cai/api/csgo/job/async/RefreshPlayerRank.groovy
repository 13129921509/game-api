package com.cai.api.csgo.job.async

import com.cai.ais.annotation.ConsumerListener
import com.cai.ais.config.AisMessage
import com.cai.ais.config.AisService
import com.cai.api.csgo.domain.NetEntity
import com.cai.api.csgo.domain.PlayerRankDomain
import com.cai.api.csgo.job.constants.JobConstants
import com.cai.api.csgo.job.service.PlayerRankJobService
import com.cai.general.util.jackson.ConvertUtil
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoService
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired

@ConsumerListener(queue = "api.refresh.player.rank", exchangeName = "api.csgo.player.refresh")
class RefreshPlayerRank extends AisService<AisMessage> {

    @Autowired
    PlayerRankJobService prSvc

    @Autowired
    MongoService mgSvc

    @Override
    Object process(AisMessage msg) {
        try{
            mgSvc.setDatabase(JobConstants.DB)
            Map value = msg.getBody() as Map
            toApiData(value, JobConstants.PlayerRank.COLLECTION)
            null
        }catch(Throwable t){
            t.printStackTrace()
            prSvc.exceptionManager.logException(null , t)
        }
    }

    ResponseMessage toApiData(Map date, String collection) {
        PlayerRankDomain domain = new PlayerRankDomain()
        domain.team_name = date.team.team_name as String
        domain.team_tag = date.team.team_tag as String
        domain.team_country_id  = date.team_country_id as String
        domain.team_logo  = new NetEntity(domain.team_name as String, JobConstants.PlayerRank.splitImageAddr + date.team_logo as String)
        domain.team_alias = date.team.team_alias as String
        domain.rank  = date.rank as Long
        domain.rating  = date.rating as String
        domain.dateline  = date.dateline as String
        Map cups = date.cups
        domain.cup1  = cups.get("1") as String
        domain.cup2  = cups.get("2") as String
        domain.cup3  = cups.get("3") as String

        domain.player_tag = date.player_tag
        domain.player_alias      = date.player_alias
        domain.player_country_id = date.player_country_id
        domain.player_avatar_url = date.player_avatar_url
        domain.player_photo_url  = new NetEntity(domain.player_tag as String, JobConstants.PlayerRank.splitImageAddr + date.player_photo_url as String)

        domain.major_rating = date.major_rating
        domain.kill= date.kill as Long
        domain.per_headshot= date.per_headshot
        domain.mvp_total = date.mvp_total as Long
        Document filter = new Document()
        filter.append("player_tag", domain.player_tag)
        // 先删除后插入
        mgSvc.deletedAndInsert(collection, filter, ConvertUtil.JSON.convertValue(domain,Document))
        //team图标保存
        prSvc.gridFsDeletedAndUploadStream(domain.team_logo.name, domain.team_logo.addr, 'logos')
        //player头像保存
        prSvc.gridFsDeletedAndUploadStream(domain.player_photo_url.name, domain.player_photo_url.addr, 'players')

        return ResponseMessageFactory.success()

    }
}
