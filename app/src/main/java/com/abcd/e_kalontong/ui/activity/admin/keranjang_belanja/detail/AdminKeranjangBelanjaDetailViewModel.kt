package com.abcd.e_kalontong.ui.activity.admin.keranjang_belanja.detail

import androidx.lifecycle.ViewModel
import com.abcd.e_kalontong.data.database.api.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminKeranjangBelanjaDetailViewModel @Inject constructor(
    private val api: ApiService
): ViewModel(){

}