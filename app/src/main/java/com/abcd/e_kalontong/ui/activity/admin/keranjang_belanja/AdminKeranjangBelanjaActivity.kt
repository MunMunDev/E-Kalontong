package com.abcd.e_kalontong.ui.activity.admin.keranjang_belanja

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.adapter.AdminKeranjangBelanjaAdapter
import com.abcd.e_kalontong.data.model.PesananHalModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.databinding.ActivityAdminKeranjangBelanjaBinding
import com.abcd.e_kalontong.ui.activity.admin.keranjang_belanja.detail.AdminKeranjangBelanjaDetailActivity
import com.abcd.e_kalontong.ui.activity.admin.main.AdminMainActivity
import com.abcd.e_kalontong.utils.KontrolNavigationDrawer
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AdminKeranjangBelanjaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminKeranjangBelanjaBinding
    private val viewModel: AdminKeranjangBelanjaViewModel by viewModels()
    private lateinit var adapter: AdminKeranjangBelanjaAdapter
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminKeranjangBelanjaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        fetchData()
        getPesanan()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminKeranjangBelanjaActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminKeranjangBelanjaActivity)
        }
    }

    private fun fetchData() {
        viewModel.fetchPesanan()
    }
    private fun getPesanan(){
        viewModel.getPesanan().observe(this@AdminKeranjangBelanjaActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminKeranjangBelanjaActivity)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(this@AdminKeranjangBelanjaActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<PesananHalModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminKeranjangBelanjaActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<PesananHalModel>) {
        adapter = AdminKeranjangBelanjaAdapter(data, object: OnClickItem.ClickAdminKeranjangBelanja{
            override fun clickItem(pesanan: ArrayList<PesananModel>, it: View) {
                val i = Intent(this@AdminKeranjangBelanjaActivity, AdminKeranjangBelanjaDetailActivity::class.java)
                i.putParcelableArrayListExtra("pesanan", pesanan)
                startActivity(i)
            }
        })

        binding.apply {
            rvPesanan.layoutManager = LinearLayoutManager(
                this@AdminKeranjangBelanjaActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvPesanan.adapter = adapter
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminKeranjangBelanjaActivity, AdminMainActivity::class.java))
        finish()
    }
}