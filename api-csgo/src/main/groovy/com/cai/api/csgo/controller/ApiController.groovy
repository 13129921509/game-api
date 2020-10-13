package com.cai.api.csgo.controller

import com.cai.api.csgo.service.TeamRankService
import com.cai.general.core.BaseController
import com.cai.general.util.response.ResponseMessage
import com.cai.web.core.IgnoreAuth
import com.cai.web.core.IgnoreAuthStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(path = "/api/csgo")
class ApiController extends BaseController{

    @Autowired
    TeamRankService trSvc

    @Autowired
    IgnoreAuthStore iaStore

    @IgnoreAuth
    @RequestMapping(path = "/teamRank", method = RequestMethod.GET)
    ResponseMessage getTeamRank(HttpServletRequest request, @RequestParam String page, @RequestParam String row){
        println request.getServletPath()
        println iaStore.getStore()
        return trSvc.getTeamRank(page as Integer, row as Integer)
    }
}
