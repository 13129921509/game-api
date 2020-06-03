package com.cai.api.csgo.job.controller

import com.cai.api.base.JobAction
import com.cai.api.csgo.job.service.ApiService
import com.cai.general.util.response.ResponseMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
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
    ApiService apiSvc

    @Override
    @RequestMapping('/refresh')
    ResponseMessage refresh() {
        return apiSvc.refresh()
    }
}
