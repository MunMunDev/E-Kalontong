package com.abcd.e_kalontong.ui.fragment.user.profile

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.data.model.UserModel
import com.abcd.e_kalontong.databinding.AlertDialogAkunBinding
import com.abcd.e_kalontong.databinding.FragmentProfileBinding
import com.abcd.e_kalontong.utils.KontrolNavigationDrawer
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferencesLogin
    private val viewModel: ProfileViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var tempUser: UserModel = UserModel()

    private lateinit var activityContext: Activity
    private lateinit var contextLifecycleOwner: LifecycleOwner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        activityContext = requireActivity()
        contextLifecycleOwner = viewLifecycleOwner

        setSharedPreferences()
        setData()
        button()
        getPostUpdateData()

        return binding.root
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(activityContext)
    }

    private fun setData(){
        binding.apply {
            etNama.text = sharedPreferences.getNama()
            etNomorHp.text = sharedPreferences.getNomorHp()
            etUsername.text = sharedPreferences.getUsername()
            etPassword.text = sharedPreferences.getPassword()
        }
    }

    private fun button() {
        binding.btnUbahData.setOnClickListener {
            setDialogUpdateData()
        }
    }

    private fun setDialogUpdateData() {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(activityContext)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etEditNama.setText(sharedPreferences.getNama())
            etEditNomorHp.setText(sharedPreferences.getNomorHp())
            etEditUsername.setText(sharedPreferences.getUsername())
            etEditPassword.setText(sharedPreferences.getPassword())

            btnSimpan.setOnClickListener {
                var cek = false
                if(etEditNama.toString().isEmpty()){
                    etEditNama.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etEditNomorHp.toString().isEmpty()){
                    etEditNomorHp.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etEditUsername.toString().isEmpty()){
                    etEditUsername.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etEditPassword.toString().isEmpty()){
                    etEditPassword.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if(!cek){
                    tempUser = UserModel(
                        sharedPreferences.getIdUser().toString(),
                        etEditNama.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditUsername.text.toString(),
                        etEditPassword.text.toString(),
                        sharedPreferences.getSebagai()
                    )
                    postUpdateData(
                        sharedPreferences.getIdUser().toString(),
                        etEditNama.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditUsername.text.toString(),
                        etEditPassword.text.toString(),
                        sharedPreferences.getUsername()
                    )

                    dialogInputan.dismiss()
                }

            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateData(idUser: String, nama: String, nomorHp: String, username: String, password: String, usernameLama: String) {
        viewModel.postUpdateUser(idUser, nama, nomorHp, username, password, usernameLama)
    }

    private fun getPostUpdateData(){
        viewModel.getUpdateData().observe(contextLifecycleOwner){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(activityContext)
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData(result.message)
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(data: ResponseModel) {
        if(data.status == "0"){
            Toast.makeText(activityContext, "Berhasil Update Akun", Toast.LENGTH_SHORT).show()
            sharedPreferences.setLogin(
                tempUser.idUser!!.trim().toInt(),
                tempUser.nama!!,
                tempUser.nomor_hp!!,
                tempUser.username!!,
                tempUser.password!!,
                "user"
            )
            tempUser = UserModel()
            setData()
        } else{
            Toast.makeText(activityContext, data.message_response, Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

}