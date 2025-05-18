package com.abcd.e_kalontong.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.data.model.UserModel
import com.abcd.e_kalontong.databinding.ListAdminAkunBinding
import com.abcd.e_kalontong.utils.OnClickItem

class AdminAkunAdapter(
    private var listSemuaUser: ArrayList<UserModel>,
    private var onClick: OnClickItem.ClickAkun
): RecyclerView.Adapter<AdminAkunAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListAdminAkunBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminAkunBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listSemuaUser.size+1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvNama.text = "Nama User"
                tvNomorHp.text = "Nomor Hp"
                tvUsername.text = "Username"
                tvPassword.text = "Password"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvNama.setBackgroundResource(R.drawable.bg_table_title)
                tvNomorHp.setBackgroundResource(R.drawable.bg_table_title)
                tvUsername.setBackgroundResource(R.drawable.bg_table_title)
                tvPassword.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvNama.setTextColor(Color.parseColor("#ffffff"))
                tvNomorHp.setTextColor(Color.parseColor("#ffffff"))
                tvUsername.setTextColor(Color.parseColor("#ffffff"))
                tvPassword.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvNama.setTypeface(null, Typeface.BOLD)
                tvNomorHp.setTypeface(null, Typeface.BOLD)
                tvUsername.setTypeface(null, Typeface.BOLD)
                tvPassword.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)

            }
            else{
                val semuaUser = listSemuaUser[(position-1)]


                tvNo.text = "$position"
                tvNama.text = semuaUser.nama
                tvNomorHp.text = semuaUser.nomor_hp
                tvUsername.text = semuaUser.username
                tvPassword.text = semuaUser.password
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvNama.setBackgroundResource(R.drawable.bg_table)
                tvNomorHp.setBackgroundResource(R.drawable.bg_table)
                tvUsername.setBackgroundResource(R.drawable.bg_table)
                tvPassword.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvNama.setTextColor(Color.parseColor("#000000"))
                tvNomorHp.setTextColor(Color.parseColor("#000000"))
                tvUsername.setTextColor(Color.parseColor("#000000"))
                tvPassword.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvNama.setTypeface(null, Typeface.NORMAL)
                tvNomorHp.setTypeface(null, Typeface.NORMAL)
                tvUsername.setTypeface(null, Typeface.NORMAL)
                tvPassword.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(semuaUser, it)
                }
            }
        }
    }
}