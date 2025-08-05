package com.abcd.e_kalontong.ui.activity.admin.pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.PesananHalModel
import com.abcd.e_kalontong.data.model.RiwayatPesananHalModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPesananViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {

    private var _riwayatPesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananHalModel>>>()

    fun fetchPesanan(){
        viewModelScope.launch(Dispatchers.IO) {
            _riwayatPesanan.postValue(UIState.Loading)
            delay(200)
            try {
                val fetchPesanan = api.getAdminPesanan("")
                _riwayatPesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _riwayatPesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getPesanan(): LiveData<UIState<ArrayList<RiwayatPesananHalModel>>> = _riwayatPesanan
}