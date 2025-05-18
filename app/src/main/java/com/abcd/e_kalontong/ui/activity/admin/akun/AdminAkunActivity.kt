package com.abcd.e_kalontong.ui.activity.admin.akun

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.AdminAkunAdapter
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.data.model.UserModel
import com.abcd.e_kalontong.databinding.ActivityAdminAkunBinding
import com.abcd.e_kalontong.databinding.AlertDialogAkunBinding
import com.abcd.e_kalontong.databinding.AlertDialogKeteranganBinding
import com.abcd.e_kalontong.databinding.AlertDialogKonfirmasiBinding
import com.abcd.e_kalontong.utils.KontrolNavigationDrawer
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminAkunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAkunBinding
    private val viewModel: AdminAkunViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var adapter: AdminAkunAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchAkun()
        getAkun()
        getTambahUser()
        getUpdateUser()
        getHapusUser()
    }


    private fun setKontrolNavigationDrawer() {
        binding.apply {
            appNavbarDrawer.apply{
                ivNavDrawer.visibility = View.VISIBLE
                ivBack.visibility = View.GONE
                tvTitle.text = "Semua Akun"
            }
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminAkunActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, appNavbarDrawer.ivNavDrawer, this@AdminAkunActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                showDialogTambahData()
            }
        }
    }

    private fun showDialogTambahData() {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {

            btnSimpan.setOnClickListener {
                val nama = etEditNama.text.toString().trim()
                val nomorHp = etEditNomorHp.text.toString().trim()
                val username = etEditUsername.text.toString().trim()
                val password = etEditPassword.text.toString().trim()
                postTambahUser(nama, nomorHp, username, password)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahUser(
        nama: String, nomorHp: String,
        username: String, password: String
    ) {
        viewModel.postTambahAkun(nama, nomorHp, username, password, "user")
    }

    private fun getTambahUser(){
        viewModel.getTambahAkun().observe(this@AdminAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminAkunActivity)
                is UIState.Failure-> setFailureTambahAkun(result.message)
                is UIState.Success-> setSuccessTambahAkun(result.data)
            }
        }
    }

    private fun setSuccessTambahAkun(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@AdminAkunActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminAkunActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
        fetchAkun()
    }

    private fun setFailureTambahAkun(message: String) {
        Toast.makeText(this@AdminAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun fetchAkun() {
        viewModel.fetchAkun()
    }

    private fun getAkun(){
        viewModel.getAkun().observe(this@AdminAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminAkunActivity)
                is UIState.Failure-> setFailureFetchAkun(result.message)
                is UIState.Success-> setSuccessFetchAkun(result.data)
            }
        }
    }

    private fun setFailureFetchAkun(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminAkunActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchAkun(data: ArrayList<UserModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminAkunActivity, "tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<UserModel>) {
        binding.apply {
            adapter = AdminAkunAdapter(data, object: OnClickItem.ClickAkun{
                override fun clickItemNama(keterangan: String, nama: String, it: View) {
                    showClickKeterangan(keterangan, nama)
                }

                override fun clickItemNomoHp(keterangan: String, nomorHp: String, it: View) {

                }

                override fun clickItemUsername(keterangan: String, username: String, it: View) {
                    showClickKeterangan(keterangan, username)
                }

                override fun clickItemSetting(akun: UserModel, it: View) {
                    val popupMenu = PopupMenu(this@AdminAkunActivity, it)
                    popupMenu.inflate(R.menu.popup_edit_hapus)
                    popupMenu.setOnMenuItemClickListener(object :
                        PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                            when (menuItem!!.itemId) {
                                R.id.edit -> {
                                    setShowDialogEdit(akun)
                                    return true
                                }
                                R.id.hapus -> {
                                    setShowDialogHapus(akun)
                                    return true
                                }
                            }
                            return true
                        }

                    })
                    popupMenu.show()
                }
            })
        }

        binding.apply {
            rvAkun.layoutManager = LinearLayoutManager(this@AdminAkunActivity, LinearLayoutManager.VERTICAL, false)
            rvAkun.adapter = adapter
        }

    }

    private fun showClickKeterangan(keterangan: String, value: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = keterangan
            tvBodyKeterangan.text = value
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowDialogEdit(akun: UserModel) {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etEditNama.setText(akun.nama)
            etEditNomorHp.setText(akun.nomor_hp)
            etEditUsername.setText(akun.username)
            etEditPassword.setText(akun.password)

            btnSimpan.setOnClickListener {
                val idUser = akun.idUser!!
                val nama = etEditNama.text.toString().trim()
                val nomorHp = etEditNomorHp.text.toString().trim()
                val username = etEditUsername.text.toString().trim()
                val password = etEditPassword.text.toString().trim()
                val usernameLama = akun.username!!
                postUpdateUser(idUser, nama, nomorHp, username, password, usernameLama)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateUser(
        idUser: String, nama: String, nomorHp: String,
        username: String, password: String, usernameLama: String
    ) {
        viewModel.postUpdateAkun(idUser, nama, nomorHp, username, password, usernameLama)
    }

    private fun getUpdateUser(){
        viewModel.getUpdateAkun().observe(this@AdminAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminAkunActivity)
                is UIState.Failure-> setFailureUpdateAkun(result.message)
                is UIState.Success-> setSuccessUpdateAkun(result.data)
            }
        }
    }

    private fun setSuccessUpdateAkun(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@AdminAkunActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
            fetchAkun()
        } else{
            Toast.makeText(this@AdminAkunActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureUpdateAkun(message: String) {
        Toast.makeText(this@AdminAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setShowDialogHapus(akun: UserModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminAkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus ${akun.nama!!} ?"
            tvBodyKonfirmasi.text = "Akun ini akan hapus dan data tidak dapat dipulihkan"

            btnKonfirmasi.setOnClickListener {
                val idUser = akun.idUser!!
                postHapusUser(idUser)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusUser(idUser: String){
        viewModel.postDeleteAkun(idUser)
    }

    private fun getHapusUser(){
        viewModel.getDeleteAkun().observe(this@AdminAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminAkunActivity)
                is UIState.Failure-> setFailureHapusAkun(result.message)
                is UIState.Success-> setSuccessHapusAkun(result.data)
            }
        }
    }

    private fun setSuccessHapusAkun(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@AdminAkunActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
            fetchAkun()
        } else{
            Toast.makeText(this@AdminAkunActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureHapusAkun(message: String) {
        Toast.makeText(this@AdminAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }
}