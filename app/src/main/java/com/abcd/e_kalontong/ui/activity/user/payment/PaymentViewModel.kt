package com.abcd.e_kalontong.ui.activity.user.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.AlamatModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _keranjangBelanja = MutableLiveData<UIState<ArrayList<PesananModel>>>()
    val _pembayaran = MutableLiveData<UIState<ArrayList<AlamatModel>>>()
    val _responseRegistrasiPembayaran = MutableLiveData<UIState<ResponseModel>>()
    private val _postPesan = MutableLiveData<UIState<ResponseModel>>()


    fun fetchKeranjangBelanja(idUser: Int){
        viewModelScope.launch(Dispatchers.IO) {
            _keranjangBelanja.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getKeranjangBelanja("", idUser)
                _keranjangBelanja.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _keranjangBelanja.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchAlamat(idUser:Int){
        viewModelScope.launch(Dispatchers.IO) {
            _pembayaran.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataPembayaran = api.getAlamatUtama("", idUser)
                _pembayaran.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception){
                _pembayaran.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postRegistrasiPembayaran(kodeUnik:String, idUser:Int){
        viewModelScope.launch(Dispatchers.IO){
            _responseRegistrasiPembayaran.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataRegistrasiPembayaran = api.postRegistrasiPembayaran(
                    "", kodeUnik, idUser)
                _responseRegistrasiPembayaran.postValue(UIState.Success(dataRegistrasiPembayaran))
            } catch (ex: Exception){
                _responseRegistrasiPembayaran.postValue(UIState.Failure("Error pada : ${ex.message}"))
            }
        }
    }

    fun postPesanCod(idUser:Int){
        viewModelScope.launch(Dispatchers.IO) {
            _postPesan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val fetchPesanan = api.postPesanCod("", idUser)
                _postPesan.postValue(UIState.Success(fetchPesanan))
            } catch (ex: Exception){
                _postPesan.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getKeranjangBelanja(): LiveData<UIState<ArrayList<PesananModel>>> = _keranjangBelanja
    fun getAlamat(): LiveData<UIState<ArrayList<AlamatModel>>> = _pembayaran
    fun getRegistrasiPembayaran(): LiveData<UIState<ResponseModel>> = _responseRegistrasiPembayaran
    fun getPostPesan(): LiveData<UIState<ResponseModel>> = _postPesan
}