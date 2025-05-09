package com.abcd.e_kalontong.data.database.api

import com.abcd.e_kalontong.data.model.UserModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("e-kelontong/api/get.php")
    suspend fun getUser(
        @Query("get_user") get_user: String,
        @Query("username") username: String,
        @Query("password") password: String,
    ): ArrayList<UserModel>

    // POST

    // Siswa
//    @FormUrlEncoded
//    @POST("absensi-sman-5/api/post.php")
//    suspend fun addUser(
//        @Field("add_user") addUser:String,
//        @Field("nama") nama:String,
//        @Field("kelas") kelas:String,
//        @Field("nomor_hp") nomorHp:String,
//        @Field("nis") nis:String,
//        @Field("password") password:String,
//        @Field("sebagai") sebagai:String
//    ): ArrayList<ResponseModel>

}