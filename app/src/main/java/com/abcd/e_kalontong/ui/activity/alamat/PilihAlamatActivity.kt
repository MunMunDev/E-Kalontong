package com.abcd.e_kalontong.ui.activity.alamat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.PilihAlamatAdapter
import com.abcd.e_kalontong.data.model.AlamatModel
import com.abcd.e_kalontong.data.model.KecamatanModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.databinding.ActivityPilihAlamatBinding
import com.abcd.e_kalontong.databinding.AlertDialogPilihAlamatBinding
import com.abcd.e_kalontong.ui.activity.user.payment.PaymentActivity
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.OnClickItem
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PilihAlamatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPilihAlamatBinding
    private val viewModel: PilihAlamatViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferencesLogin
    @Inject
    lateinit var loading: LoadingAlertDialog

    private lateinit var pesanan: ArrayList<PesananModel>
    private lateinit var listKecamatan: ArrayList<KecamatanModel>
    private var listNamaKecamatan: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihAlamatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferences()
        setButton()
        fetchDataSebelumnnya()
        setAppNavBar()
        fetchKecamatan()
        getKecamatan()
        fetchAlamat(sharedPreferences.getIdUser())
        getAlamat()
        getUpdateMainAlamat()
        getTambahAlamat()
        getUpdateAlamat()
    }

    private fun setButton() {
        binding.apply {
            appNavbarDrawer.ivBack.setOnClickListener {
                val i = Intent(this@PilihAlamatActivity, PaymentActivity::class.java)
                i.putParcelableArrayListExtra("pesanan", pesanan)
                startActivity(i)
                finish()
            }
            btnTambahAlamat.setOnClickListener {
                setShowDialogTambahData()
            }
        }
    }

    private fun fetchDataSebelumnnya() {
        val extras = intent.extras
        if(extras != null) {
            pesanan = arrayListOf()
            pesanan = intent.getParcelableArrayListExtra("pesanan")!!
        }
    }


    private fun setAppNavBar() {
        binding.appNavbarDrawer.apply {
            tvTitle.text = "Pilih Alamat"
            ivNavDrawer.visibility = View.GONE
            ivBack.visibility = View.VISIBLE
        }
    }

    private fun setShowDialogTambahData() {
        val view = AlertDialogPilihAlamatBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@PilihAlamatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            var selectedKecamatan = ""
            val arrayAdapterKecamatan = ArrayAdapter(
                this@PilihAlamatActivity,
                android.R.layout.simple_spinner_item,
                listKecamatan
            )

            arrayAdapterKecamatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spKecamatan.adapter = arrayAdapterKecamatan

            spKecamatan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedKecamatan = listKecamatan[position].id_kecamatan!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            btnSimpan.setOnClickListener {
                var cek = true
                if(etNamaLengkap.text.toString().trim().isEmpty()){
                    etNamaLengkap.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etNomorHp.text.toString().trim().isEmpty()){
                    etNomorHp.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etDetailAlamat.text.toString().trim().isEmpty()){
                    etDetailAlamat.error = "Tidak Boleh Kosong"
                    cek = false
                }

                if(cek){
                    val namaLengkap = etNamaLengkap.text.toString()
                    val nomorHp = etNomorHp.text.toString()
                    val detailAlamat = etDetailAlamat.text.toString()

                    postTambahAlamat(
                        sharedPreferences.getIdUser().toString(),
                        namaLengkap, nomorHp, selectedKecamatan, detailAlamat
                    )
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahAlamat(
        idUser: String, namaLengkap: String, nomorHp: String,
        idKecamatan: String, detailAlamat: String
    ) {
        viewModel.postTambahAlamat(idUser, namaLengkap, nomorHp, idKecamatan, detailAlamat)
    }

    private fun getTambahAlamat(){
        viewModel.getTambahAlamat().observe(this@PilihAlamatActivity){ result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@PilihAlamatActivity)
                is UIState.Failure -> setFailureTambahAlamat(result.message)
                is UIState.Success -> setSuccessTambahAlamat(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureTambahAlamat(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTambahAlamat(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@PilihAlamatActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                fetchAlamat(sharedPreferences.getIdUser())
            } else{
                Toast.makeText(this@PilihAlamatActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@PilihAlamatActivity)
    }

    private fun fetchKecamatan(){
        viewModel.fetchKecamatan()
    }

    private fun getKecamatan(){
        viewModel.getKecamatan().observe(this@PilihAlamatActivity){ result->
            when(result){
                is UIState.Loading->{}
                is UIState.Failure-> setFailureFetchKecamatan(result.message)
                is UIState.Success-> setSuccessFetchKecamatan(result.data)
            }
        }
    }

    private fun setFailureFetchKecamatan(message: String) {

    }

    private fun setSuccessFetchKecamatan(data: ArrayList<KecamatanModel>) {
        if(data.isNotEmpty()){
            listKecamatan = data
            for(value in data){
                listNamaKecamatan.add(value.kecamatan!!)
            }
        }
    }

    private fun fetchAlamat(idUser: Int){
        viewModel.fetchDataAlamat(idUser)
    }
    private fun getAlamat(){
        viewModel.getDataAlamat().observe(this@PilihAlamatActivity){ result->
            when(result){
                is UIState.Loading -> setStarShimmer()
                is UIState.Success -> setSuccessFetchAlamat(result.data)
                is UIState.Failure -> setFailureFetchAlamat(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureFetchAlamat(message: String) {
        setStopShimmer()
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchAlamat(data: ArrayList<AlamatModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@PilihAlamatActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<AlamatModel>) {
        binding.apply {
            val adapter = PilihAlamatAdapter(data, object: OnClickItem.ClickPilihAlamat{
                override fun clickItemPilih(alamat: AlamatModel, it: View) {
                    postUpdateMainAlamat(alamat.id_alamat!!)
                }

                override fun clickItemEdit(alamat: AlamatModel, it: View) {
                    setShowDialogUpdateData(alamat.id_alamat!!, alamat.nama_lengkap!!, alamat.nomor_hp!!, alamat.detail_alamat!!)
                }

            })
            rvAlamat.layoutManager = LinearLayoutManager(this@PilihAlamatActivity, LinearLayoutManager.VERTICAL, false)
            rvAlamat.adapter = adapter
        }
    }

    private fun postUpdateMainAlamat(idAlamat: String) {
        viewModel.postUpdateMainAlamat(idAlamat, sharedPreferences.getIdUser().toString())
    }

    private fun getUpdateMainAlamat(){
        viewModel.getUpdateMainAlamat().observe(this@PilihAlamatActivity){ result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@PilihAlamatActivity)
                is UIState.Failure -> setFailureUpdateMainAlamat(result.message)
                is UIState.Success -> setSuccessUpdateMainAlamat(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureUpdateMainAlamat(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateMainAlamat(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                val i = Intent(this@PilihAlamatActivity, PaymentActivity::class.java)
                i.putExtra("pesanan", pesanan)
                startActivity(i)
                finish()
            } else{
                Toast.makeText(this@PilihAlamatActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setShowDialogUpdateData(
        idAlamat: String, namaLengkap: String,
        nomorHp: String, detailAlamat: String
    ) {
        val view = AlertDialogPilihAlamatBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@PilihAlamatActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etNamaLengkap.setText(namaLengkap)
            etNomorHp.setText(nomorHp)
            etDetailAlamat.setText(detailAlamat)

            var selectedKecamatan = ""

            val arrayAdapterKecamatan = ArrayAdapter(
                this@PilihAlamatActivity,
                android.R.layout.simple_spinner_item,
                listNamaKecamatan
            )

            arrayAdapterKecamatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spKecamatan.adapter = arrayAdapterKecamatan

            spKecamatan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedKecamatan = listKecamatan[position].id_kecamatan!!
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            btnSimpan.setOnClickListener {
                var cek = true
                if(etNamaLengkap.text.toString().trim().isEmpty()){
                    etNamaLengkap.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etNomorHp.text.toString().trim().isEmpty()){
                    etNomorHp.error = "Tidak Boleh Kosong"
                    cek = false
                }
                if(etDetailAlamat.text.toString().trim().isEmpty()){
                    etDetailAlamat.error = "Tidak Boleh Kosong"
                    cek = false
                }

                if(cek){
                    val valueNamaLengkap = etNamaLengkap.text.toString()
                    val valueNomorHp = etNomorHp.text.toString()
                    val valueDetailAlamat = etDetailAlamat.text.toString()

                    postUpdateAlamat(
                        idAlamat, sharedPreferences.getIdUser().toString(),
                        valueNamaLengkap, valueNomorHp, selectedKecamatan, valueDetailAlamat
                    )

                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateAlamat(
        idAlamat: String, idUser: String, namaLengkap: String,
        nomorHp: String, idKecamatan:String, detailAlamat: String
    ) {
        viewModel.postUpdateAlamat(idAlamat, idUser, namaLengkap, nomorHp, idKecamatan, detailAlamat)
    }

    private fun getUpdateAlamat(){
        viewModel.getUpdateAlamat().observe(this@PilihAlamatActivity){ result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@PilihAlamatActivity)
                is UIState.Failure -> setFailureUpdateAlamat(result.message)
                is UIState.Success -> setSuccessUpdateAlamat(result.data)
                else -> {}
            }
        }
    }

    private fun setFailureUpdateAlamat(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PilihAlamatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateAlamat(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@PilihAlamatActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                fetchAlamat(sharedPreferences.getIdUser())
            } else{
                Toast.makeText(this@PilihAlamatActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setStarShimmer(){
        binding.apply {
            binding.apply {
                rvAlamat.visibility = View.GONE

                smAlamat.visibility = View.VISIBLE
                smAlamat.startShimmer()
            }
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            binding.apply {
                rvAlamat.visibility = View.VISIBLE

                smAlamat.visibility = View.GONE
                smAlamat.stopShimmer()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val i = Intent(this@PilihAlamatActivity, PaymentActivity::class.java)
        i.putParcelableArrayListExtra("pesanan", pesanan)
        startActivity(i)
        finish()
    }
}