package com.example.masterproject.sources.base

import com.example.masterproject.model.AppException
import com.example.masterproject.model.BackendException
import com.example.masterproject.model.ConnectionException
import com.example.masterproject.model.ParseBackendResponseException
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import retrofit2.HttpException
import java.io.IOException
import java.sql.Blob

open class BaseRetrofitSource(
    retrofitConfig: RetrofitConfig
) {
    val retrofit = retrofitConfig.retrofit

    private val errorAdapter =
        retrofitConfig.moshi.adapter(ErrorResponseBody::class.java)

    suspend fun<T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        }catch (e: JsonDataException){
            throw ParseBackendResponseException(e)
        }catch (e: JsonEncodingException){
            throw ParseBackendResponseException(e)
        }catch (e: HttpException){
            throw createBackendException(e)
        }catch (e: IOException){
            throw ConnectionException(e)
        }
    }

    private fun createBackendException(e: HttpException): Exception{
        return try {
            val errorBody = errorAdapter.fromJson(
                e.response()!!.errorBody()!!.string())!!
            BackendException(errorBody.code, errorBody.msg)
        }catch (e: Exception){
            throw ParseBackendResponseException(e)
        }
    }

    class ErrorResponseBody(
        val code: Int,
        val msg: String
    )
}