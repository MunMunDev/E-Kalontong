package com.abcd.e_kalontong.ui.activity.admin.jenis_produk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.JenisProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminJenisProdukViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    private var _jenisProduk = MutableLiveData<UIState<ArrayList<JenisProdukModel>>>()
    private var _postTambahJenisProduk = MutableLiveData<UIState<ResponseModel>>()
    private var _postUpdateJenisProduk = MutableLiveData<UIState<ResponseModel>>()
    private var _postDeleteProduk = MutableLiveData<UIState<ResponseModel>>()

    fun fetchJenisProduk(){
        viewModelScope.launch(Dispatchers.IO) {
            _jenisProduk.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchJenisProduk = api.getJenisProduk("")
                _jenisProduk.postValue(UIState.Success(fetchJenisProduk))
            } catch (ex: Exception){
                _jenisProduk.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun postTambahProduk(jenisProduk: String){
        viewModelScope.launch {
            _postTambahJenisProduk.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahProduk = api.postTambahJenisProduk(
                    "", jenisProduk
                )
                _postTambahJenisProduk.postValue(UIState.Success(postTambahProduk))
            } catch (ex: Exception){
                _postTambahJenisProduk.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateProduk(
        idJenisProduk: String, jenisProduk: String
    ){
        viewModelScope.launch {
            _postUpdateJenisProduk.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahProduk = api.postUpdateJenisProduk(
                    "", idJenisProduk, jenisProduk
                )
                _postUpdateJenisProduk.postValue(UIState.Success(postTambahProduk))
            } catch (ex: Exception){
                _postUpdateJenisProduk.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeleteProduk(idJenisProduk: String){
        viewModelScope.launch {
            _postDeleteProduk.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahProduk = api.postDeleteJenisProduk(
                    "", idJenisProduk
                )
                _postDeleteProduk.postValue(UIState.Success(postTambahProduk))
            } catch (ex: Exception){
                _postDeleteProduk.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getJenisProduk(): LiveData<UIState<ArrayList<JenisProdukModel>>> = _jenisProduk
    fun getTambahProduk(): LiveData<UIState<ResponseModel>> = _postTambahJenisProduk
    fun getUpdateProduk(): LiveData<UIState<ResponseModel>> = _postUpdateJenisProduk
    fun getDeleteProduk(): LiveData<UIState<ResponseModel>> = _postDeleteProduk
}