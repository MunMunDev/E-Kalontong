package com.abcd.e_kalontong.ui.activity.admin.pesanan.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPesananDetailViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _postBayar = MutableLiveData<UIState<ResponseModel>>()
    private val _postKirim = MutableLiveData<UIState<ResponseModel>>()

    fun postBayar(idPemesanan: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postBayar.postValue(UIState.Loading)
            delay(1000)
            try {
                val data = api.postAdminPesananBayar("", idPemesanan)
                _postBayar.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _postBayar.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDikirim(idPemesanan: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postKirim.postValue(UIState.Loading)
            delay(1000)
            try {
                val data = api.postAdminPesananDikirim("", idPemesanan)
                _postKirim.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _postKirim.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getBayar() : LiveData<UIState<ResponseModel>> = _postBayar
    fun getDikirim() : LiveData<UIState<ResponseModel>> = _postKirim

}