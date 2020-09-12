package com.cai.api.base.log

import com.cai.general.util.jackson.ConvertUtil
import com.cai.redis.RedisLockService
import com.cai.redis.RedisService

import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FileLogAdapter<T extends LogHelper, E extends Log> implements FileWriter<E>{

    private BufferedWriter osw

    T logHelper

    private volatile String fileName

    private String prefix

    private ContentResolveMode<String> contentResolveMode

    private File file

    private String path

    private long maxFileSize

    private NextFileName nf = new NextFileName()

    private RedisLockService relSvc

    final private static String CREATE_LOG_FILE = "sys:op:createLogFile"

    FileLogAdapter(RedisLockService relSvc
                   , String path
                   , String prefix
                   , LogHelper logHelper
                   , long maxFileSize){
        this.relSvc = relSvc
        this.path = path
        this.prefix = prefix
        this.logHelper = logHelper
        this.maxFileSize = maxFileSize
        init()
    }

    @Override
    void writer(E log) {
        try{
            beforeWriter()
            osw.append(insertLogAndConvert(log).toString() + "\n")
            osw.flush()
        }catch(Throwable t){
            t.printStackTrace()
        }
    }

    private void beforeWriter(){
        relSvc.tryOpLock(CREATE_LOG_FILE, {
            // 验证文件时间参数是否过期
            fileName = nf.getFileNameByExpired(prefix)
            buildAndOpenFileStream()
            // 验证文件大小是否超过限定值
            while (file.length() >= maxFileSize){
                fileName = nf.getFileNameByNextNumber(prefix)
                this.file = new File(path + fileName + ".txt")
            }
            this.file = new File(path + fileName + ".txt")
            buildAndOpenFileStream()
        })
    }


    @Override
    Map insertLogAndConvert(E log) {
        logHelper.insertLog(log as Log)
        return ConvertUtil.JSON.convertValue(log, Map)
    }

    void finalize() throws Throwable {
        super.finalize()
        if (osw)
            osw.close()
    }


    private void init(){
        try{
            buildFileName()
            buildAndOpenFileStream()
        }catch(IOException e){
            e.printStackTrace()
        }

    }


    private String buildFileName(){
        fileName = nf.getFileNameByNextNumber(prefix)
        this.file = new File(path + fileName + ".txt")
    }

    private void buildAndOpenFileStream(){
        if (!file.exists())
            file.createNewFile()
        osw = new BufferedWriter(new java.io.FileWriter(file, true))
    }

    private class NextFileName{

        long number = 0

        String data = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        LocalDate ld = LocalDate.now()

        List<String> words = []

        String fileName

        String getFileNameByExpired(String content){
            if (LocalDate.now().isAfter(ld)){
                number = 1
                data = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                fillWords(content)
                fileName = getFileName()
                return fileName
            }
            return fileName
        }

        String getFileNameByNextNumber(String content){
            number ++
            fillWords(content)
            fileName = getFileName()
            return fileName
        }

        void fillWords(String content){
            words.clear()
            words.add(content)
            words.add(data)
            words.add(number as String)
        }

        private String getFileName(){
            return words.join("-")
        }

        boolean isExpires(){

        }
    }
}
