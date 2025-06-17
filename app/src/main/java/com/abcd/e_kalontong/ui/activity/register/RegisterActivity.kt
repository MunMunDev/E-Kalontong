package com.abcd.e_kalontong.ui.activity.register

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.abcd.e_kalontong.data.model.KecamatanModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.databinding.ActivityRegisterBinding
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    @Inject
    lateinit var loading: LoadingAlertDialog
    private val viewModel : RegisterViewModel by viewModels()
    private lateinit var listKecamatan: ArrayList<KecamatanModel>
    private var selectedKecamatan = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchKecamatan()
        setButton()
        postRegisterUser()
        getKecamatan()
    }

    private fun fetchKecamatan() {
        viewModel.fetchKecamatan()
    }

    private fun getKecamatan(){
        viewModel.getKecamatan().observe(this@RegisterActivity){result->
            when(result){
                is UIState.Loading->{}
                is UIState.Success-> setSuccessFetchKecamatan(result.data)
                is UIState.Failure-> setFailureFetchKecamatan(result.message)
            }
        }
    }

    private fun setFailureFetchKecamatan(message: String) {
        Log.e("RegisterActivityTAG", "setFailureFetchKecamatan: $message ")
    }

    private fun setSuccessFetchKecamatan(data: ArrayList<KecamatanModel>) {
        if(data.isNotEmpty()){
            listKecamatan = data
            val listNamaKecamatan: ArrayList<String> = arrayListOf()
            for(value in data){
                listNamaKecamatan.add(value.kecamatan!!)
                Log.d("RegisterActivityTAG", "data: $value: ")
            }
            if(listNamaKecamatan.isNotEmpty()){
                Log.d("RegisterActivityTAG", "ada isi nya: ")
                setSpinnerKecamatan(listNamaKecamatan)
            }
        }
    }

    private fun setSpinnerKecamatan(listNamaKecamatan: ArrayList<String>) {
        binding.apply {
            val arrayAdapterKecamatan = ArrayAdapter(
                this@RegisterActivity,
                android.R.layout.simple_spinner_item,
                listNamaKecamatan
            )

            arrayAdapterKecamatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spKecamatan.adapter = arrayAdapterKecamatan

            spKecamatan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedKecamatan = listKecamatan[position].id_kecamatan!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun setButton() {
        binding.apply {
            ivBack.setOnClickListener{
                finish()
            }
            btnRegistrasi.setOnClickListener {
                buttonRegistrasi()
            }
        }
    }

    private fun buttonRegistrasi() {
        binding.apply {
            var cek = false
            if (etEditNama.toString().isEmpty()) {
                etEditNama.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etEditNomorHp.toString().isEmpty()) {
                etEditNomorHp.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etDetailAlamat.toString().isEmpty()) {
                etDetailAlamat.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etEditUsername.toString().isEmpty()) {
                etEditUsername.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etEditPassword.toString().isEmpty()) {
                etEditPassword.error = "Tidak Boleh Kosong"
                cek = true
            }

            if (!cek) {
                postTambahData(
                    etEditNama.text.toString().trim(),
                    etEditNomorHp.text.toString().trim(),
                    selectedKecamatan,
                    etDetailAlamat.text.toString().trim(),
                    etEditUsername.text.toString().trim(),
                    etEditPassword.text.toString().trim(),
                )
            }
        }
    }

    private fun postTambahData(
        nama: String, nomorHp: String, idKecamatan: String, detailAlamat: String,
        username: String, password: String
    ){
        viewModel.postRegisterUser(
            nama, nomorHp, idKecamatan, detailAlamat, username, password, "user"
        )
    }

    private fun postRegisterUser(){
        viewModel.getRegisterUser().observe(this@RegisterActivity){ result ->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@RegisterActivity)
                is UIState.Failure-> responseFailureRegisterUser(result.message)
                is UIState.Success-> responseSuccessRegiserUser(result.data)
            }
        }
    }

    private fun responseFailureRegisterUser(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun responseSuccessRegiserUser(data: ResponseModel) {
        if (data.status == "0"){
            Toast.makeText(this@RegisterActivity, "Berhasil melakukan registrasi", Toast.LENGTH_SHORT).show()
            finish()
        } else{
            Toast.makeText(this@RegisterActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }
}