package com.cai.api.csgo.controller

import com.cai.api.csgo.service.ChinaTeamRankService
import com.cai.api.csgo.service.PlayRankService
import com.cai.api.csgo.service.TeamRankService
import com.cai.general.core.BaseController
import com.cai.general.core.Session
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
    ChinaTeamRankService ctrSvc

    @Autowired
    PlayRankService prSvc

    @IgnoreAuth
    @RequestMapping(path = "/teamRank", method = RequestMethod.POST)
    ResponseMessage getTeamRank(HttpServletRequest request, @RequestBody Map map){
        return trSvc.getTeamRank(map.page as Integer, map.row as Integer, map.param as List<Map<String,Object>>)
    }

    @IgnoreAuth
    @RequestMapping(path = "/chinaTeamRank", method = RequestMethod.POST)
    ResponseMessage getChinaTeamRank(HttpServletRequest request, @RequestBody Map map){
        return ctrSvc.getChinaTeamRank(map.page as Integer, map.row as Integer, map.param as List<Map<String,Object>>)
    }

    @IgnoreAuth
    @RequestMapping(path = "/playRank", method = RequestMethod.POST)
    ResponseMessage getPlayRank(HttpServletRequest request, @RequestBody Map map){
        return prSvc.getPlayRank(map.page as Integer, map.row as Integer, map.param as List<Map<String,Object>>)
    }
}
