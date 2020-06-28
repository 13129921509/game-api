package com.cai.api.base.log

interface FileWriter<E> {

    void writer(E vals)

    Map insertLogAndConvert(E obj)
}
