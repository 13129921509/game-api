package com.cai.api.csgo.job.constants

class JobConstants {
    final static String DB = "api-csgo"
    class TeamRank {
        final static String API_NAME = 'csgo_team_rank'
        final static String ORIGIN_COLLECTION = 'origin_csgo_team_rank'
        final static String COLLECTION = 'csgo_team_rank'

        static final String splitImageAddr = 'https://oss.5eplay.com/'
        //战队排名来源
        static final String teamRankTableResource = "https://csgo.5eplay.com/api/data/rank/team?page={0}&field=&range="

        static Integer maxPage = 50
    }


    class ChinaTeamRank {
        final static String API_NAME = 'csgo_china_team_rank'
        final static String ORIGIN_COLLECTION = 'origin_csgo_china_team_rank'
        final static String COLLECTION = 'csgo_china_team_rank'

        static final String splitImageAddr = 'https://oss.5eplay.com/'
        //战队排名来源
        static final String teamRankTableResource = "https://csgo.5eplay.com/api/data/rank/team?page={0}&field=&range=cn"

        static Integer maxPage = 50
    }

    class PlayerRank {
        final static String API_NAME = 'player_rank'
        final static String ORIGIN_COLLECTION = 'origin_player_rank'
        final static String COLLECTION = 'player_rank'

        static final String splitImageAddr = 'https://oss.5eplay.com/'
        //战队排名来源
        static final String teamRankTableResource = "https://csgo.5eplay.com/api/data/rank/player?page={0}&range=&major=1"

        static Integer maxPage = 30
    }

}
