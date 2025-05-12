package com.abcd.e_kalontong.ui.fragment.user.product

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.ProdukAdapter
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.databinding.AlertDialogPesanProduk2Binding
import com.abcd.e_kalontong.databinding.AlertDialogPesanProdukBinding
import com.abcd.e_kalontong.databinding.AlertDialogShowImageBinding
import com.abcd.e_kalontong.databinding.FragmentProductBinding
import com.abcd.e_kalontong.ui.activity.user.main.MainActivity
import com.abcd.e_kalontong.ui.activity.user.produk.search.SearchProdukActivity
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import com.abcd.e_kalontong.utils.network.UIState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private lateinit var binding : FragmentProductBinding
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var activityContext: Activity
    private lateinit var context: Context
    private lateinit var contextView: LifecycleOwner
    @Inject
    lateinit var loading: LoadingAlertDialog
    @Inject
    lateinit var rupiah: KonversiRupiah
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(layoutInflater)
        activityContext = (activity as MainActivity)
        context = this.requireContext().applicationContext
        contextView = viewLifecycleOwner

        setSharedPreferencesLogin()
        setTopAppBar()
        fetchProduk()
        getProduk()
        getPesan()
        setSwipeRefreshLayout()

        return binding.root
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(context)
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
                fetchProduk()
            }, 2000)
        }
    }

    private fun setTopAppBar() {
        binding.topAppBar.apply {
            llSearchProduk.setOnClickListener {
                val i = Intent(context, SearchProdukActivity::class.java)
                i.putExtra("id_check", 0)
                startActivity(i)
            }
        }
    }

    private fun fetchProduk(){
        viewModel.fetchProduk()
    }

    private fun getProduk() {
        viewModel.getProduk().observe(this.viewLifecycleOwner){result->
            when(result){
                is UIState.Loading-> setStartShimmerProduk()
                is UIState.Success-> setSuccessFetchProduk(result.data)
                is UIState.Failure-> setFailureFetchProduk(result.message)
            }
        }
    }

    private fun setFailureFetchProduk(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        setStopShimmerProduk()
    }

    private fun setSuccessFetchProduk(data: ArrayList<ProdukModel>) {
        if(data.isNotEmpty()){
            setAdapterProduk(data)
        } else{
            Toast.makeText(context, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerProduk()
    }

    private fun setAdapterProduk(data: ArrayList<ProdukModel>) {
        val adapter = ProdukAdapter(data, object : OnClickItem.ClickProduk{
            override fun clickItemPesan(produk: ProdukModel, it: View) {
                setShowDialogPesan(produk)
            }
        })

        binding.rvProduk.let {
            it.layoutManager = GridLayoutManager(context, 2)
//            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    private fun setShowImage(gambar: String, produk: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(activityContext)
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

        Glide.with(context)
            .load("${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .placeholder(R.drawable.loading)
            .error(R.drawable.icon_product_home)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setShowDialogPesan(produk: ProdukModel) {
        val view = AlertDialogPesanProduk2Binding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(activityContext)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvProduk.text = produk.produk!!
            tvJenisProduk.text = produk.jenis_produk!!.jenis_produk
            tvHarga.text = rupiah.rupiah(produk.harga!!.toLong())

            btnTambah.setOnClickListener {
                var jumlah = tvJumlah.text.toString().toInt()
                if(jumlah < produk.stok!!){
                    jumlah+=1
                    tvJumlah.text = jumlah.toString()
                }
            }
            btnKurang.setOnClickListener {
                var jumlah = tvJumlah.text.toString().toInt()
                if (jumlah > 0){
                    jumlah-=1
                    tvJumlah.text = jumlah.toString()
                }
            }

            Glide.with(context)
                .load("${Constant.LOCATION_GAMBAR}${produk.gambar}") // URL Gambar
                .placeholder(R.drawable.loading)
                .error(R.drawable.icon_product_home)
                .into(view.ivGambarProduk) // imageView mana yang akan diterapkan

            btnSimpan.setOnClickListener {
                var cek = false
                if(tvJumlah.text.toString().trim() == "0"){
                    Toast.makeText(context, "Tidak Boleh Bernilai 0", Toast.LENGTH_SHORT).show()
                    cek = true
                }

                if(!cek){
                    dialogInputan.dismiss()
                    val idUser = sharedPreferencesLogin.getIdUser()
                    val idProduk = produk.id_produk!!
                    val jumlah = tvJumlah.text.toString().trim()

                    postPesan(idUser, idProduk, jumlah)
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postPesan(idUser: Int, idProduk: Int, jumlah: String) {
        viewModel.postPesan(idUser, idProduk, jumlah)
    }

    private fun getPesan(){
        viewModel.getPesan().observe(viewLifecycleOwner){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(activityContext)
                is UIState.Success-> setSuccessPostPesan(result.data)
                is UIState.Failure-> setFailurePostPesan(result.message)
            }
        }
    }

    private fun setFailurePostPesan(message: String) {
        Toast.makeText(context, "Gagal pesan : $message", Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessPostPesan(data: ResponseModel) {
        if(data.status == "0"){
            Toast.makeText(context, "Berhasil Pesan", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(context, data.message_response, Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setStartShimmerProduk(){
        binding.apply {
            smProduk.startShimmer()
            rvProduk.visibility = View.GONE
            smProduk.visibility = View.VISIBLE
        }
    }

    private fun setStopShimmerProduk(){
        binding.apply {
            smProduk.stopShimmer()
            rvProduk.visibility = View.VISIBLE
            smProduk.visibility = View.GONE
        }
    }
}