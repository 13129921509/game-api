package com.cai.api.base.log

interface LogHelper<T extends Log>{
    /**
     * 插入日志
     * @param log
     * @return
     */
    def boolean insertLog(T log)
}
