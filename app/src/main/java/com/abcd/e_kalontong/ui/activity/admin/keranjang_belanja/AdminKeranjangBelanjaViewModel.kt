package com.abcd.e_kalontong.ui.activity.admin.keranjang_belanja

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.PesananHalModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminKeranjangBelanjaViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    private var _pesanan = MutableLiveData<UIState<ArrayList<PesananHalModel>>>()
    private var _postTambahPesanan = MutableLiveData<UIState<ResponseModel>>()
    private var _postUpdatePesanan = MutableLiveData<UIState<ResponseModel>>()
    private var _postDeletePesanan = MutableLiveData<UIState<ResponseModel>>()

    fun fetchPesanan(){
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.getAdminKeranjangBelanja("")
                _pesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahPesanan(
        idUser: String, idProduk: String, jumlah: String
    ){
        viewModelScope.launch {
            _postTambahPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahPesanan = api.postAdminTambahPesanan(
                    "", idUser, idProduk, jumlah
                )
                _postTambahPesanan.postValue(UIState.Success(postTambahPesanan))
            } catch (ex: Exception){
                _postTambahPesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatePesanan(
        idPesanan:String, idUser:String, idProduk:String, jumlah:String
    ){
        viewModelScope.launch {
            _postUpdatePesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postUpdatePesanan = api.postAdminUpdatePesanan(
                    "", idPesanan, idUser, idProduk, jumlah
                )
                _postUpdatePesanan.postValue(UIState.Success(postUpdatePesanan))
            } catch (ex: Exception){
                _postUpdatePesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeletePesanan(idPesanan: String){
        viewModelScope.launch {
            _postDeletePesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postDeletePesanan = api.postAdminDeletePesanan(
                    "", idPesanan
                )
                _postDeletePesanan.postValue(UIState.Success(postDeletePesanan))
            } catch (ex: Exception){
                _postDeletePesanan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getPesanan(): LiveData<UIState<ArrayList<PesananHalModel>>> = _pesanan
    fun getTambahPesanan(): LiveData<UIState<ResponseModel>> = _postTambahPesanan
    fun getUpdatePesanan(): LiveData<UIState<ResponseModel>> = _postUpdatePesanan
    fun getDeletePesanan(): LiveData<UIState<ResponseModel>> = _postDeletePesanan
}