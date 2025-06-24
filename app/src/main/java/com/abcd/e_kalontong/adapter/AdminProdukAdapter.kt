package com.abcd.e_kalontong.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.databinding.ListAdminProdukBinding
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.OnClickItem
import com.bumptech.glide.Glide

class AdminProdukAdapter(
    private var listProduk: ArrayList<ProdukModel>,
    private var onClick: OnClickItem.ClickAdminProduk
): RecyclerView.Adapter<AdminProdukAdapter.ViewHolder>() {
    private var konversiRupiah: KonversiRupiah = KonversiRupiah()

    class ViewHolder(val binding: ListAdminProdukBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminProdukBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listProduk.size+1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvJenisProduk.text = "Jenis Produk"
                tvProduk.text = "Produk"
                tvHarga.text = "Harga"
                tvStok.text = "Stok"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJenisProduk.setBackgroundResource(R.drawable.bg_table_title)
                tvProduk.setBackgroundResource(R.drawable.bg_table_title)
                tvStok.setBackgroundResource(R.drawable.bg_table_title)
                tvHarga.setBackgroundResource(R.drawable.bg_table_title)
                ivGambarProduk.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJenisProduk.setTextColor(Color.parseColor("#ffffff"))
                tvProduk.setTextColor(Color.parseColor("#ffffff"))
                tvStok.setTextColor(Color.parseColor("#ffffff"))
                tvHarga.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJenisProduk.setTypeface(null, Typeface.BOLD)
                tvProduk.setTypeface(null, Typeface.BOLD)
                tvStok.setTypeface(null, Typeface.BOLD)
                tvHarga.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val vProduk = listProduk[(position-1)]
                val jenisProduk = vProduk.jenis_produk!!.jenis_produk
                val produk = vProduk.produk
                val stok = vProduk.stok
                val gambar = vProduk.gambar!!

                tvNo.text = "$position"
                tvJenisProduk.text = jenisProduk
                tvProduk.text = produk
                tvStok.text = stok.toString()
                tvHarga.text = konversiRupiah.rupiah(vProduk.harga!!.toLong())
                Glide.with(holder.itemView)
                    .load("${Constant.LOCATION_GAMBAR}${gambar}")
                    .error(R.drawable.gambar_not_have_image)
                    .into(ivGambarProduk)
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJenisProduk.setBackgroundResource(R.drawable.bg_table)
                tvProduk.setBackgroundResource(R.drawable.bg_table)
                tvStok.setBackgroundResource(R.drawable.bg_table)
                tvHarga.setBackgroundResource(R.drawable.bg_table)
                ivGambarProduk.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJenisProduk.setTextColor(Color.parseColor("#000000"))
                tvProduk.setTextColor(Color.parseColor("#000000"))
                tvStok.setTextColor(Color.parseColor("#000000"))
                tvHarga.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJenisProduk.setTypeface(null, Typeface.NORMAL)
                tvProduk.setTypeface(null, Typeface.NORMAL)
                tvStok.setTypeface(null, Typeface.NORMAL)
                tvHarga.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvJenisProduk.setOnClickListener {
                    onClick.clickItemJenisProduk("Jenis Produk", vProduk.jenis_produk.jenis_produk!!, it)
                }
                tvProduk.setOnClickListener {
                    onClick.clickItemProduk("Produk", vProduk.produk!!, it)
                }
                tvHarga.setOnClickListener {
                    onClick.clickItemHargaProduk("Harga Produk", vProduk.harga, it)
                }
                ivGambarProduk.setOnClickListener {
                    onClick.clickItemGambarProduk(vProduk.produk!!, gambar, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(vProduk, it)
                }
            }
        }
    }
}