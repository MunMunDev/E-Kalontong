package com.abcd.e_kalontong.ui.activity.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _registerUser = MutableLiveData<UIState<ResponseModel>>()

    fun postRegisterUser(nama:String, nomorHp:String, username:String, password:String, sebagai:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val registerUser = api.addUser("", nama, nomorHp, username, password, sebagai)
                _registerUser.postValue(UIState.Success(registerUser))
            } catch (ex: Exception){
                _registerUser.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getRegisterUser(): LiveData<UIState<ResponseModel>> = _registerUser
}
