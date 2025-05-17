package com.abcd.e_kalontong.ui.fragment.user.profile

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
class ProfileViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _responsePostUpdateUser = MutableLiveData<UIState<ResponseModel>>()

    fun postUpdateUser(
        idUser:String, nama: String, nomorHp: String,
        username: String, password: String, usernameLama:String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateUser.postValue(UIState.Loading)
            try {
                val data = api.postUpdateUser("", idUser, nama, nomorHp, username, password, usernameLama)
                _responsePostUpdateUser.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateUser.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getUpdateData(): LiveData<UIState<ResponseModel>> = _responsePostUpdateUser
}