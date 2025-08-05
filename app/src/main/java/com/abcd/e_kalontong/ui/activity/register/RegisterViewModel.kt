package com.abcd.e_kalontong.ui.activity.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.KecamatanModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _registerUser = MutableLiveData<UIState<ResponseModel>>()
    private var _kecamatan = MutableLiveData<UIState<ArrayList<KecamatanModel>>>()

    fun postRegisterUser(
        nama:String, nomorHp:String, idKecamatan:String, detailAlamat:String,
        username:String, password:String, sebagai:String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _registerUser.postValue(UIState.Loading)
            delay(200)
            try {
                val registerUser = api.addUser("", nama, nomorHp, idKecamatan, detailAlamat, username, password, sebagai)
                _registerUser.postValue(UIState.Success(registerUser))
            } catch (ex: Exception){
                _registerUser.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchKecamatan(){
        viewModelScope.launch(Dispatchers.IO) {
//            _kecamatan.postValue(UIState.Loading)
            try {
                val dataAlamat = api.getKecamatan("")
                _kecamatan.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _kecamatan.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun getRegisterUser(): LiveData<UIState<ResponseModel>> = _registerUser
    fun getKecamatan(): LiveData<UIState<ArrayList<KecamatanModel>>> = _kecamatan
}
