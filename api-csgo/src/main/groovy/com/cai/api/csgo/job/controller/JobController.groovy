package com.cai.api.csgo.job.controller

import com.cai.api.base.JobAction
import com.cai.api.csgo.job.service.TeamRankService
import com.cai.general.util.response.ResponseMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * 通过scheduler定时更新信息
 *
 * scheduler/api => (scheduler -> api)
 */
@RestController
@RequestMapping('scheduler/api')
class JobController implements JobAction{

    @Autowired
    TeamRankService apiSvc

    @Override
    @RequestMapping(method = RequestMethod.POST, value = '/refresh')
    ResponseMessage refresh() {
        return apiSvc.refresh()
    }
}
