package com.abcd.e_kalontong.ui.activity.admin.kasir

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.PesananAdapter
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.databinding.ActivityAdminKasirBinding
import com.abcd.e_kalontong.databinding.AlertDialogKonfirmasiBinding
import com.abcd.e_kalontong.databinding.AlertDialogPesanProdukBinding
import com.abcd.e_kalontong.databinding.AlertDialogShowImageBinding
import com.abcd.e_kalontong.ui.activity.user.produk.search.SearchProdukActivity
import com.abcd.e_kalontong.utils.KontrolNavigationDrawer
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.network.UIState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AdminKasirActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminKasirBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    @Inject lateinit var loading: LoadingAlertDialog
    @Inject lateinit var rupiah: KonversiRupiah
    private val viewModel: AdminKasirViewModel by viewModels()
    private var listPesanan : ArrayList<PesananModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminKasirBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        getPesan()
        fetchPesananKasir()
        getPesananKasir()
        getUpdatePesanan()
        getDeletePesanan()
    }

    @SuppressLint("SetTextI18n")
    private fun setKontrolNavigationDrawer() {
        binding.apply {
            appNavbarDrawer.apply{
                ivNavDrawer.visibility = View.VISIBLE
                ivBack.visibility = View.GONE
                tvTitle.text = "Kasir"
            }
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminKasirActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, appNavbarDrawer.ivNavDrawer, this@AdminKasirActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            fabAddProduk.setOnClickListener{
                startActivity(Intent(this@AdminKasirActivity, SearchProdukActivity::class.java))
                finish()
            }
            btnBayar.setOnClickListener {
                if(listPesanan.isNotEmpty()){
                    showClickKonfirmasi()
                }else {
                    Toast.makeText(this@AdminKasirActivity, "Pesanan Belum Ada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showClickKonfirmasi() {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminKasirActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Pesanan Dari Kasir"
            tvBodyKonfirmasi.text = "Apakah pembeli telah melakukan pembayaran?"

            btnKonfirmasi.setOnClickListener {
                postPesan()
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postPesan(){
        viewModel.postPesan()
    }

    private fun getPesan(){
        viewModel.getPesan().observe(this@AdminKasirActivity){result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@AdminKasirActivity)
                is UIState.Success-> setSuccessPostPesan(result.data)
                is UIState.Failure-> setFailurePostPesan(result.message)
            }
        }
    }

    private fun setFailurePostPesan(message: String) {
        Toast.makeText(this@AdminKasirActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessPostPesan(data: ResponseModel) {
        if(data.status == "0"){
            Toast.makeText(this@AdminKasirActivity, "Berhasil Pesan", Toast.LENGTH_SHORT).show()
            fetchPesananKasir()
        } else{
            Toast.makeText(this@AdminKasirActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }

        loading.alertDialogCancel()
    }

    private fun fetchPesananKasir(){
        viewModel.fetchPesananKasir()
    }

    private fun getPesananKasir(){
        viewModel.getPesananKasir().observe(this@AdminKasirActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmerPesanan()
                is UIState.Success-> setSuccessFetchPesanan(result.data)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(this@AdminKasirActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmerPesanan()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<PesananModel>) {
        listPesanan = arrayListOf()
        if(data.isNotEmpty()){
            listPesanan.addAll(data)
            setHargaTotalProduk(data)
        } else{
            Toast.makeText(this@AdminKasirActivity, "Belum ada pesanan", Toast.LENGTH_SHORT).show()
            binding.tvTotalTagihan.text = rupiah.rupiah(0)
        }
        setAdapter(listPesanan)
        setStopShimmerPesanan()
    }

    private fun setAdapter(data: ArrayList<PesananModel>) {
        val adapter = PesananAdapter(data, object: OnClickItem.ClickPesanan{
            override fun clickItemPesanan(pesanan: PesananModel, it: View) {
                val popupMenu = PopupMenu(this@AdminKasirActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(pesanan)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogDelete(pesanan)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

            override fun clickGambarPesanan(gambar: String, produk: String, it: View) {
                setShowImage(gambar, produk)
            }

        })
        binding.apply {
            rvPesanan.layoutManager = LinearLayoutManager(this@AdminKasirActivity, LinearLayoutManager.VERTICAL, false)
            rvPesanan.adapter = adapter
        }
    }

    private fun setHargaTotalProduk(data: ArrayList<PesananModel>){
        if(data.size>0){
            var totalHarga = 0.0

            Log.d("MainActivityTag", "setData: $data")

            for (value in data){
                val harga = value.produk!!.harga!!.toInt()
                val namaProduk = value.produk.produk
                val jumlah = value.jumlah!!.trim().toInt()

                val vHarga = harga*jumlah

                totalHarga += vHarga
                Log.d("PaymentActivityTAG", "set: " +
                        "harga: $totalHarga, namaProduk: $namaProduk ")
            }
            binding.apply {
                setAdapter(data)
                tvTotalTagihan.text = rupiah.rupiah(totalHarga.toLong())
            }
        }
    }

    private fun setShowImage(gambar: String, produk: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminKasirActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitle.text = produk
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

        Glide.with(this@AdminKasirActivity)
            .load(gambar) // URL Gambar
            .placeholder(R.drawable.loading)
            .error(R.drawable.icon_product_home)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan
    }

    @SuppressLint("SetTextI18n")
    private fun setShowDialogDelete(pesanan: PesananModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminKasirActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Delete Pesanan?"
            tvBodyKonfirmasi.text = "Pesanan yang anda pilih akan terhapus"

            btnKonfirmasi.setOnClickListener {
                dialogInputan.dismiss()
                postDeletePesanan(pesanan.id_pesanan!!)
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

    }

    private fun postDeletePesanan(idPesanan: Int) {
        viewModel.postDeletePesanan(idPesanan)
    }

    private fun getDeletePesanan(){
        viewModel.getDeletePesanan().observe(this@AdminKasirActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminKasirActivity)
                is UIState.Success-> setSuccessDeletePesanan(result.data)
                is UIState.Failure-> setFailureDeletePesanan(result.message)
            }
        }
    }

    private fun setFailureDeletePesanan(message: String) {
        Toast.makeText(this@AdminKasirActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessDeletePesanan(data: ResponseModel) {
        if(data.status=="0"){
            Toast.makeText(this@AdminKasirActivity, "Berhasil hapus", Toast.LENGTH_SHORT).show()
            fetchPesananKasir()
        } else{
            Toast.makeText(this@AdminKasirActivity, "${data.message_response}", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setShowDialogEdit(pesanan: PesananModel) {
        val view = AlertDialogPesanProdukBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminKasirActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvProduk.text = pesanan.produk!!.produk
            tvJenisProduk.text = pesanan.produk.jenis_produk!!.jenis_produk
            tvHarga.text = rupiah.rupiah(pesanan.produk.harga!!.toLong())
            tvJumlah.text = pesanan.jumlah

            btnTambah.setOnClickListener {
                var jumlah = tvJumlah.text.toString().toInt()
                if(jumlah < pesanan.produk.stok!!){
                    jumlah+=1
                    tvJumlah.text = jumlah.toString()
                }
            }
            btnKurang.setOnClickListener {
                var jumlah = tvJumlah.text.toString().toInt()
                if (jumlah > 1){
                    jumlah-=1
                    tvJumlah.text = jumlah.toString()
                }
            }

            btnSimpan.setOnClickListener {
                var cek = false
                if(tvJumlah.text.toString().trim() == "0"){
                    Toast.makeText(this@AdminKasirActivity, "Tidak Boleh Bernilai 0", Toast.LENGTH_SHORT).show()
                    cek = true
                }

                if(!cek){
                    dialogInputan.dismiss()
                    val idPesanan = pesanan.id_pesanan!!
                    val jumlah = tvJumlah.text.toString().trim()

                    postUpdatePesanan(idPesanan, jumlah)
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdatePesanan(idPesanan: Int, jumlah: String) {
        viewModel.postUpdatePesanan(idPesanan, jumlah)
    }

    private fun getUpdatePesanan(){
        viewModel.getUpdatePesanan().observe(this@AdminKasirActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminKasirActivity)
                is UIState.Success-> setSuccessUpdatePesanan(result.data)
                is UIState.Failure-> setFailureUpdatePesanan(result.message)
            }
        }
    }

    private fun setFailureUpdatePesanan(message: String) {
        Toast.makeText(this@AdminKasirActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdatePesanan(data: ResponseModel) {
        if(data.status=="0"){
            Toast.makeText(this@AdminKasirActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
            fetchPesananKasir()
        } else{
            Toast.makeText(this@AdminKasirActivity, "${data.message_response}", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setStartShimmerPesanan(){
        binding.apply {
            smPesanan.startShimmer()
            rvPesanan.visibility = View.GONE
            smPesanan.visibility = View.VISIBLE

            clBuatPesanan.visibility = View.GONE
        }
    }

    private fun setStopShimmerPesanan(){
        binding.apply {
            smPesanan.stopShimmer()
            smPesanan.visibility = View.GONE
            rvPesanan.visibility = View.VISIBLE
            clBuatPesanan.visibility = View.VISIBLE
        }
    }

}