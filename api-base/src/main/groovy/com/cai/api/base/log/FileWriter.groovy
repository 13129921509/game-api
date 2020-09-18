package com.cai.api.base.log

interface FileWriter<B, C>{

    void writer(C vals)

    C convert(B obj, Class<C> clazz)
}
