package com.abcd.e_kalontong.ui.activity.admin.riwayat_pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.RiwayatPesananHalModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRiwayatPesananViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {

    private var _riwayatPesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananHalModel>>>()

    fun fetchRiwayatPesanan(){
        viewModelScope.launch(Dispatchers.IO) {
            _riwayatPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.getAdminRiwayatPesanan("")
                _riwayatPesanan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _riwayatPesanan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }


    fun getRiwayatPesanan(): LiveData<UIState<ArrayList<RiwayatPesananHalModel>>> = _riwayatPesanan
}