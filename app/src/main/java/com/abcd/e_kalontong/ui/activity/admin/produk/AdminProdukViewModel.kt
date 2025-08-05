package com.abcd.e_kalontong.ui.activity.admin.produk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.JenisProdukModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AdminProdukViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _jenisProduk = MutableLiveData<UIState<ArrayList<JenisProdukModel>>>()
    private var _produk = MutableLiveData<UIState<ArrayList<ProdukModel>>>()
    private var _postTambahProduk = MutableLiveData<UIState<ResponseModel>>()
    private var _postUpdateProduk = MutableLiveData<UIState<ResponseModel>>()
    private var _postUpdateProdukNoImage = MutableLiveData<UIState<ResponseModel>>()
    private var _postDeleteProduk = MutableLiveData<UIState<ResponseModel>>()

    fun fetchJenisProduk() {
        viewModelScope.launch(Dispatchers.IO) {
            _jenisProduk.postValue(UIState.Loading)
            delay(200)
            try {
                val fetchJenisProduk = api.getJenisProduk("")
                _jenisProduk.postValue(UIState.Success(fetchJenisProduk))
            } catch (ex: Exception) {
                _jenisProduk.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchProduk() {
        viewModelScope.launch(Dispatchers.IO) {
            _produk.postValue(UIState.Loading)
            delay(200)
            try {
                val fetchProduk = api.getProduk("")
                _produk.postValue(UIState.Success(fetchProduk))
            } catch (ex: Exception) {
                _produk.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahProduk(
        tambahProduk: RequestBody, idJenisProduk: RequestBody, produk: RequestBody,
        harga: RequestBody, stok: RequestBody, gambar: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _postTambahProduk.postValue(UIState.Loading)
            delay(200)
            try {
                val postTambahProduk = api.postTambahProduk(
                    tambahProduk, idJenisProduk, produk, harga, stok, gambar
                )
                _postTambahProduk.postValue(UIState.Success(postTambahProduk))
            } catch (ex: Exception) {
                _postTambahProduk.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateProduk(
        updateProduk: RequestBody,
        idProduk: RequestBody,
        idJenisProduk: RequestBody,
        produk: RequestBody,
        harga: RequestBody,
        stok: RequestBody,
        gambar: MultipartBody.Part
    ) {
        viewModelScope.launch {
            _postUpdateProduk.postValue(UIState.Loading)
            delay(200)
            try {
                val postTambahProduk = api.postUpdateProduk(
                    updateProduk, idProduk, idJenisProduk, produk, harga, stok, gambar
                )
                _postUpdateProduk.postValue(UIState.Success(postTambahProduk))
            } catch (ex: Exception) {
                _postUpdateProduk.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateProdukNoImage(
        updateProduk: String,
        idProduk: String,
        idJenisProduk: String,
        produk: String,
        harga: String,
        stok: String
    ) {
        viewModelScope.launch {
            _postUpdateProdukNoImage.postValue(UIState.Loading)
            delay(200)
            try {
                val postTambahProduk = api.postUpdateProdukNoImage(
                    updateProduk, idProduk, idJenisProduk, produk, harga, stok
                )
                _postUpdateProdukNoImage.postValue(UIState.Success(postTambahProduk))
            } catch (ex: Exception) {
                _postUpdateProdukNoImage.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeleteProduk(idProduk: Int) {
        viewModelScope.launch {
            _postDeleteProduk.postValue(UIState.Loading)
            delay(200)
            try {
                val postTambahProduk = api.postDeleteProduk(
                    "", idProduk
                )
                _postDeleteProduk.postValue(UIState.Success(postTambahProduk))
            } catch (ex: Exception) {
                _postDeleteProduk.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getJenisProduk(): LiveData<UIState<ArrayList<JenisProdukModel>>> = _jenisProduk
    fun getProduk(): LiveData<UIState<ArrayList<ProdukModel>>> = _produk
    fun getTambahProduk(): LiveData<UIState<ResponseModel>> = _postTambahProduk
    fun getUpdateProduk(): LiveData<UIState<ResponseModel>> = _postUpdateProduk
    fun getUpdateProdukNoImage(): LiveData<UIState<ResponseModel>> = _postUpdateProdukNoImage
    fun getDeleteProduk(): LiveData<UIState<ResponseModel>> = _postDeleteProduk
}