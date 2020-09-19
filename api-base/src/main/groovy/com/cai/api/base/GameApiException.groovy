package com.cai.api.base

class GameApiException extends Exception{

    String msg

    Throwable t

    GameApiException(String msg) {
        super(msg)
        this.msg = msg
    }


    GameApiException(Throwable t, String msg) {
        super(msg, t)
        this.msg = msg
    }
}
