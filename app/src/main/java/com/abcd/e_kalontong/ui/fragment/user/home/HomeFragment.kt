package com.abcd.e_kalontong.ui.fragment.user.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.PesananAdapter
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.databinding.AlertDialogKonfirmasiBinding
import com.abcd.e_kalontong.databinding.AlertDialogPesanProdukBinding
import com.abcd.e_kalontong.databinding.AlertDialogShowImageBinding
import com.abcd.e_kalontong.databinding.FragmentHomeBinding
import com.abcd.e_kalontong.ui.activity.user.main.MainActivity
import com.abcd.e_kalontong.ui.activity.user.produk.search.SearchProdukActivity
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KontrolNavigationDrawer
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import com.abcd.e_kalontong.utils.network.UIState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    @Inject
    lateinit var loading: LoadingAlertDialog
    @Inject
    lateinit var rupiah: KonversiRupiah
    private lateinit var pesananAdapter: PesananAdapter
    private var listPesanan: ArrayList<PesananModel> = ArrayList()

    private lateinit var activityContext: Activity
    private lateinit var context: Context
    private lateinit var contextLifecycleOwner: LifecycleOwner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        activityContext = (activity as MainActivity)
        context = requireContext().applicationContext
        contextLifecycleOwner = viewLifecycleOwner

        setSharedPreferencesLogin()
        setButton()
        fetchPesanan()
        getPesanan()
        getDeletePesanan()
        getUpdatePesanan()
        setSwipeRefreshLayout()

        return binding.root
    }

    private fun setSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
                fetchPesanan()
            }, 1500)
        }
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(context)
    }

    private fun setButton() {
        binding.apply {
            srcData.setOnClickListener {
                startActivity(Intent(context, SearchProdukActivity::class.java))
            }
            btnProduk.setOnClickListener {
                (activity as MainActivity).clickProduk()
            }
            btnRiwayat.setOnClickListener {
                (activity as MainActivity).clickRiwayat()
            }
            btnAkun.setOnClickListener {
                (activity as MainActivity).clickAccount()
            }
            btnPesan.setOnClickListener {
//                showDialogPesan(listPesanan)
//                val i = Intent(context, PaymentActivity::class.java)
//                i.putParcelableArrayListExtra("pesanan", listPesanan)
//                startActivity(i)
            }
        }
    }

    private fun fetchPesanan() {
        viewModel.fetchPesanan(sharedPreferencesLogin.getIdUser())
    }

    private fun getPesanan() {
        viewModel.getPesanan().observe(contextLifecycleOwner){result->
            when(result){
                is UIState.Loading-> setStartShimmerProduk()
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Log.d("DetailTAG", "setFailureFetchPesanan: $message")
        setNoHaveData()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<PesananModel>) {
        if(data.isNotEmpty()){
            setAdapter(data)
            setHaveData()

            listPesanan = data
        } else{
            setNoHaveData()
        }
    }

    private fun setAdapter(data: ArrayList<PesananModel>) {
        pesananAdapter = PesananAdapter(data, object : OnClickItem.ClickPesanan{
            override fun clickItemPesanan(pesanan: PesananModel, it: View) {
                val popupMenu = PopupMenu(context, it)
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
            rvPesanan.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvPesanan.adapter = pesananAdapter
        }

    }

    private fun setShowDialogDelete(pesanan: PesananModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(activityContext)
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
        viewModel.getDeletePesanan().observe(contextLifecycleOwner){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(activityContext)
                is UIState.Success-> setSuccessDeletePesanan(result.data)
                is UIState.Failure-> setFailureDeletePesanan(result.message)
            }
        }
    }

    private fun setFailureDeletePesanan(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessDeletePesanan(data: ResponseModel) {
        if(data.status=="0"){
            Toast.makeText(context, "Berhasil hapus", Toast.LENGTH_SHORT).show()
            fetchPesanan()
        } else{
            Toast.makeText(context, "${data.message_response}", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setShowDialogEdit(pesanan: PesananModel) {
        val view = AlertDialogPesanProdukBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(activityContext)
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
                    Toast.makeText(context, "Tidak Boleh Bernilai 0", Toast.LENGTH_SHORT).show()
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
        viewModel.getUpdatePesanan().observe(contextLifecycleOwner){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(activityContext)
                is UIState.Success-> setSuccessUpdatePesanan(result.data)
                is UIState.Failure-> setFailureUpdatePesanan(result.message)
            }
        }
    }

    private fun setFailureUpdatePesanan(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdatePesanan(data: ResponseModel) {
        if(data.status=="0"){
            Toast.makeText(context, "Berhasil Update", Toast.LENGTH_SHORT).show()
            fetchPesanan()
        } else{
            Toast.makeText(context, "${data.message_response}", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
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
            .load(gambar) // URL Gambar
            .placeholder(R.drawable.loading)
            .error(R.drawable.icon_product_home)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setNoHaveData(){
        binding.apply {
            rvPesanan.visibility = View.GONE
            btnPesan.visibility = View.GONE

            tvNotHavePesanan.visibility = View.VISIBLE
            setStopShimmerProduk()
        }
    }

    private fun setHaveData(){
        binding.apply {
            rvPesanan.visibility = View.VISIBLE
            btnPesan.visibility = View.VISIBLE

            tvNotHavePesanan.visibility = View.GONE
            setStopShimmerProduk()
        }
    }


    private fun setStartShimmerProduk(){
        binding.apply {
            smPesanan.startShimmer()
            rvPesanan.visibility = View.GONE
            smPesanan.visibility = View.VISIBLE

            btnPesan.visibility = View.GONE
        }
    }

    private fun setStopShimmerProduk(){
        binding.apply {
            smPesanan.stopShimmer()
            smPesanan.visibility = View.GONE
        }
    }

}