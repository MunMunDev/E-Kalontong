package com.abcd.e_kalontong.ui.activity.admin.riwayat_pesanan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import com.abcd.e_kalontong.databinding.AlertDialogAdminPrintLaporanBinding
import com.abcd.e_kalontong.ui.activity.admin.main.AdminMainActivity
import com.abcd.e_kalontong.ui.activity.admin.riwayat_pesanan.detail.AdminRiwayatPesananDetailActivity
import com.abcd.e_kalontong.ui.activity.admin.riwayat_pesanan.print.AdminPrintLaporanActivity
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

    private lateinit var tahun: ArrayList<String>
    private lateinit var bulan: ArrayList<String>
    private lateinit var tanggal: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRiwayatPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchTahun()
        getTahun()
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

    private fun setButton() {
        binding.apply {
            btnPrint.setOnClickListener {
                setShowDialogPrintLaporan()
            }
        }
    }

    private fun fetchTahun(){
        viewModel.fetchTahunRiwayatPesanan()
    }

    private fun getTahun(){
        viewModel.getTahunRiwayatPesanan().observe(this@AdminRiwayatPesananActivity){result->
            when(result){
                is UIState.Loading->{}
                is UIState.Failure-> setFailureTahun(result.message)
                is UIState.Success-> setSuccessTahun(result.data)
            }
        }
    }

    private fun setFailureTahun(message: String) {
        Toast.makeText(this@AdminRiwayatPesananActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTahun(data: ArrayList<String>) {
        if(data.isNotEmpty()){
            tahun = data
        } else{

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
                val i = Intent(this@AdminRiwayatPesananActivity, AdminRiwayatPesananDetailActivity::class.java)
                i.putExtra("nama", nama)
                i.putParcelableArrayListExtra("pesanan", pesanan)
                startActivity(i)
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

    private fun setArray(){
        bulan = arrayListOf(
            "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli",
            "Agustus", "September", "Oktober", "November", "Desember"
        )
        tanggal = arrayListOf(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
        )
    }

    private fun setShowDialogPrintLaporan() {
        val view = AlertDialogAdminPrintLaporanBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPesananActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        setArray()

        var selectedValueMulaiTahun = ""
        var selectedPositionMulaiBulan = 0
        var selectedValueMulaiTanggal = ""

        var selectedValueSampaiTahun = ""
        var selectedPositionSampaiBulan = 0
        var selectedValueSampaiTanggal = ""

        view.apply {
            val arrayAdapterMulaiTahun = ArrayAdapter(
                this@AdminRiwayatPesananActivity,
                android.R.layout.simple_spinner_item,
                tahun
            )
            arrayAdapterMulaiTahun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spMulaiTahun.adapter = arrayAdapterMulaiTahun
            spMulaiTahun.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedValueMulaiTahun = spMulaiTahun.selectedItem.toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spMulaiTahun.adapter = arrayAdapterMulaiTahun

            val arrayAdapterMulaiBulan = ArrayAdapter(
                this@AdminRiwayatPesananActivity,
                android.R.layout.simple_spinner_item,
                bulan
            )
            arrayAdapterMulaiBulan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spMulaiBulan.adapter = arrayAdapterMulaiBulan
            spMulaiBulan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedPositionMulaiBulan = position
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spMulaiBulan.adapter = arrayAdapterMulaiBulan

            val arrayAdapterMulaiTanggal = ArrayAdapter(
                this@AdminRiwayatPesananActivity,
                android.R.layout.simple_spinner_item,
                tanggal
            )
            arrayAdapterMulaiTanggal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spMulaiTanggal.adapter = arrayAdapterMulaiTanggal
            spMulaiTanggal.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedValueMulaiTanggal = spMulaiTanggal.selectedItem.toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spMulaiTanggal.adapter = arrayAdapterMulaiTanggal


            // Sampai
            val arrayAdapterSampaiTahun = ArrayAdapter(
                this@AdminRiwayatPesananActivity,
                android.R.layout.simple_spinner_item,
                tahun
            )
            arrayAdapterSampaiTahun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spSampaiTahun.adapter = arrayAdapterSampaiTahun
            spSampaiTahun.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedValueSampaiTahun = spSampaiTahun.selectedItem.toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spSampaiTahun.adapter = arrayAdapterSampaiTahun

            val arrayAdapterSampaiBulan = ArrayAdapter(
                this@AdminRiwayatPesananActivity,
                android.R.layout.simple_spinner_item,
                bulan
            )
            arrayAdapterSampaiBulan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spSampaiBulan.adapter = arrayAdapterSampaiBulan
            spSampaiBulan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedPositionSampaiBulan = position
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spSampaiBulan.adapter = arrayAdapterSampaiBulan

            val arrayAdapterSampaiTanggal = ArrayAdapter(
                this@AdminRiwayatPesananActivity,
                android.R.layout.simple_spinner_item,
                tanggal
            )
            arrayAdapterSampaiTanggal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spSampaiTanggal.adapter = arrayAdapterSampaiTanggal
            spSampaiTanggal.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedValueSampaiTanggal = spSampaiTanggal.selectedItem.toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            spSampaiTanggal.adapter = arrayAdapterSampaiTanggal

            btnPrint.setOnClickListener {
                val mulaiBulan = selectedPositionMulaiBulan+1
                val sampaiBulan = selectedPositionSampaiBulan+1

                val mulaiTanggal = "$selectedValueMulaiTahun-$mulaiBulan-$selectedValueMulaiTanggal 00:00:00"
                val sampaiTanggal = "$selectedValueSampaiTahun-$sampaiBulan-$selectedValueSampaiTanggal 23:59:59"

                val i = Intent(this@AdminRiwayatPesananActivity, AdminPrintLaporanActivity::class.java)
                i.putExtra("mulai_tanggal", mulaiTanggal)
                i.putExtra("sampai_tanggal", sampaiTanggal)
                startActivity(i)
                dialogInputan.dismiss()
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminRiwayatPesananActivity, AdminMainActivity::class.java))
        finish()
    }
}