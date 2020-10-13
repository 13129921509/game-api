package com.cai.api.csgo.job.controller

import com.cai.api.base.JobAction
import com.cai.api.csgo.job.service.ChinaTeamRankJobService
import com.cai.api.csgo.job.service.PlayerRankJobService
import com.cai.api.csgo.job.service.TeamRankJobService
import com.cai.api.csgo.message.ApiMessage
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.web.core.IgnoreAuth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * 通过scheduler定时更新信息
 *
 * scheduler/api => (scheduler -> api)
 */
@RestController
@RequestMapping('/scheduler/api')
class JobController implements JobAction{

    @Autowired
    TeamRankJobService trSvc

    @Autowired
    ChinaTeamRankJobService ctrSvc

    @Autowired
    PlayerRankJobService prSvc

    @Override
    @IgnoreAuth
    @RequestMapping(method = RequestMethod.POST, value = '/refresh')
    ResponseMessage refresh() {
        return doFresh()
    }

    ResponseMessage doFresh(){
        ResponseMessage rsp = ResponseMessageFactory.success()
        try {
            if (!(rsp = trSvc.refresh()).isSuccess)
                return rsp
            if (!(rsp = ctrSvc.refresh()).isSuccess)
                return rsp
            if (!(rsp = prSvc.refresh()).isSuccess)
                return rsp
            return rsp
        }catch(Throwable t){
            return ResponseMessageFactory.error(ApiMessage.ERROR.MSG_ERROR_0001)
        }
    }
}
