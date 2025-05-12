package com.abcd.e_kalontong.ui.fragment.user.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _produk = MutableLiveData<UIState<ArrayList<ProdukModel>>>()
    private var _pesan = MutableLiveData<UIState<ResponseModel>>()

    fun fetchProduk(){
        viewModelScope.launch(Dispatchers.IO) {
            _produk.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getProduk("")
                _produk.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _produk.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postPesan(idUser: Int, idProduk: Int, jumlah: String){
        viewModelScope.launch(Dispatchers.IO) {
            _pesan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.postTambahPesanan("", idUser, idProduk, jumlah)
                _pesan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _pesan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getProduk(): LiveData<UIState<ArrayList<ProdukModel>>> = _produk
    fun getPesan(): LiveData<UIState<ResponseModel>> = _pesan
}