package com.abcd.e_kalontong.ui.activity.admin.kasir

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminKasirViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _fetchPesananPesananKasir = MutableLiveData<UIState<ArrayList<PesananModel>>>()
    private var _postUpdatePesananKasir = MutableLiveData<UIState<ResponseModel>>()
    private var _postDeletePesananKasir = MutableLiveData<UIState<ResponseModel>>()
    private var _postPesan = MutableLiveData<UIState<ResponseModel>>()

    fun fetchPesananKasir(){
        viewModelScope.launch(Dispatchers.IO) {
            _fetchPesananPesananKasir.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesananKasir = api.getAdminPesananKasir("")
                _fetchPesananPesananKasir.postValue(UIState.Success(fetchPesananKasir))
            } catch (ex: Exception){
                _fetchPesananPesananKasir.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postUpdatePesanan(
        idPesanan: Int, jumlah: String
    ){
        viewModelScope.launch {
            _postUpdatePesananKasir.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postData = api.postUpdatePesanan(
                    "", idPesanan, jumlah
                )
                _postUpdatePesananKasir.postValue(UIState.Success(postData))
            } catch (ex: Exception){
                _postUpdatePesananKasir.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeletePesanan(idPesanan: Int){
        viewModelScope.launch {
            _postDeletePesananKasir.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postData = api.postDeletePesanan(
                    "", idPesanan
                )
                _postDeletePesananKasir.postValue(UIState.Success(postData))
            } catch (ex: Exception){
                _postDeletePesananKasir.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postPesan(){
        viewModelScope.launch {
            _postPesan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postData = api.postAdminPesan("")
                _postPesan.postValue(UIState.Success(postData))
            } catch (ex: Exception){
                _postPesan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getPesananKasir(): LiveData<UIState<ArrayList<PesananModel>>> = _fetchPesananPesananKasir
    fun getUpdatePesanan(): LiveData<UIState<ResponseModel>> = _postUpdatePesananKasir
    fun getDeletePesanan(): LiveData<UIState<ResponseModel>> = _postDeletePesananKasir
    fun getPesan(): LiveData<UIState<ResponseModel>> = _postPesan
}