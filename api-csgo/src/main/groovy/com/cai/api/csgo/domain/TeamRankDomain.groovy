package com.cai.api.csgo.domain

import com.cai.api.base.domain.BaseApiDomain
import com.cai.api.csgo.job.constants.JobConstants
import com.cai.general.core.EntityType
import org.springframework.stereotype.Component

@Component
class TeamRankDomain extends BaseApiDomain{

    static DEFINE = define([
            table: JobConstants.TeamRank.COLLECTION,
            cache: true,
            db: JobConstants.DB,
            type: EntityType.MONGO
    ])

    String team_name

    String team_tag

    String team_country_id

    NetEntity team_logo

    String rank

    String point

    String bonus

    String rating

    String match_total //比赛地图数目

    String kd

    String win_rate //胜率

    String dateline

    String cup1

    String cup2

    String cup3

    @Override
    Object getEntityId() {
        return team_name
    }

    @Override
    String getCacheKey() {
        return "$DEFINE.table:$team_name"
    }
}
