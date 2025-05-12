package com.abcd.e_kalontong.ui.fragment.user.product

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.ProdukAdapter
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.databinding.FragmentProductBinding
import com.abcd.e_kalontong.ui.activity.produk.search.SearchProdukActivity
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class ProductFragment : Fragment() {
    private lateinit var binding : FragmentProductBinding
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var context: Context
    private lateinit var contextView: LifecycleOwner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(layoutInflater)
        context = this.requireContext().applicationContext
        contextView = viewLifecycleOwner

        setTopAppBar()
        fetchProduk()
        getProduk()
        setSwipeRefreshLayout()

        return binding.root
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

            }

            override fun clickGambarProduk(gambar: String, produk: String, it: View) {

            }
        }, false)

        binding.rvProduk.let {
            it.layoutManager = GridLayoutManager(context, 2)
//            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
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