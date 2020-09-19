package com.cai.api.base.log

import com.cai.api.base.GameApiException
import com.cai.general.util.jackson.ConvertUtil
import com.cai.redis.RedisLockService
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FileLogAdapter<E extends Log, C> implements LogAdapter<E, C>{

    private BufferedWriter osw

    private volatile String fileName

    private String prefix

    private ContentResolveMode<String> contentResolveMode

    private File file

    private String path

    private long maxFileSize

    private NextFileName nf = new NextFileName()

    private RedisLockService relSvc

    private LogHelper<E> logHelper

    final private static String CREATE_LOG_FILE = "sys:op:createLogFile"

    Class<C> convertType

    FileLogAdapter(RedisLockService relSvc
                   , String path
                   , String prefix
                   , LogHelper logHelper
                   , long maxFileSize
                   , Class convertType){
        this.relSvc = relSvc
        this.path = path
        this.prefix = prefix
        this.logHelper = logHelper
        this.maxFileSize = maxFileSize
        this.convertType = convertType
        init()
    }

    @Override
    boolean insertLog(E log) {
        writer(convert(log, convertType))
        return logHelper.insertLog(log)
    }

    void writer(C vals) {
        try {
            beforeWriter()
            osw.append(vals.toString() + "\n")
            osw.flush()
        }catch(Throwable t){
            t.printStackTrace()
        }
    }

    C convert(E obj, Class<C> clazz) {
        return ConvertUtil.JSON.convertValue(obj, clazz)
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
        try{
            if (!file.exists())
                file.createNewFile()
            osw = new BufferedWriter(new java.io.FileWriter(file, true))
        }catch(Throwable e){
            if (e instanceof IOException)
                throw new GameApiException("路径: $file.parent 不存在")
            else
                throw e
        }

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
