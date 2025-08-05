package com.abcd.e_kalontong.ui.activity.admin.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.KecamatanModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.data.model.UserModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAkunViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _semuaAkun = MutableLiveData<UIState<ArrayList<UserModel>>>()
    private var _kecamatan = MutableLiveData<UIState<ArrayList<KecamatanModel>>>()
    private var _postTambahAkun = MutableLiveData<UIState<ResponseModel>>()
    private var _postUpdateAkun = MutableLiveData<UIState<ResponseModel>>()
    private var _postDeleteAkun = MutableLiveData<UIState<ResponseModel>>()

    fun fetchAkun(){
        viewModelScope.launch(Dispatchers.IO) {
            _semuaAkun.postValue(UIState.Loading)
            delay(200)
            try {
                val fetchAkun = api.getAllUser("")
                _semuaAkun.postValue(UIState.Success(fetchAkun))
            } catch (ex: Exception){
                _semuaAkun.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchKecamatan(){
        viewModelScope.launch(Dispatchers.IO) {
            _kecamatan.postValue(UIState.Loading)
            delay(200)
            try {
                val dataAlamat = api.getKecamatan("")
                _kecamatan.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _kecamatan.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postTambahAkun(
        nama:String, nomorHp:String, idKecamatan:String, detailAlamat:String,
        username:String, password:String, sebagai:String
    ){
        viewModelScope.launch {
            _postTambahAkun.postValue(UIState.Loading)
            delay(200)
            try {
                val postTambahAkun = api.addUser("", nama, nomorHp, idKecamatan, detailAlamat, username, password, "user")
                _postTambahAkun.postValue(UIState.Success(postTambahAkun))
            } catch (ex: Exception){
                _postTambahAkun.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateAkun(
        idUser: String, nama:String, nomorHp:String, username:String, password:String, usernameLama:String
    ){
        viewModelScope.launch {
            _postUpdateAkun.postValue(UIState.Loading)
            delay(200)
            try {
                val postTambahAkun = api.postUpdateUser(
                    "", idUser, nama, nomorHp, username, password, usernameLama
                )
                _postUpdateAkun.postValue(UIState.Success(postTambahAkun))
            } catch (ex: Exception){
                _postUpdateAkun.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postDeleteAkun(idUser: String){
        viewModelScope.launch {
            _postDeleteAkun.postValue(UIState.Loading)
            delay(200)
            try {
                val postTambahAkun = api.postAdminHapusUser(
                    "", idUser
                )
                _postDeleteAkun.postValue(UIState.Success(postTambahAkun))
            } catch (ex: Exception){
                _postDeleteAkun.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getAkun(): LiveData<UIState<ArrayList<UserModel>>> = _semuaAkun
    fun getKecamatan(): LiveData<UIState<ArrayList<KecamatanModel>>> = _kecamatan
    fun getTambahAkun(): LiveData<UIState<ResponseModel>> = _postTambahAkun
    fun getUpdateAkun(): LiveData<UIState<ResponseModel>> = _postUpdateAkun
    fun getDeleteAkun(): LiveData<UIState<ResponseModel>> = _postDeleteAkun
}