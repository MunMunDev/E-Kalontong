package com.abcd.e_kalontong.ui.activity.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.UserModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _user = MutableLiveData<UIState<ArrayList<UserModel>>>()

    fun fetchUser(
        username: String, password: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            delay(200)
            try {
                val data = api.getUser("", username, password)
                _user.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _user.postValue(UIState.Failure("Error : ${ex.message}"))
            }
        }
    }

    fun getUser() : LiveData<UIState<ArrayList<UserModel>>> = _user
}