package com.abcd.e_kalontong.ui.activity.admin.jenis_produk

import android.content.Intent
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
import com.abcd.e_kalontong.adapter.AdminJenisProdukAdapter
import com.abcd.e_kalontong.data.model.JenisProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.databinding.ActivityAdminJenisProdukBinding
import com.abcd.e_kalontong.databinding.AlertDialogAdminJenisProdukBinding
import com.abcd.e_kalontong.databinding.AlertDialogKonfirmasiBinding
import com.abcd.e_kalontong.ui.activity.admin.main.AdminMainActivity
import com.abcd.e_kalontong.utils.KontrolNavigationDrawer
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminJenisProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminJenisProdukBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: AdminJenisProdukViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminJenisProdukAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminJenisProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchJenisProduk()
        getJenisProduk()
        getTambahJenisProduk()
        getUpdateJenisProduk()
        getHapusJenisProduk()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminJenisProdukActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminJenisProdukActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                setShowDialogTambah()
            }
        }
    }

    private fun fetchJenisProduk() {
        viewModel.fetchJenisProduk()
    }
    private fun getJenisProduk(){
        viewModel.getJenisProduk().observe(this@AdminJenisProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisProdukActivity)
                is UIState.Failure-> setFailureFetchJenisProduk(result.message)
                is UIState.Success-> setSuccessFetchJenisProduk(result.data)
            }
        }
    }

    private fun setFailureFetchJenisProduk(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminJenisProdukActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchJenisProduk(data: ArrayList<JenisProdukModel>) {
        if(data.isNotEmpty()){
            setAdapterJenisProduk(data)
        } else{
            Toast.makeText(this@AdminJenisProdukActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setAdapterJenisProduk(data: ArrayList<JenisProdukModel>) {
        adapter = AdminJenisProdukAdapter(data, object : OnClickItem.ClickAdminJenisProduk{
            override fun clickItemSetting(jenisProduk: JenisProdukModel, it: View) {
                val popupMenu = PopupMenu(this@AdminJenisProdukActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(jenisProduk)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(jenisProduk)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }
        })

        binding.apply {
            rvJenisProduk.layoutManager = LinearLayoutManager(this@AdminJenisProdukActivity, LinearLayoutManager.VERTICAL, false)
            rvJenisProduk.adapter = adapter
        }
    }

    private fun setShowDialogEdit(jenisProduk: JenisProdukModel) {
        val view = AlertDialogAdminJenisProdukBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisProdukActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etJenisProduk.setText(jenisProduk.jenis_produk)

            var cek = true
            if(etJenisProduk.text.toString().trim().isEmpty()){
                etJenisProduk.error = "Tidak Boleh Kosong"
                cek = false
            }

            btnSimpan.setOnClickListener {
                if(cek){
                    postUpdateJenisProduk(jenisProduk.id_jenis_produk.toString(), etJenisProduk.text.toString().trim())

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateJenisProduk(idJenisProduk: String, jenisProduk: String) {
        viewModel.postUpdateProduk(idJenisProduk, jenisProduk)
    }

    private fun getUpdateJenisProduk(){
        viewModel.getUpdateProduk().observe(this@AdminJenisProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisProdukActivity)
                is UIState.Failure-> setFailureUpdateJenisProduk(result.message)
                is UIState.Success-> setSuccessUpdateJenisProduk(result.data)
            }
        }
    }

    private fun setFailureUpdateJenisProduk(message: String) {
        Toast.makeText(this@AdminJenisProdukActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateJenisProduk(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@AdminJenisProdukActivity, "Berhasil", Toast.LENGTH_SHORT).show()
            fetchJenisProduk()
        } else{
            Toast.makeText(this@AdminJenisProdukActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setShowDialogHapus(jenisProduk: JenisProdukModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisProdukActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus ${jenisProduk.jenis_produk}"
            tvBodyKonfirmasi.text = "Jenis Produk ini akan menghapus produk yang terkait dengan jenis produk ini"

            btnKonfirmasi.setOnClickListener {
                postHapusJenisProduk(jenisProduk.id_jenis_produk.toString())
                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusJenisProduk(idJenisProduk: String) {
        viewModel.postDeleteProduk(idJenisProduk)
    }

    private fun getHapusJenisProduk(){
        viewModel.getDeleteProduk().observe(this@AdminJenisProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisProdukActivity)
                is UIState.Failure-> setFailureDeleteJenisProduk(result.message)
                is UIState.Success-> setSuccessDeleteJenisProduk(result.data)
            }
        }
    }

    private fun setSuccessDeleteJenisProduk(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@AdminJenisProdukActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
            fetchJenisProduk()
        } else{
            Toast.makeText(this@AdminJenisProdukActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureDeleteJenisProduk(message: String) {
        Toast.makeText(this@AdminJenisProdukActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setShowDialogTambah() {
        val view = AlertDialogAdminJenisProdukBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminJenisProdukActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            btnSimpan.setOnClickListener {
                var cek = true
                if(etJenisProduk.text.toString().trim().isEmpty()){
                    etJenisProduk.error = "Tidak Boleh Kosong"
                    cek = false
                }

                if(cek){
                    postTambahJenisProduk(etJenisProduk.text.toString().trim())

                    dialogInputan.dismiss()
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahJenisProduk(jenisProduk: String) {
        viewModel.postTambahProduk(jenisProduk)
    }

    private fun getTambahJenisProduk(){
        viewModel.getTambahProduk().observe(this@AdminJenisProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminJenisProdukActivity)
                is UIState.Failure-> setFailureTambahJenisProduk(result.message)
                is UIState.Success-> setSuccessTambahJenisProduk(result.data)
            }
        }
    }

    private fun setFailureTambahJenisProduk(message: String) {
        Toast.makeText(this@AdminJenisProdukActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahJenisProduk(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@AdminJenisProdukActivity, "Berhasil", Toast.LENGTH_SHORT).show()
            fetchJenisProduk()
        } else{
            Toast.makeText(this@AdminJenisProdukActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminJenisProdukActivity, AdminMainActivity::class.java))
        finish()
    }
}