package com.abcd.e_kalontong.ui.activity.alamat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abcd.e_kalontong.data.database.api.ApiService
import com.abcd.e_kalontong.data.model.AlamatModel
import com.abcd.e_kalontong.data.model.KecamatanModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PilihAlamatViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _alamatUser = MutableLiveData<UIState<ArrayList<AlamatModel>>>()
    val _kecamatan = MutableLiveData<UIState<ArrayList<KecamatanModel>>>()
    val _updateMainAlamat = MutableLiveData<UIState<ResponseModel>>()
    val _tambahAlamatUser = MutableLiveData<UIState<ResponseModel>>()
    val _updateAlamatUser = MutableLiveData<UIState<ResponseModel>>()

    fun fetchDataAlamat(idUser:Int){
        viewModelScope.launch(Dispatchers.IO) {
            _alamatUser.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataAlamat = api.getAlamatUser("", idUser)
                _alamatUser.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _alamatUser.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun fetchKecamatan(){
        viewModelScope.launch(Dispatchers.IO) {
            _kecamatan.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataAlamat = api.getKecamatan("")
                _kecamatan.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _kecamatan.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postUpdateMainAlamat(
        idAlamat: String, idUser: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _updateMainAlamat.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataAlamat = api.postUpdateMainAlamat(
                    "", idAlamat, idUser
                )
                _updateMainAlamat.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _updateMainAlamat.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postTambahAlamat(
        idUser: String, namaLengkap: String, nomorHp: String,
        idKecamatan: String, detailAlamat: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _tambahAlamatUser.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataAlamat = api.postTambahAlamat(
                    "", idUser, namaLengkap, nomorHp, idKecamatan, detailAlamat
                )
                _tambahAlamatUser.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _tambahAlamatUser.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postUpdateAlamat(
        idAlamat: String, idUser: String, namaLengkap: String,
        nomorHp: String, idKecamatan: String, detailAlamat: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _updateAlamatUser.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataAlamat = api.postUpdateAlamat(
                    "", idAlamat, idUser, namaLengkap, nomorHp, idKecamatan, detailAlamat
                )
                _updateAlamatUser.postValue(UIState.Success(dataAlamat))
            } catch (ex: Exception){
                _updateAlamatUser.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun getDataAlamat(): LiveData<UIState<ArrayList<AlamatModel>>> = _alamatUser
    fun getKecamatan(): LiveData<UIState<ArrayList<KecamatanModel>>> = _kecamatan
    fun getUpdateMainAlamat(): LiveData<UIState<ResponseModel>> = _updateMainAlamat
    fun getTambahAlamat(): LiveData<UIState<ResponseModel>> = _tambahAlamatUser
    fun getUpdateAlamat(): LiveData<UIState<ResponseModel>> = _updateAlamatUser
}