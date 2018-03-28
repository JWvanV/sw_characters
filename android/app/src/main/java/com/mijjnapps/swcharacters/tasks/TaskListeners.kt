package com.mijjnapps.swcharacters.tasks

interface ApiListResponseListener<in T> {
    fun onSuccess(result: List<T>)
    fun onFail()
}

interface DbListResponseListener<in T> {
    fun onSuccess(result: List<T>)
}

interface ApiResponseListener<in T> {
    fun onSuccess(result: T)
    fun onFail()
}

interface DbResponseListener<in T> {
    fun onSuccess(result: T)
}