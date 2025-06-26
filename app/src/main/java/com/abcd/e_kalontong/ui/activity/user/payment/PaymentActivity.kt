package com.abcd.e_kalontong.ui.activity.user.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.aplikasipenjualanplafon.utils.TanggalDanWaktu
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.ListPaymentAdapter
import com.abcd.e_kalontong.data.model.AlamatModel
import com.abcd.e_kalontong.data.model.PesananModel
import com.abcd.e_kalontong.data.model.ProdukModel
import com.abcd.e_kalontong.data.model.ResponseModel
import com.abcd.e_kalontong.databinding.ActivityPaymentBinding
import com.abcd.e_kalontong.ui.activity.alamat.PilihAlamatActivity
import com.abcd.e_kalontong.ui.activity.user.main.MainActivity
import com.abcd.e_kalontong.utils.Constant
import com.abcd.e_kalontong.utils.KataAcak
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import com.abcd.e_kalontong.utils.network.UIState
import com.midtrans.sdk.uikit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: PaymentViewModel by viewModels()
    @Inject
    lateinit var tanggalDanWaktu: TanggalDanWaktu
    @Inject
    lateinit var hurufAcak: KataAcak
    @Inject
    lateinit var rupiah: KonversiRupiah
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var totalBiaya = 0.0
    private var uuid = UUID.randomUUID().toString()
    @Inject
    lateinit var kataAcak: KataAcak

    private var acak = ""

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var customerDetails: CustomerDetails
    private var itemDetails: ArrayList<ItemDetails> = arrayListOf()
    private lateinit var initTransactionDetails: SnapTransactionDetail

    private lateinit var pesanan: ArrayList<PesananModel>
    private var idUser = 0
    private var namaLengkap = ""
    private var nomorHp = ""
    private var kecamatanKabKota = ""
    private var alamat = ""
    private var detailAlamat = ""
    private var jenisPembayaran = ""

    private var idProduk = 0

    private var selectedValue = ""
    private var numberPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        acak = kataAcak.getHurufDanAngka()
        setSharedPreferencesLogin()
        fetchDataSebelumnya()
        setAppNavbarDrawer()
        setButton()
        setSpinnerMetodePembayaran()
        fetchAlamat(sharedPreferencesLogin.getIdUser())
        getAlamat()
        konfigurationMidtrans()
        getDataRegistrasiPembayaran()
        getTambahPesananDitempat()
        getPesanan()
    }

    private fun fetchDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            pesanan = arrayListOf()
            pesanan = intent.getParcelableArrayListExtra("pesanan")!!
            idProduk = intent.getIntExtra("idProduk", 0)

            if(pesanan.isNotEmpty()){
                setProduk(pesanan)
            } else{
                Toast.makeText(this@PaymentActivity, "Produk kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAppNavbarDrawer() {
        binding.appNavbarDrawer.apply {
            tvTitle.text = "Pesanan Anda"
            ivNavDrawer.visibility = View.GONE
            ivBack.visibility = View.VISIBLE
        }
    }
    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@PaymentActivity)
        idUser = sharedPreferencesLogin.getIdUser()
    }

    private fun setButton() {
        binding.apply {
            binding.appNavbarDrawer.ivBack.setOnClickListener {
//                startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
                finish()
            }
            btnBayar.setOnClickListener {
                if(tvKecamatan.text.toString().trim() == resources.getString(R.string.alamat).trim()){
                    Toast.makeText(this@PaymentActivity, "Tolong masukkan alamat anda", Toast.LENGTH_SHORT).show()
                } else{
                    if(numberPosition == 0){
//                        fetchDataRegistrasiPembayaran(uuid, idUser)
                        postDataRegistrasiPembayaran(acak, idUser)
                    } else{
                        postTambahPesananDitempat(idUser)
                    }
                }
            }

            clAlamat.setOnClickListener {
                val i = Intent(this@PaymentActivity, PilihAlamatActivity::class.java)
                i.putParcelableArrayListExtra("pesanan", pesanan)
                startActivity(i)
                finish()
            }
        }
    }

    private fun setSpinnerMetodePembayaran(){
        binding.apply {
            // Spinner Metode Pembayaran
            val arrayAdapter = ArrayAdapter.createFromResource(
                this@PaymentActivity,
                R.array.metode_pembayaran,
                android.R.layout.simple_spinner_item
            )
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spMetodePembayaran.adapter = arrayAdapter

            spMetodePembayaran.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPosition = spMetodePembayaran.selectedItemPosition
                    selectedValue = spMetodePembayaran.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spMetodePembayaran.adapter = arrayAdapter
        }
    }

    private fun fetchAlamat(idUser: Int){
        viewModel.fetchAlamat(idUser)
    }

    private fun getAlamat(){
        viewModel.getAlamat().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Loading->setStartShimmer()
                is UIState.Success->setSuccessFetchAlamat(result.data)
                is UIState.Failure->setFailureFetchAlamat(result.message)
                else -> {}
            }
        }
    }

    private fun setFailureFetchAlamat(message: String) {
        Toast.makeText(this@PaymentActivity, "alamat: $message", Toast.LENGTH_SHORT).show()
        setStopShimmer()
    }

    private fun setSuccessFetchAlamat(data: ArrayList<AlamatModel>) {
        setAlamat(data)
        setStopShimmer()
    }

    private fun setProduk(data: ArrayList<PesananModel>) {
        Log.d("PaymentActivityTAG", "setDataSuccessPembayaran: ${data.size}")
        if(data.size>0){
            var totalHarga: Double = 0.0

            Log.d("MainActivityTag", "setData: $data")

            var no = 1

            for (value in data){
                val harga = value.produk!!.harga!!.toInt()
                val namaProduk = value.produk.produk
                val jumlah = value.jumlah!!.trim().toInt()

                val vHarga = harga*jumlah

                totalHarga += vHarga
                Log.d("PaymentActivityTAG", "set: no: $no, " +
                        "harga: $totalHarga, namaProduk: $namaProduk ")

                itemDetails.add(
                    ItemDetails(
                        "$no", harga.toDouble(), jumlah, "$namaProduk"
                    )
                )
                no++
            }

            totalBiaya = totalHarga.toDouble()
            Log.d("PaymentActivityTAG", "setDataSuccessPembayaran: $totalBiaya")

            initTransactionDetails = SnapTransactionDetail(
                acak,
                totalBiaya
            )

            binding.apply {
                setAdapter(data)
                tvTotalTagihan.text = rupiah.rupiah(totalHarga.toLong())
            }
        } else{
            Toast.makeText(this@PaymentActivity, "Terima Kasih Telah Memesan", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
            finish()
        }
    }


    private fun setAlamat(data: ArrayList<AlamatModel>) {
        binding.apply {
            // Alamat
            if(data.isNotEmpty()){
                data[0].apply {
                    val kecamatan = "${kecamatan!!.kecamatan}, Kabupaten Sidenreng Rappang"
                    kecamatanKabKota = kecamatan
                    tvNama.text = nama_lengkap
                    tvNomorHp.text = nomor_hp
                    tvKecamatan.text = kecamatan
                    tvAlamatDetail.text = detail_alamat

                    namaLengkap = nama_lengkap!!
                    nomorHp = nomor_hp!!
                    detailAlamat = detail_alamat!!
                }
            } else{
                // Tidak ada data
                tvNama.text = sharedPreferencesLogin.getNama()
                tvNomorHp.text = sharedPreferencesLogin.getNomorHp()
                tvKecamatan.text = resources.getString(R.string.alamat)
            }

            setCustomerDetails()
        }
    }

    private fun setAdapter(data: ArrayList<PesananModel>) {
        binding.apply {
            val adapter = ListPaymentAdapter(data)
            rvPesanan.layoutManager = LinearLayoutManager(this@PaymentActivity, LinearLayoutManager.VERTICAL, false)
            rvPesanan.adapter = adapter
        }
    }

    private fun postDataRegistrasiPembayaran(
        kodeUnik: String,
        idUser: Int
    ) {
        viewModel.postRegistrasiPembayaran(kodeUnik, idUser)
    }

    private fun getDataRegistrasiPembayaran() {
        viewModel.getRegistrasiPembayaran().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@PaymentActivity)
                is UIState.Success-> setDataSuccessRegistrasiPembayaran(result.data)
                is UIState.Failure-> setDataFailureRegistrasiPembayaran(result.message)
                else->{}
            }
        }
    }

    private fun setDataFailureRegistrasiPembayaran(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PaymentActivity, "Ada masalah : $message", Toast.LENGTH_SHORT).show()
    }

    private fun setDataSuccessRegistrasiPembayaran(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status == "0"){
            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                activity = this@PaymentActivity,
                launcher = launcher,
                transactionDetails = initTransactionDetails,
                customerDetails = customerDetails,
                itemDetails = itemDetails
            )
        }else{
            Toast.makeText(this@PaymentActivity, "Gagal Registrasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postTambahPesananDitempat(
        idUser: Int,
    ) {
        viewModel.postPesanCod(idUser)
    }
    private fun getTambahPesananDitempat(){
        viewModel.getPostPesan().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@PaymentActivity)
                is UIState.Failure -> setFailurePostPesan(result.message)
                is UIState.Success -> setSuccessPostPesan(result.data)
                else->{}
            }
        }
    }

    private fun setFailurePostPesan(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessPostPesan(data: ResponseModel) {
        loading.alertDialogCancel()
        if(data.status=="0"){
            Toast.makeText(this@PaymentActivity, "Berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
            finish()
        } else{
            Toast.makeText(this@PaymentActivity, data.message_response, Toast.LENGTH_SHORT).show()
        }
    }

    private fun konfigurationMidtrans() {
        setLauncher()
        setCustomerDetails()
        setInitTransactionDetails()
        buildUiKit()
    }

    private fun buildUiKit() {
        setInitTransactionDetails()
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl(Constant.MIDTRANS_BASE_URL)
            .withMerchantClientKey(Constant.MIDTRANS_CLIENT_KEY)
            .enableLog(true)
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UiKitApi.getDefaultInstance().uiKitSetting
        uIKitCustomSetting.saveCardChecked = true
    }

    private fun setInitTransactionDetails() {
//        initTransactionDetails = SnapTransactionDetail(
//            uuid,
//            totalBiaya
//        )
        initTransactionDetails = SnapTransactionDetail(
            acak,
            totalBiaya
        )
    }

    private fun setCustomerDetails() {
//        var nomorHp = sharedPreferencesLogin.getNomorHp()
//        if(nomorHp == ""){
//            nomorHp = "0"
//        }
//        customerDetails = CustomerDetails(
//            firstName = sharedPreferencesLogin.getNama(),
//            customerIdentifier = "${sharedPreferencesLogin.getIdUser()}",
//            email = "mail@mail.com",
//            phone = nomorHp
//        )
        if(nomorHp == ""){
            nomorHp = "0"
        }

        customerDetails = CustomerDetails(
            firstName = namaLengkap,
            customerIdentifier = "${sharedPreferencesLogin.getIdUser()}",
            email = "mail@mail.com",
            phone = nomorHp
        )
    }

    private fun setLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(
                        UiKitConstants.KEY_TRANSACTION_RESULT)

                    Toast.makeText(this, "${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val transactionResult = data?.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(
                UiKitConstants.KEY_TRANSACTION_RESULT)
            if (transactionResult != null) {
                loading.alertDialogLoading(this@PaymentActivity)
                when (transactionResult.status) {
                    UiKitConstants.STATUS_SUCCESS -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Finished. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
//                        fetchDataPembayaran(idUser)
                        fetchPesanan()
                    }
                    UiKitConstants.STATUS_PENDING -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Pending. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        acak = kataAcak.getHurufDanAngka()
//                        fetchDataPembayaran(idUser)
                        fetchPesanan()
                    }
                    UiKitConstants.STATUS_FAILED -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Failed. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        acak = kataAcak.getHurufDanAngka()
//                        fetchDataPembayaran(idUser)
                        fetchPesanan()
                    }
                    UiKitConstants.STATUS_CANCELED -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show()
                        acak = kataAcak.getHurufDanAngka()
//                        fetchDataPembayaran(idUser)
                        fetchPesanan()
                    }
                    UiKitConstants.STATUS_INVALID -> {
                        loading.alertDialogCancel()
                        Toast.makeText(this, "Transaction Invalid. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        acak = kataAcak.getHurufDanAngka()
//                        fetchDataPembayaran(idUser)
                        fetchPesanan()
                    }
                    else -> {
                        Toast.makeText(this, "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status, Toast.LENGTH_LONG).show()
                        loading.alertDialogCancel()
                        fetchPesanan()
//                        fetchDataPembayaran(idUser)
                    }
                }
            } else {
                Toast.makeText(this@PaymentActivity, "Gagal Tranksaksi", Toast.LENGTH_LONG).show()
                fetchPesanan()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun fetchPesanan() {
        viewModel.fetchKeranjangBelanja(sharedPreferencesLogin.getIdUser())
    }

    private fun getPesanan() {
        viewModel.getKeranjangBelanja().observe(this@PaymentActivity){ result->
            when(result){
                is UIState.Loading-> {}
                is UIState.Failure-> setFailureFetchPesanan(result.message)
                is UIState.Success-> setSuccessFetchPesanan(result.data)
            }
        }
    }

    private fun setFailureFetchPesanan(message: String) {
        startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
        finish()
    }

    private fun setSuccessFetchPesanan(data: ArrayList<PesananModel>) {
        if(data.isEmpty()){
            startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun setStartShimmer(){
        binding.apply {
            smAlamat.startShimmer()
            smAlamat.visibility = View.VISIBLE

            clAlamatDetail.visibility = View.GONE
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            smAlamat.stopShimmer()
            clAlamatDetail.visibility = View.VISIBLE

            smAlamat.visibility = View.GONE

        }
    }
}