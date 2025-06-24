package com.abcd.e_kalontong.ui.fragment.user.pesanan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.adapter.RiwayatPesananAdapter
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.databinding.FragmentPesananBinding
import com.abcd.e_kalontong.ui.activity.pesanan.detail.PesananDetailActivity
import com.abcd.e_kalontong.ui.activity.user.main.MainActivity
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PesananFragment : Fragment() {
    private lateinit var binding: FragmentPesananBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: PesananViewModel by viewModels()

    private lateinit var activityContext: Activity
    private lateinit var contextLifecycleOwner: LifecycleOwner

    private lateinit var listPesanan : ArrayList<RiwayatPesananModel>
    private lateinit var tempListPesanan : ArrayList<RiwayatPesananModel>

    private lateinit var listRiwayatPesanan : ArrayList<RiwayatPesananModel>
    private lateinit var tempListRiwayatPesanan : ArrayList<RiwayatPesananModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPesananBinding.inflate(layoutInflater)
        activityContext = (activity as MainActivity)
        contextLifecycleOwner = viewLifecycleOwner

        setSharedPreferencesLogin()
        fetchPesanan()
        getPesanan()
        fetchRiwayatPesanan()
        getRiwayatPesanan()

        return binding.root
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(activityContext)
    }

    private fun fetchPesanan() {
        viewModel.fetchPesanan(sharedPreferencesLogin.getIdUser().toString())
    }
    private fun getPesanan(){
        viewModel.getPesanan().observe(contextLifecycleOwner){ result->
            when(result){
                is UIState.Loading-> setStartShimmerPesanan()
                is UIState.Success-> setSuccessFetchPesanan(result.data)
                is UIState.Failure-> setFailureFetchPesanan(result.message)
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_SHORT).show()
        setStopShimmerPesanan()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<RiwayatPesananModel>) {
        tempListPesanan = data
        listPesanan = data

        if(data.isNotEmpty()){
            searchDataPesanan(data)
            setAdapterPesanan(listPesanan)
        } else{
            Toast.makeText(activityContext, "Tidak ada data Pesanan", Toast.LENGTH_SHORT).show()
        }

        setStopShimmerPesanan()
    }

    private fun setAdapterPesanan(data: ArrayList<RiwayatPesananModel>) {
        val adapter = RiwayatPesananAdapter(data, object : OnClickItem.ClickRiwayatPesanan{
            override fun clickItemRiwayatPesanan(riwayatPesanan: RiwayatPesananModel, it: View) {
                val pesanan = tempListPesanan.filter { it.id_pemesanan == riwayatPesanan.id_pemesanan } as ArrayList<RiwayatPesananModel>
                val i = Intent(activityContext, PesananDetailActivity::class.java)
                i.putParcelableArrayListExtra("pesanan", pesanan)
                startActivity(i)
            }
        })

        binding.apply {
            rvPesanan.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.VERTICAL, false)
            rvPesanan.adapter = adapter
        }
    }

    private fun fetchRiwayatPesanan() {
        viewModel.fetchRiwayatPesanan(sharedPreferencesLogin.getIdUser().toString())
    }
    private fun getRiwayatPesanan(){
        viewModel.getRiwayatPesanan().observe(contextLifecycleOwner){ result->
            when(result){
                is UIState.Loading-> setStartShimmerRiwayatPesanan()
                is UIState.Success-> setSuccessFetchRiwayatPesanan(result.data)
                is UIState.Failure-> setFailureFetchRiwayatPesanan(result.message)
            }
        }
    }

    private fun setFailureFetchRiwayatPesanan(message: String) {
        Toast.makeText(activityContext, message, Toast.LENGTH_SHORT).show()
        setStopShimmerRiwayatPesanan()
    }

    private fun setSuccessFetchRiwayatPesanan(data: ArrayList<RiwayatPesananModel>) {
        tempListRiwayatPesanan = data
        listRiwayatPesanan = data
        if(data.isNotEmpty()){
            searchDataRiwayatPesanan(data)
            setAdapterRiwayatPesanan(listRiwayatPesanan)
        } else{
            Toast.makeText(activityContext, "Tidak ada data Riwayat Pesanan", Toast.LENGTH_SHORT).show()
        }
        setStopShimmerRiwayatPesanan()
    }

    private fun setAdapterRiwayatPesanan(data: ArrayList<RiwayatPesananModel>) {
        val adapter = RiwayatPesananAdapter(data, object : OnClickItem.ClickRiwayatPesanan{
            override fun clickItemRiwayatPesanan(riwayatPesanan: RiwayatPesananModel, it: View) {
                val pesanan = tempListRiwayatPesanan.filter { it.id_pemesanan == riwayatPesanan.id_pemesanan } as ArrayList<RiwayatPesananModel>
                val i = Intent(activityContext, PesananDetailActivity::class.java)
                i.putParcelableArrayListExtra("pesanan", pesanan)
                startActivity(i)
            }
        })

        binding.apply {
            rvRiwayatPesanan.layoutManager = LinearLayoutManager(activityContext, LinearLayoutManager.VERTICAL, false)
            rvRiwayatPesanan.adapter = adapter
        }
    }

    private fun searchDataPesanan(data: ArrayList<RiwayatPesananModel>){
        listPesanan = data.distinctBy { it.id_pemesanan } as ArrayList<RiwayatPesananModel>
        Log.d("DetailTAG", "searchDataPesanan: ${listPesanan.size}")
        for(value in listPesanan){

        }
        for((no, value) in listPesanan.withIndex()){
            val searchData = tempListPesanan.filter { it.id_pemesanan == value.id_pemesanan } as ArrayList<RiwayatPesananModel>

            val jumlah = searchData.size
            val totalHarga = searchData.sumOf { it.harga!!.toInt() }

            listPesanan[no].harga = totalHarga.toString()
            listPesanan[no].jum_jenis_produk = "$jumlah Jenis Produk"
        }
    }

    private fun searchDataRiwayatPesanan(data: ArrayList<RiwayatPesananModel>){
        listRiwayatPesanan = data.distinctBy { it.id_pemesanan } as ArrayList<RiwayatPesananModel>
        for((no, value) in listRiwayatPesanan.withIndex()){
            val searchData = tempListRiwayatPesanan.filter { it.id_pemesanan == value.id_pemesanan } as ArrayList<RiwayatPesananModel>

            val jumlah = searchData.size
            val totalHarga = searchData.sumOf { it.harga!!.toInt() }

            listRiwayatPesanan[no].harga = totalHarga.toString()
            listRiwayatPesanan[no].jum_jenis_produk = "$jumlah Jenis Produk"
        }
    }

    private fun setStartShimmerPesanan(){
        binding.apply {
            smPesanan.startShimmer()
            rvPesanan.visibility = View.GONE
            smPesanan.visibility = View.VISIBLE
        }
    }

    private fun setStopShimmerPesanan(){
        binding.apply {
            smPesanan.stopShimmer()
            rvPesanan.visibility = View.VISIBLE
            smPesanan.visibility = View.GONE
        }
    }

    private fun setStartShimmerRiwayatPesanan(){
        binding.apply {
            smRiwayatPesanan.startShimmer()
            rvRiwayatPesanan.visibility = View.GONE
            smRiwayatPesanan.visibility = View.VISIBLE
        }
    }

    private fun setStopShimmerRiwayatPesanan(){
        binding.apply {
            smRiwayatPesanan.stopShimmer()
            rvRiwayatPesanan.visibility = View.VISIBLE
            smRiwayatPesanan.visibility = View.GONE
        }
    }

}