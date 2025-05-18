package com.abcd.e_kalontong.ui.activity.admin.produk

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.AdminProdukAdapter
import com.abcd.e_kalontong.data.model.JenisProdukModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.databinding.ActivityAdminProdukBinding
import com.abcd.e_kalontong.databinding.AlertDialogKeteranganBinding
import com.abcd.e_kalontong.databinding.AlertDialogKonfirmasiBinding
import com.abcd.e_kalontong.databinding.AlertDialogProdukBinding
import com.abcd.e_kalontong.databinding.AlertDialogShowImageBinding
import com.abcd.e_kalontong.ui.activity.admin.main.AdminMainActivity
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KontrolNavigationDrawer
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.network.UIState
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@AndroidEntryPoint
class AdminProdukActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminProdukBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: AdminProdukViewModel by viewModels()
    @Inject
    lateinit var loading: LoadingAlertDialog
    @Inject
    lateinit var rupiah: KonversiRupiah
    private lateinit var adapter: AdminProdukAdapter
    private var listJenisProduk = ArrayList<JenisProdukModel>()

    private var tempView: AlertDialogProdukBinding? = null
    private var tempDialog: AlertDialog? = null
    private var fileImage: MultipartBody.Part? = null

    private var idJenisProduk: String? = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
        fetchJenisProduk()
        getJenisProduk()
        fetchDataProduk()
        getDataProduk()
        getAddProduk()
        getUpdateProduk()
        getUpdateProdukNoImage()
        getDeleteProduk()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminProdukActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminProdukActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                showDialogTambahData()
            }
        }
    }

    private fun showDialogTambahData() {
        val view = AlertDialogProdukBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminProdukActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view
        tempDialog = dialogInputan

        setSpinnerJenisProduk(view, "0")

        view.apply {
            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }
            btnSimpan.setOnClickListener {
                var cek = false
                if(etProduk.text.toString().trim().isEmpty()){
                    etProduk.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etStok.text.toString().trim().isEmpty()){
                    etStok.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etHarga.text.toString().trim().isEmpty()){
                    etHarga.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etGambar.text.toString().trim() == resources.getString(R.string.add_image).trim() ){
                    etGambar.error = "Gambar tidak boleh kosong"
                    cek = true
                }

                if(!cek){
                    val produk = etProduk.text.toString().trim()
                    val stok = etStok.text.toString().trim()
                    val harga = etHarga.text.toString().trim()
                    postAddProduk(idJenisProduk!!, produk, fileImage!!, stok, harga)
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempView = null
                tempDialog = null
                fileImage = null
                idJenisProduk = null
            }
        }
    }

    private fun postAddProduk(idJenisProduk: String, produk: String, image: MultipartBody.Part, stok: String, harga: String) {
        viewModel.postTambahProduk(
            convertStringToMultipartBody(""),
            convertStringToMultipartBody(idJenisProduk),
            convertStringToMultipartBody(produk),
            convertStringToMultipartBody(harga),
            convertStringToMultipartBody(stok),
            fileImage!!
        )
    }

    private fun getAddProduk(){
        viewModel.getTambahProduk().observe(this@AdminProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminProdukActivity)
                is UIState.Success-> setSuccessAddProduk(result.data)
                is UIState.Failure-> setFailureAddProduk(result.message)
            }
        }
    }

    private fun setFailureAddProduk(message: String) {
        Toast.makeText(this@AdminProdukActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessAddProduk(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@AdminProdukActivity, "Berhasil", Toast.LENGTH_SHORT).show()
            fetchDataProduk()
            fileImage = null
            tempView = null
            tempDialog = null
            idJenisProduk = null
        } else{
            Toast.makeText(this@AdminProdukActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchJenisProduk() {
        viewModel.fetchJenisProduk()
    }
    private fun getJenisProduk(){
        viewModel.getJenisProduk().observe(this@AdminProdukActivity){result->
            when(result){
                is UIState.Loading->{}
                is UIState.Success-> setSuccessFetchJenisProduk(result.data)
                is UIState.Failure-> setFailureFetchJenisProduk(result.message)
            }
        }
    }

    private fun setFailureFetchJenisProduk(message: String) {
        Toast.makeText(this@AdminProdukActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchJenisProduk(data: ArrayList<JenisProdukModel>) {
        if(data.isNotEmpty()){
            listJenisProduk = data
        } else{
            Toast.makeText(this@AdminProdukActivity, "Tidak ada data Jenis Produk", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDataProduk() {
        viewModel.fetchProduk()
    }
    private fun getDataProduk(){
        viewModel.getProduk().observe(this@AdminProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminProdukActivity)
                is UIState.Failure-> setFailureFetchProduk(result.message)
                is UIState.Success-> setSuccessFetchProduk(result.data)
            }
        }
    }

    private fun setFailureFetchProduk(message: String) {
        Toast.makeText(this@AdminProdukActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
        setStopShimmer()
    }

    private fun setSuccessFetchProduk(data: ArrayList<ProdukModel>) {
        loading.alertDialogCancel()
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        }
    }

    private fun setAdapter(data: ArrayList<ProdukModel>) {
        adapter = AdminProdukAdapter(data, object : OnClickItem.ClickAdminProduk{

            override fun clickItemJenisProduk(keterangan: String, jenisProduk: String, it: View) {
                showKeterangan(keterangan, jenisProduk)
            }

            override fun clickItemProduk(keterangan: String, produk: String, it: View) {
                showKeterangan(keterangan, produk)
            }

            override fun clickItemHargaProduk(keterangan: String, harga: String, it: View) {
//                showKeterangan(keterangan, harga)
            }

            override fun clickItemGambarProduk(keterangan: String, gambar: String, it: View) {
                setShowImage(keterangan, gambar)
            }

            override fun clickItemSetting(produk: ProdukModel, it: View) {
                val popupMenu = PopupMenu(this@AdminProdukActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(produk)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(produk)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

        })

        binding.apply {
            rvProduk.layoutManager = LinearLayoutManager(this@AdminProdukActivity, LinearLayoutManager.VERTICAL, false)
            rvProduk.adapter = adapter
        }
    }

    private fun showKeterangan(keterangan: String, value: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminProdukActivity)
        alertDialog.setView(view.root)
            .setCancelable(true)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = keterangan
            tvBodyKeterangan.text = value

            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowDialogEdit(produk: ProdukModel) {
        val view = AlertDialogProdukBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminProdukActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view
        tempDialog = dialogInputan

        setSpinnerJenisProduk(view, produk.jenis_produk!!.id_jenis_produk!!.toString())

        view.apply {
            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            var idProduk = produk.id_produk!!

            etProduk.setText(produk.produk)
            etStok.setText(produk.stok.toString())
            etHarga.setText(produk.harga)

            btnSimpan.setOnClickListener {
                var cek = false
                if(etProduk.text.toString().trim().isEmpty()){
                    etProduk.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etStok.text.toString().trim().isEmpty()){
                    etStok.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etHarga.text.toString().trim().isEmpty()){
                    etHarga.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if(!cek){
                    var produk = etProduk.text.toString().trim()
                    var harga = etHarga.text.toString().trim()
                    var stok = etStok.text.toString().trim()

                    if(etGambar.text.toString().trim() != resources.getString(R.string.add_image)){
                        postUpdateProduk(idProduk.toString(), idJenisProduk!!, produk, harga, stok, fileImage!!)
                    } else{
                        postUpdateProdukNoImage(idProduk.toString(), idJenisProduk!!, produk, harga, stok)
                    }
                }
            }

            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                fileImage = null
                tempView = null
                tempDialog = null
                idJenisProduk = null
            }
        }
    }

    private fun setSpinnerJenisProduk(view: AlertDialogProdukBinding, cekIdJenisProduk: String) {
        val arrayIdJenisProduk = ArrayList<String>()
        val arrayJenisProduk = ArrayList<String>()

        for(values in listJenisProduk){
            arrayIdJenisProduk.add(values.id_jenis_produk.toString()!!)
            arrayJenisProduk.add(values.jenis_produk!!)
        }

        val arrayAdapter = ArrayAdapter(
            this@AdminProdukActivity,
            android.R.layout.simple_spinner_item,
            arrayJenisProduk
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.apply {
            spJenisProduk.adapter = arrayAdapter

            if(cekIdJenisProduk != "0"){
                for((cekNomor, value) in arrayIdJenisProduk.withIndex()){
                    if(value.trim() == cekIdJenisProduk.trim()){
                        spJenisProduk.setSelection(cekNomor)
                    }
                }
            } else{
                spJenisProduk.setSelection(0)
            }

            spJenisProduk.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val numberPosition = spJenisProduk.selectedItemPosition
                    idJenisProduk = arrayIdJenisProduk[numberPosition]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun postUpdateProduk(idProduk: String, idJenisProduk: String, produk: String, harga: String, stok: String, gambarProduk: MultipartBody.Part) {
        viewModel.postUpdateProduk(
            convertStringToMultipartBody(""),
            convertStringToMultipartBody(idProduk),
            convertStringToMultipartBody(idJenisProduk),
            convertStringToMultipartBody(produk),
            convertStringToMultipartBody(harga),
            convertStringToMultipartBody(stok),
            gambarProduk
        )
    }
    private fun getUpdateProduk(){
        viewModel.getUpdateProduk().observe(this@AdminProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminProdukActivity)
                is UIState.Success-> setSuccessUpdateProduk(result.data)
                is UIState.Failure-> setFailureUpdateProduk(result.message)
            }
        }
    }

    private fun setFailureUpdateProduk(message: String) {
        Toast.makeText(this@AdminProdukActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateProduk(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@AdminProdukActivity, "Berhasil Update Produk", Toast.LENGTH_SHORT).show()
            tempDialog!!.dismiss()
            fileImage = null
            tempView = null
            tempDialog = null
            idJenisProduk = null
            fetchDataProduk()
        } else{
            Toast.makeText(this@AdminProdukActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }


    private fun postUpdateProdukNoImage(idProduk: String, idJenisProduk: String, produk: String, harga: String, stok: String) {
        viewModel.postUpdateProdukNoImage(
            "", idProduk, idJenisProduk, produk, harga, stok
        )
    }

    private fun getUpdateProdukNoImage(){
        viewModel.getUpdateProdukNoImage().observe(this@AdminProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminProdukActivity)
                is UIState.Failure-> setFailureUpdateProduk(result.message)
                is UIState.Success-> setSuccessUpdateProduk(result.data)
            }
        }
    }

    private fun setShowDialogHapus(produk: ProdukModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminProdukActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempDialog = dialogInputan

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus Produk?"
            tvBodyKonfirmasi.text = "Produk yang dihapus tidak dapat dipulihkan"

            btnKonfirmasi.setOnClickListener {
                postDeleteProduk(produk.id_produk!!)
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postDeleteProduk(idProduk: Int) {
        viewModel.postDeleteProduk(idProduk)
    }

    private fun getDeleteProduk(){
        viewModel.getDeleteProduk().observe(this@AdminProdukActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminProdukActivity)
                is UIState.Success-> setSuccessDeleteProduk(result.data)
                is UIState.Failure-> setFailureDeleteProduk(result.message)
            }
        }
    }

    private fun setFailureDeleteProduk(message: String) {
        Toast.makeText(this@AdminProdukActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessDeleteProduk(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@AdminProdukActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
            tempDialog!!.dismiss()
            fetchDataProduk()
        } else{
            Toast.makeText(this@AdminProdukActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setShowImage(jenisProduk: String, gambar: String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminProdukActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitle.text = jenisProduk
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

        Glide.with(this@AdminProdukActivity)
            .load("${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
            .error(R.drawable.gambar_not_have_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun setStopShimmer(){
        binding.apply {
            smProduk.stopShimmer()
            smProduk.visibility = View.GONE

            rvProduk.visibility = View.VISIBLE
        }
    }

    //Permission
    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getNameFile(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor?.moveToFirst()
        val name = cursor?.getString(nameIndex!!)
        cursor?.close()
        return name!!
    }

    @SuppressLint("Recycle")
    private fun uploadImageToStorage(pdfUri: Uri?, pdfFileName: String, nameAPI:String): MultipartBody.Part? {
        var pdfPart : MultipartBody.Part? = null
        pdfUri?.let {
            val file = contentResolver.openInputStream(pdfUri)?.readBytes()

            if (file != null) {
//                // Membuat objek RequestBody dari file PDF
//                val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
//                // Membuat objek MultipartBody.Part untuk file PDF
//                pdfPart = MultipartBody.Part.createFormData("materi_pdf", pdfFileName, requestFile)

                pdfPart = convertFileToMultipartBody(file, pdfFileName, nameAPI)
            }
        }
        return pdfPart
    }

    private fun convertFileToMultipartBody(file: ByteArray, pdfFileName: String, nameAPI:String): MultipartBody.Part?{
        val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(nameAPI, pdfFileName, requestFile)

        return filePart
    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                startActivity(Intent(this, AdminProdukActivity::class.java))
            } else { //request for the permission
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        } else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun pickImageFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }

        startActivityForResult(intent, Constant.STORAGE_PERMISSION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.STORAGE_PERMISSION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI file PDF yang dipilih
            val fileUri = data.data!!

            val nameImage = getNameFile(fileUri)

            tempView?.let {
                it.etGambar.text = nameImage
            }

            // Mengirim file PDF ke website menggunakan Retrofit
            fileImage = uploadImageToStorage(fileUri, nameImage, "gambar")
        }
    }

    private fun convertStringToMultipartBody(data: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminProdukActivity, AdminMainActivity::class.java))
        finish()
    }
}