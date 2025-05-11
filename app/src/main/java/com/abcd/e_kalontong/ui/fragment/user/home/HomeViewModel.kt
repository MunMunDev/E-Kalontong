package com.abcd.e_kalontong.ui.fragment.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _pesanan = MutableLiveData<UIState<ArrayList<PesananModel>>>()
    private var _updatePesanan = MutableLiveData<UIState<ResponseModel>>()
    private var _deletePesanan = MutableLiveData<UIState<ResponseModel>>()

    fun fetchPesanan(idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getPesanan("", idUser)
                _pesanan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _pesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postUpdatePesanan(idPesanan: Int, jumlah: String){
        viewModelScope.launch(Dispatchers.IO) {
            _updatePesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.postUpdatePesanan("", idPesanan, jumlah)
                _updatePesanan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _updatePesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postDeletePesanan(idPesanan: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _deletePesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.postDeletePesanan("", idPesanan)
                _deletePesanan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _deletePesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getPesanan(): LiveData<UIState<ArrayList<PesananModel>>> = _pesanan
    fun getUpdatePesanan(): LiveData<UIState<ResponseModel>> = _updatePesanan
    fun getDeletePesanan(): LiveData<UIState<ResponseModel>> = _deletePesanan
}