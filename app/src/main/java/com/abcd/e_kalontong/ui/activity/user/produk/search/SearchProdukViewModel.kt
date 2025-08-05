package com.abcd.e_kalontong.ui.activity.user.produk.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchProdukViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _produk = MutableLiveData<UIState<ArrayList<ProdukModel>>>()
    fun getProduk(): LiveData<UIState<ArrayList<ProdukModel>>> = _produk

    private var _keranjangBelanja = MutableLiveData<UIState<ArrayList<PesananModel>>>()
    fun getKeranjangBelanja(): LiveData<UIState<ArrayList<PesananModel>>> = _keranjangBelanja

    private var _pesan = MutableLiveData<UIState<ResponseModel>>()
    fun getPesan(): LiveData<UIState<ResponseModel>> = _pesan

    private var _updatePesan = MutableLiveData<UIState<ResponseModel>>()
    fun getUpdatePesan(): LiveData<UIState<ResponseModel>> = _updatePesan

    fun fetchProduk(){
        viewModelScope.launch(Dispatchers.IO) {
            _produk.postValue(UIState.Loading)
            delay(200)
            try {
                val data = api.getProduk("")
                _produk.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _produk.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }
    fun fetchKeranjangBelanja(idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _keranjangBelanja.postValue(UIState.Loading)
            delay(200)
            try {
                val data = api.getKeranjangBelanja("", idUser)
                _keranjangBelanja.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _keranjangBelanja.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postPesan(idUser: Int, idProduk: Int, jumlah: String){
        viewModelScope.launch(Dispatchers.IO) {
            _pesan.postValue(UIState.Loading)
            delay(200)
            try {
                val data = api.postTambahPesanan("", idUser, idProduk, jumlah)
                _pesan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _pesan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postUpdatePesan(idPesanan: Int, jumlah: String){
        viewModelScope.launch(Dispatchers.IO) {
            _updatePesan.postValue(UIState.Loading)
            delay(200)
            try {
                val data = api.postUpdatePesanan("", idPesanan, jumlah)
                _updatePesan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _updatePesan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }
}