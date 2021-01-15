package com.cai.api.csgo.service

import com.cai.api.base.BaseService
import com.cai.api.csgo.job.constants.JobConstants
import com.cai.general.util.response.ResponseMessage
import com.cai.general.util.response.ResponseMessageFactory
import com.cai.mongo.service.MongoDataPaginationPlugin
import com.cai.mongo.service.criteria.CollectionSplitCriteriaBuilder
import org.bson.Document
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class PlayRankService extends BaseService{
    @Override
    ResponseMessage afterRefresh() {
        return null
    }

    ResponseMessage getPlayRank(int page, int row, List<Map<String,Object>> param){
        List<Document> data = mgSvc.findListAndPageable(JobConstants.DB,JobConstants.PlayerRank.COLLECTION, new Query(CollectionSplitCriteriaBuilder.getCriteria(param)), new MongoDataPaginationPlugin(page, row))
        return ResponseMessageFactory.success("success", data)
    }

    @Override
    void setDb() {
        changeDb(JobConstants.DB)
    }
}
