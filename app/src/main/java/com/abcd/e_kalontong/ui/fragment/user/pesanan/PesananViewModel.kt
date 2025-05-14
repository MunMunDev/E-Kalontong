package com.abcd.e_kalontong.ui.fragment.user.pesanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PesananViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _pesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananModel>>>()
    val _riwayatPesanan = MutableLiveData<UIState<ArrayList<RiwayatPesananModel>>>()

    fun fetchPesanan(idUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _pesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataPembayaran = api.getPesanan("", idUser)
                _pesanan.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception) {
                _pesanan.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun fetchRiwayatPesanan(idUser: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _riwayatPesanan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataPembayaran = api.getRiwayatPesanan("", idUser)
                _riwayatPesanan.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception) {
                _riwayatPesanan.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun getPesanan(): LiveData<UIState<ArrayList<RiwayatPesananModel>>> = _pesanan
    fun getRiwayatPesanan(): LiveData<UIState<ArrayList<RiwayatPesananModel>>> = _riwayatPesanan
}