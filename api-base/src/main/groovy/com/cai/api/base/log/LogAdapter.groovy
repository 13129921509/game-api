package com.cai.api.base.log

interface LogAdapter<E extends Log, C> extends LogHelper<E>, FileWriter<E, C>{

}
