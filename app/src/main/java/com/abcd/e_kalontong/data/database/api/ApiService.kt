package com.abcd.e_kalontong.data.database.api

import com.abcd.e_kalontong.data.model.AlamatModel
import com.abcd.e_kalontong.data.model.KecamatanModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
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

    @GET("e-kelontong/api/get.php")
    suspend fun getPesanan(
        @Query("get_pesanan") get_pesanan: String,
        @Query("id_user") id_user: Int,
    ): ArrayList<PesananModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getProduk(
        @Query("get_produk") get_produk: String,
    ): ArrayList<ProdukModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getAlamatUtama(
        @Query("get_pilih_alamat") get_pilih_alamat: String,
        @Query("id_user") id_user: Int,
    ): ArrayList<AlamatModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getAlamatUser(
        @Query("get_alamat") get_alamat: String,
        @Query("id_user") id_user: Int,
    ): ArrayList<AlamatModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getKecamatan(
        @Query("get_kecamatan") get_kecamatan: String,
    ): ArrayList<KecamatanModel>


    // POST

    // User
    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun addUser(
        @Field("add_user") addUser:String,
        @Field("nama") nama:String,
        @Field("kelas") kelas:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("nis") nis:String,
        @Field("password") password:String,
        @Field("sebagai") sebagai:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postTambahPesanan(
        @Field("tambah_pesanan") tambah_pesanan:String,
        @Field("id_user") id_user:Int,
        @Field("id_produk") id_produk:Int,
        @Field("jumlah") jumlah:String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postUpdatePesanan(
        @Field("update_pesanan") update_pesanan:String,
        @Field("id_produk") id_produk:Int,
        @Field("jumlah") jumlah:String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postDeletePesanan(
        @Field("delete_pesanan") delete_pesanan:String,
        @Field("id_produk") id_produk:Int,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postPesanCod(
        @Field("post_cod") post_cod:String,
        @Field("id_user") id_user: Int,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postRegistrasiPembayaran(
        @Field("post_register_pembayaran") post_register_pembayaran:String,
        @Field("kode_unik") kode_unik: String,
        @Field("id_user") id_user: Int,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postUpdateMainAlamat(
        @Field("update_main_alamat") update_main_alamat: String,
        @Field("id_alamat") id_alamat: String,
        @Field("id_user") id_user: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postTambahAlamat(
        @Field("tambah_pilih_alamat") tambah_pilih_alamat: String,
        @Field("id_user") id_user: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("id_kecamatan") id_kecamatan: String,
        @Field("detail_alamat") detail_alamat: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postUpdateAlamat(
        @Field("update_pilih_alamat") update_pilih_alamat: String,
        @Field("id_alamat") id_alamat: String,
        @Field("id_user") id_user: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("id_kecamatan") id_kecamatan: String,
        @Field("detail_alamat") detail_alamat: String,
    ): ArrayList<ResponseModel>

}