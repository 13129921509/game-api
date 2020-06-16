package com.cai.api.csgo.job.async

import com.cai.ais.AisMessage
import com.cai.ais.AisService
import com.cai.ais.annotation.ConsumerListener
import com.cai.api.csgo.job.constants.JobConstants
import com.cai.api.csgo.job.service.TeamRankService
import org.springframework.beans.factory.annotation.Autowired

@ConsumerListener(queue = "api.refresh.team-rank", exchangeName = "api.csgo.team.refresh")
class RefreshTeamRank extends AisService<AisMessage>{

    @Autowired
    TeamRankService trSvc

    @Override
    void process(AisMessage msg) {
        try{
            Map value = msg.getBody() as Map
            trSvc.toApiData(value, JobConstants.TeamRank.COLLECTION)
        }catch(Throwable t){
            t.printStackTrace()
            trSvc.exceptionManager.logException(null , t)
        }

    }
}
