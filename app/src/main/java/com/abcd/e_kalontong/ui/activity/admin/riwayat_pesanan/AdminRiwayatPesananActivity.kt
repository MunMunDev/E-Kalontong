package com.abcd.e_kalontong.ui.activity.admin.riwayat_pesanan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.AdminRiwayatPesananAdapter
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.RiwayatPesananHalModel
import com.abcd.e_kalontong.databinding.ActivityAdminRiwayatPesananBinding
import com.abcd.e_kalontong.ui.activity.admin.main.AdminMainActivity
import com.abcd.e_kalontong.utils.KontrolNavigationDrawer
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminRiwayatPesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminRiwayatPesananBinding
    private val viewModel: AdminRiwayatPesananViewModel by viewModels()
    private lateinit var adapter: AdminRiwayatPesananAdapter
    @Inject
    lateinit var loading: LoadingAlertDialog
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRiwayatPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        fetchData()
        getPesanan()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminRiwayatPesananActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminRiwayatPesananActivity)
        }
    }

    private fun fetchData() {
        viewModel.fetchRiwayatPesanan()
    }
    private fun getPesanan(){
        viewModel.getRiwayatPesanan().observe(this@AdminRiwayatPesananActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminRiwayatPesananActivity)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(this@AdminRiwayatPesananActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<RiwayatPesananHalModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminRiwayatPesananActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<RiwayatPesananHalModel>) {
        adapter = AdminRiwayatPesananAdapter(data, object: OnClickItem.ClickAdminRiwayatPesanan{
            override fun clickItem(
                pesanan: ArrayList<RiwayatPesananModel>,
                nama: String,
                it: View
            ) {
//                val i = Intent(this@AdminRiwayatPesananActivity, AdminRiwayatPesananDetailActivity::class.java)
//                i.putExtra("nama", nama)
//                i.putParcelableArrayListExtra("pesanan", pesanan)
//                startActivity(i)
            }
        })

        binding.apply {
            rvPesanan.layoutManager = LinearLayoutManager(
                this@AdminRiwayatPesananActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvPesanan.adapter = adapter
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminRiwayatPesananActivity, AdminMainActivity::class.java))
        finish()
    }
}