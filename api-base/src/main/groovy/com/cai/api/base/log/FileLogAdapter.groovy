package com.cai.api.base.log

import com.cai.general.util.jackson.ConvertUtil

class FileLogAdapter<T extends LogHelper, E extends Log> implements FileWriter<E>{

    BufferedWriter osw

    OutputStream os

    T logHelper

    FileLogAdapter(OutputStream os, T logHelper) {
        this.os = os
        this.logHelper = logHelper
        osw = new BufferedWriter(new OutputStreamWriter(os))
    }

    FileLogAdapter(String path , LogHelper logHelper){
        this.logHelper = logHelper
        File file = new File(path)
        if (!file.exists())
            file.createNewFile()
        os = file.newObjectOutputStream()
        osw = new BufferedWriter(new OutputStreamWriter(os))
    }

    @Override
    void writer(E log) {
        osw.write(insertLogAndConvert(log).toString() + "\n\r")
        osw.flush()
    }

    @Override
    Map insertLogAndConvert(E log) {
        logHelper.insertLog(log as Log)
        return ConvertUtil.JSON.convertValue(log, Map)
    }
}
