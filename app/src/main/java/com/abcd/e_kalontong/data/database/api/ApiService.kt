package com.abcd.e_kalontong.data.database.api

import com.abcd.e_kalontong.data.model.AlamatModel
import com.abcd.e_kalontong.data.model.JenisProdukModel
import com.abcd.e_kalontong.data.model.KecamatanModel
import com.abcd.e_kalontong.data.model.PesananHalModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.data.model.RiwayatPesananHalModel
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.data.model.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("e-kelontong/api/get.php")
    suspend fun getUser(
        @Query("get_user") get_user: String,
        @Query("username") username: String,
        @Query("password") password: String,
    ): ArrayList<UserModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getKeranjangBelanja(
        @Query("get_keranjang_belanja") get_keranjang_belanja: String,
        @Query("id_user") id_user: Int,
    ): ArrayList<PesananModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getProduk(
        @Query("get_produk") get_produk: String,
    ): ArrayList<ProdukModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getAlamatUtama(
        @Query("get_alamat_utama") get_alamat_utama: String,
        @Query("id_user") id_user: Int,
    ): ArrayList<AlamatModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getAlamatUser(
        @Query("get_pilih_alamat") get_pilih_alamat: String,
        @Query("id_user") id_user: Int,
    ): ArrayList<AlamatModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getKecamatan(
        @Query("get_kecamatan") get_kecamatan: String,
    ): ArrayList<KecamatanModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getPesanan(
        @Query("get_pesanan") get_pesanan: String,
        @Query("id_user") idUser: String
    ): ArrayList<RiwayatPesananModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getRiwayatPesanan(
        @Query("get_riwayat_pesanan") get_riwayat_pesanan: String,
        @Query("id_user") idUser: String
    ): ArrayList<RiwayatPesananModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getJenisProduk(
        @Query("get_jenis_produk") get_jenis_produk: String
    ): ArrayList<JenisProdukModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getAllProduk(
        @Query("get_all_produk") getAllProduk: String
    ): ArrayList<ProdukModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getAdminKeranjangBelanja(
        @Query("get_admin_keranjang_belanja") get_admin_keranjang_belanja: String,
    ): ArrayList<PesananHalModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getAllUser(
        @Query("get_admin_all_user") get_admin_all_user: String,
    ): ArrayList<UserModel>

    @GET("e-kelontong/api/get.php")
    suspend fun getAdminRiwayatPesanan(
        @Query("get_admin_riwayat_pesanan") get_admin_riwayat_pesanan: String,
    ): ArrayList<RiwayatPesananHalModel>


    // POST

    // User
    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun addUser(
        @Field("add_user") addUser:String,
        @Field("nama") nama:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("sebagai") sebagai:String
    ): ResponseModel

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
        @Field("id_pesanan") id_pesanan:Int,
        @Field("jumlah") jumlah:String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postDeletePesanan(
        @Field("delete_pesanan") delete_pesanan:String,
        @Field("id_pesanan") id_pesanan:Int,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postPesanCod(
        @Field("post_cod") post_cod:String,
        @Field("id_user") id_user: Int,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postRegistrasiPembayaran(
        @Field("post_register_pembayaran") post_register_pembayaran:String,
        @Field("kode_unik") kode_unik: String,
        @Field("id_user") id_user: Int,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postUpdateMainAlamat(
        @Field("update_main_alamat") update_main_alamat: String,
        @Field("id_alamat") id_alamat: String,
        @Field("id_user") id_user: String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postTambahAlamat(
        @Field("tambah_pilih_alamat") tambah_pilih_alamat: String,
        @Field("id_user") id_user: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("id_kecamatan") id_kecamatan: String,
        @Field("detail_alamat") detail_alamat: String,
    ): ResponseModel

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
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postUpdateUser(
        @Field("update_akun") updateAkun:String,
        @Field("id_user") idUser: String,
        @Field("nama") nama:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("username_lama") usernameLama: String
    ): ResponseModel

    // Post Jenis Produk
    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postTambahJenisProduk(
        @Field("tambah_jenis_produk") tambahJenisProduk: String,
        @Field("jenis_produk") jenis_produk: String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postUpdateJenisProduk(
        @Field("update_jenis_produk") updateJenisProduk: String,
        @Field("id_jenis_produk") id_jenis_produk: String,
        @Field("jenis_produk") jenis_produk: String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postDeleteJenisProduk(
        @Field("delete_jenis_produk") delete_jenis_produk:String,
        @Field("id_jenis_produk") id_jenis_produk: String
    ): ResponseModel


    // Post Produk
    @Multipart
    @POST("e-kelontong/api/post.php")
    suspend fun postTambahProduk(
        @Part("tambah_admin_produk") tambah_admin_produk: RequestBody,
        @Part("id_jenis_produk") id_jenis_produk: RequestBody,
        @Part("produk") produk: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part gambar: MultipartBody.Part,
    ): ResponseModel

    @Multipart
    @POST("e-kelontong/api/post.php")
    suspend fun postUpdateProduk(
        @Part("update_admin_produk") update_admin_produk: RequestBody,
        @Part("id_produk") id_produk: RequestBody,
        @Part("id_jenis_produk") id_jenis_produk: RequestBody,
        @Part("produk") produk: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("stok") stok: RequestBody,
        @Part gambar: MultipartBody.Part,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postUpdateProdukNoImage(
        @Field("update_produk_no_image") update_produk_no_image:String,
        @Field("id_produk") id_produk:String,
        @Field("id_jenis_produk") id_jenis_produk:String,
        @Field("produk") produk: String,
        @Field("harga") harga:String,
        @Field("stok") stok: String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postDeleteProduk(
        @Field("delete_admin_produk") delete_admin_produk:String,
        @Field("id_produk") id_produk:Int
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postAdminTambahPesanan(
        @Field("tambah_admin_pesanan") tambah_admin_pesanan:String,
        @Field("id_user") id_user:String,
        @Field("id_produk") id_produk:String,
        @Field("jumlah") jumlah:String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postAdminUpdatePesanan(
        @Field("update_admin_pesanan") update_admin_pesanan:String,
        @Field("id_pesanan") id_pesanan:String,
        @Field("id_user") id_user:String,
        @Field("id_produk") id_produk:String,
        @Field("jumlah") jumlah:String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postAdminDeletePesanan(
        @Field("delete_admin_pesanan") delete_admin_pesanan:String,
        @Field("id_pesanan") id_pesanan:String,
    ): ResponseModel

    @FormUrlEncoded
    @POST("e-kelontong/api/post.php")
    suspend fun postAdminHapusUser(
        @Field("admin_delete_user") admin_delete_user:String,
        @Field("id_user") idUser: String
    ): ResponseModel

}