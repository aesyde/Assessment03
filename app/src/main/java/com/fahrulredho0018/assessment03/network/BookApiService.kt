package com.fahrulredho0018.assessment03.network

import com.fahrulredho0018.assessment03.model.OpStatus
import com.fahrulredho0018.assessment03.model.Book
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

private const val BASE_URL = "https://apimobpro-production.up.railway.app/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BookApiService {
    @GET("books")
    suspend fun getBooks(
        @Header("Authorization") userId: String
    ): List<Book>

    @Multipart
    @POST("books")
    suspend fun postBooks(
        @Header("Authorization") userId: String,
        @Part("title") title: RequestBody,
        @Part("author") author: RequestBody,
        @Part("publisher") publisher: RequestBody,
        @Part("year") year: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @Multipart
    @POST("books/update/{id}")
    suspend fun updateBooks(
        @Header("Authorization") userId: String,
        @Part("title") title: RequestBody,
        @Part("author") author: RequestBody,
        @Part("publisher") publisher: RequestBody,
        @Part("year") year: RequestBody,
        @Part image: MultipartBody.Part,
        @Path("id") id : String
    ): OpStatus

    @DELETE("books/delete/{id}")
    suspend fun deleteBooks(
        @Path("id") id: String
    ): OpStatus
}

object BookApi {
    val service: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }

    fun getBookUrl(imageId: String): String {
        return "https://apimobpro-production.up.railway.app/storage/$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }