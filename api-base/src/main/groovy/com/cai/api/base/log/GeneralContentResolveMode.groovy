package com.cai.api.base.log

@Deprecated
class GeneralContentResolveMode<T> extends ContentResolveMode<T>{
    List<String> words = []

    List<String> res = []

    @Override
    String getResult(T pattern) {
        words = ((String)pattern).split("\$")
        words.each {it->
            res.add(ContentResolveData.getField(it) as String)
        }
        return words.join("\\-")
    }
}
