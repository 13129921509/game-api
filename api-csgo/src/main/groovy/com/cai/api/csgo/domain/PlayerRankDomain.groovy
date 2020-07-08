package com.cai.api.csgo.domain

import com.cai.api.base.domain.BaseApiDomain
import com.cai.api.csgo.job.constants.JobConstants
import com.cai.general.core.EntityType
import org.springframework.stereotype.Component

@Component
class PlayerRankDomain extends BaseApiDomain{

    static DEFINE = define([
            table: JobConstants.PlayerRank.COLLECTION,
            cache: true,
            db: JobConstants.DB,
            type: EntityType.MONGO
    ])
    Long rank

    String player_tag

    String player_alias

    String player_country_id

    String player_avatar_url

    NetEntity player_photo_url

    String rating

    String major_rating

    Long   kill

    String per_headshot //爆头比率

    String cup1

    String cup2

    String cup3

    Long mvp_total

    String team_name

    String team_tag

    String team_alias

    String team_country_id

    NetEntity team_logo

    String dateline
    @Override
    Object getEntityId() {
        return player_tag
    }

    @Override
    String getCacheKey() {
        return "$DEFINE.table:$player_tag"
    }
}
