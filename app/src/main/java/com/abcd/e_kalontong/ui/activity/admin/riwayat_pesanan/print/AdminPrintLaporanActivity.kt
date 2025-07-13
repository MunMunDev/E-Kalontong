package com.abcd.e_kalontong.ui.activity.admin.riwayat_pesanan.print

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abcd.aplikasipenjualanplafon.utils.TanggalDanWaktu
import com.abcd.e_kalontong.R
import com.abcd.e_kalontong.adapter.AdminPrintLaporanAdapter
import com.abcd.e_kalontong.adapter.AdminRiwayatPesananAdapter
import com.abcd.e_kalontong.data.model.RiwayatPesananModel
import com.abcd.e_kalontong.databinding.ActivityAdminPrintLaporanBinding
import com.abcd.e_kalontong.utils.KonversiRupiah
import com.abcd.e_kalontong.utils.LoadingAlertDialog
import com.abcd.e_kalontong.utils.SharedPreferencesLogin
import com.abcd.e_kalontong.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminPrintLaporanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPrintLaporanBinding
    private val viewModel: AdminLaporanViewModel by viewModels()
    private lateinit var sharedPreferences : SharedPreferencesLogin
    @Inject
    lateinit var loading: LoadingAlertDialog

    val tanggalDanWaktu = TanggalDanWaktu()
    private var tempArrayRiwayatPesanan: ArrayList<RiwayatPesananModel> = arrayListOf()
    private var arrayRiwayatPesanan: ArrayList<RiwayatPesananModel> = arrayListOf()
    private var tanggal = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPrintLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferences()
        setDataSebelumnya()
        setButton()
        getData()
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@AdminPrintLaporanActivity)
    }

    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null){
            val mulai = extras.getString("mulai_tanggal")!!
            val sampai = extras.getString("sampai_tanggal")!!

            var arrayMulai = mulai.split(" ")
            var arraySampai = sampai.split(" ")

            val vMulai = tanggalDanWaktu.konversiBulan(arrayMulai[0])
            val vSampai = tanggalDanWaktu.konversiBulan(arraySampai[0])

            tanggal = "$vMulai - $vSampai"

            fetchData(mulai, sampai)
        }
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnPrint.setOnClickListener {
                downloadData()
            }
        }
    }

    private fun fetchData(dari:String, sampai:String){
        viewModel.fetchPesanan(dari, sampai)
    }

    private fun getData(){
        viewModel.getPesanan().observe(this@AdminPrintLaporanActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminPrintLaporanActivity)
                is UIState.Failure-> setFailureFetchData(result.message)
                is UIState.Success-> setSuccessFetchData(result.data)
            }
        }
    }

    private fun setFailureFetchData(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminPrintLaporanActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchData(data: ArrayList<RiwayatPesananModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            tempArrayRiwayatPesanan.addAll(data)
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminPrintLaporanActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<RiwayatPesananModel>) {
        val adapterLaporan = AdminPrintLaporanAdapter(data)

        binding.apply {
            rvDetailPesanan.layoutManager = LinearLayoutManager(
                this@AdminPrintLaporanActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvDetailPesanan.adapter = adapterLaporan
        }
    }


    fun downloadData(){
        // on below line we are checking permission
        if (checkPermissions()) {
            // if permission is granted we are displaying a toast message.
            Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
            generatePDF()
        } else {
            // if the permission is not granted
            // we are calling request permission method.
            requestPermission()
        }
    }


    //kertas F4 landscape
    var pageHeight = 609
    var pageWidth = 935

    //kertas A4 landscape
//    var pageHeight = 595
//    var pageWidth = 842

    private fun generatePDF(){
        if(arrayRiwayatPesanan.isEmpty()){
            arrayRiwayatPesanan.addAll(tempArrayRiwayatPesanan)
        }

        val pdfDocument = PdfDocument()

        var cek = 0;
        var no = 0
        var sizeArray = arrayRiwayatPesanan.size
        while (cek<5){
            no++
            sizeArray-=10

            if(sizeArray<=0){
                cek=10
            }
        }

        //
        for(data in 1..no){
            pdf(pdfDocument, data)
        }

        val tanggal = tanggalDanWaktu.tanggalSekarangZonaMakassar()
        val waktu = tanggalDanWaktu.waktuSekarangZonaMakassar2()
        val vTanggalDanWaktu = "$tanggal-$waktu"

        // Simpan
        val file = File(Environment.getExternalStorageDirectory(), "download/riwayat-pesanan-$vTanggalDanWaktu.pdf")

        try {
            pdfDocument.writeTo(FileOutputStream(file))

            loading.alertDialogCancel()
            Toast.makeText(this@AdminPrintLaporanActivity, "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()

            loading.alertDialogCancel()
            Toast.makeText(this@AdminPrintLaporanActivity, "Fail to generate PDF file..", Toast.LENGTH_SHORT).show()
        }
        pdfDocument.close()
    }


    fun pdf(pdfDocument: PdfDocument, noHalaman:Int){
        val paint = Paint()
        val title = Paint()
        val line = Paint()

        val myPageInfo: PdfDocument.PageInfo? = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        val canvas: Canvas = myPage.canvas

        val bmp: Bitmap
        val scaledbmp: Bitmap

        bmp = BitmapFactory.decodeResource(resources, R.drawable.image_main)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 95, 80, false)
        canvas.drawBitmap(scaledbmp, 50F, 40F, paint)
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        title.textSize = 20F
        title.setColor(ContextCompat.getColor(this, R.color.black))
        canvas.drawText("E-KELONTONG RARA SIDRAP.", 160F, 88F, title)

        //Garis bawah
        line.strokeWidth = 3f
        canvas.drawLine(65f, 140f, 860f, 140f, line)

        // Table
        tableBodyPdf(canvas, paint, title)

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(this, R.color.black))
        title.textSize = 14F
        canvas.drawText("Tabel Riwayat Pesanan - $tanggal", 75f, 175f, title)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        canvas.drawText("Riwayat Pesanan - $tanggal | page-0${noHalaman}", 78f, 575F, title)

        pdfDocument.finishPage(myPage)
    }

    var no = 1
    fun tableBodyPdf(canvas: Canvas, paint: Paint, title: Paint){
        //Header
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        title.setColor(ContextCompat.getColor(this, R.color.black))
        title.textSize = 14F

        //Header
        canvas.drawLine(75f, 185f, 75f, 215f, paint) // paling kiri vertical
        canvas.drawLine(75f, 185f, 850f, 185f, paint) // paling atas horizontal
        canvas.drawLine(850f, 185f, 850f, 215f, paint) // paling kanan vertical
        canvas.drawLine(75f, 215f, 850f, 215f, paint) // paling bawah horizontal

        // Data tetap (kolom (baris vertikal) )
        canvas.drawLine(125f, 185f, 125f, 215f, paint) // no
        canvas.drawLine(180f, 185f, 180f, 215f, paint) // ID
        canvas.drawLine(300f, 185f, 300f, 215f, paint) // Nama User
        canvas.drawLine(590f, 185f, 590f, 215f, paint) // Jenis
        canvas.drawLine(640f, 185f, 640f, 215f, paint) // Jumlah
        canvas.drawLine(740f, 185f, 740f, 215f, paint) // Harga
        canvas.drawLine(850f, 185f, 850f, 215f, paint) // Tanggal

        // Tulisan
        canvas.drawText("NO", 90F, 205F, title)
        canvas.drawText("ID", 140F, 205F, title)
        canvas.drawText("Nama", 190F, 205F, title)
        canvas.drawText("Produk", 310F, 205F, title) // x + 10 dari nama
        canvas.drawText("Jum", 600F, 205F, title)
        canvas.drawText("Harga", 655F, 205F, title)
        canvas.drawText("Tanggal", 755F, 205F, title)

        // Body
        var nilaiBodyTableStartX = 75f
        var nilaiBodyTableStartY = 215f
        var nilaiBodyTableStopX = 850f
        var nilaiBodyTableStopY = 245f

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        val rupiah = KonversiRupiah()

        var valueNomor = 0;
        var cekNomor = 1;

        var s = arrayRiwayatPesanan.size-1

        var totalHarga = 0
        while (valueNomor<1){
            val value = arrayRiwayatPesanan[0]
            canvas.drawLine(nilaiBodyTableStartX, nilaiBodyTableStartY, nilaiBodyTableStartX, nilaiBodyTableStopY, paint) // paling kiri vertical
            canvas.drawLine(nilaiBodyTableStartX, nilaiBodyTableStartY, nilaiBodyTableStopX, nilaiBodyTableStartY, paint) // paling atas horizontal
            canvas.drawLine(nilaiBodyTableStopX, nilaiBodyTableStartY, nilaiBodyTableStopX, nilaiBodyTableStopY, paint) // paling kanan vertical
            canvas.drawLine(nilaiBodyTableStartX, nilaiBodyTableStopY, nilaiBodyTableStopX, nilaiBodyTableStopY, paint) // paling bawah horizontal

            // Data tetap (kolom (baris vertikal) )
            canvas.drawLine(125f, nilaiBodyTableStartY, 125f, nilaiBodyTableStopY, paint) // no
            canvas.drawLine(180f, nilaiBodyTableStartY, 180f, nilaiBodyTableStopY, paint) // ID
            canvas.drawLine(300f, nilaiBodyTableStartY, 300f, nilaiBodyTableStopY, paint) // Nama Barang
            canvas.drawLine(590f, nilaiBodyTableStartY, 590f, nilaiBodyTableStopY, paint) // Jenis
            canvas.drawLine(640f, nilaiBodyTableStartY, 640f, nilaiBodyTableStopY, paint) // Jumlah
            canvas.drawLine(740f, nilaiBodyTableStartY, 740f, nilaiBodyTableStopY, paint) // Harga
            canvas.drawLine(850f, nilaiBodyTableStartY, 850f, nilaiBodyTableStopY, paint) // Tanggal

            // Tulisan
            val list = arrayRiwayatPesanan[0]
            val waktu = (list.waktu)!!.split(" ")
            val tanggal = (waktu[0]).split("-")
            val valueTanggal = "${tanggal[2]}-${tanggal[1]}-${tanggal[0]}"

            canvas.drawText("${no}", 90F, (nilaiBodyTableStartY+20f), title)
            canvas.drawText("${value.id_pemesanan}", 140F, (nilaiBodyTableStartY+20f), title)
            canvas.drawText("${value.nama_lengkap}", 190F, (nilaiBodyTableStartY+20f), title)
            canvas.drawText("${value.produk}", 310F, (nilaiBodyTableStartY+20f), title)
            canvas.drawText("${value.jumlah}", 600F, (nilaiBodyTableStartY+20f), title)
            canvas.drawText("${rupiah.rupiah(value.harga!!.toLong())}", 655F, (nilaiBodyTableStartY+20f), title)
            canvas.drawText("${valueTanggal}", 755F, (nilaiBodyTableStartY+20f), title)

            nilaiBodyTableStartY+=30f
            nilaiBodyTableStopY+=30f
            no++

            arrayRiwayatPesanan.removeAt(0)

//            Log.d(TAG, "tableBodyPdf: ${arrayRiwayatPesanan.size}")

            totalHarga += (value.harga!!.trim().toInt() * value.jumlah!!.trim().toInt())
            if(arrayRiwayatPesanan.size<=0){
                valueNomor = 1

                canvas.drawLine(nilaiBodyTableStartX, nilaiBodyTableStartY, nilaiBodyTableStartX, nilaiBodyTableStopY, paint) // paling kiri vertical
                canvas.drawLine(nilaiBodyTableStartX, nilaiBodyTableStartY, nilaiBodyTableStopX, nilaiBodyTableStartY, paint) // paling atas horizontal
                canvas.drawLine(nilaiBodyTableStopX, nilaiBodyTableStartY, nilaiBodyTableStopX, nilaiBodyTableStopY, paint) // paling kanan vertical
                canvas.drawLine(nilaiBodyTableStartX, nilaiBodyTableStopY, nilaiBodyTableStopX, nilaiBodyTableStopY, paint) // paling bawah horizontal

                canvas.drawLine(590f, nilaiBodyTableStartY, 590f, nilaiBodyTableStopY, paint) // Jenis
                canvas.drawText("Total Harga Per Table", 400F, (nilaiBodyTableStartY+20f), title)
                canvas.drawText("${rupiah.rupiah(totalHarga.toLong())}", 600F, (nilaiBodyTableStartY+20f), title)
            }
            if(cekNomor>=10){
                valueNomor = 1

                canvas.drawLine(nilaiBodyTableStartX, nilaiBodyTableStartY, nilaiBodyTableStartX, nilaiBodyTableStopY, paint) // paling kiri vertical
                canvas.drawLine(nilaiBodyTableStartX, nilaiBodyTableStartY, nilaiBodyTableStopX, nilaiBodyTableStartY, paint) // paling atas horizontal
                canvas.drawLine(nilaiBodyTableStopX, nilaiBodyTableStartY, nilaiBodyTableStopX, nilaiBodyTableStopY, paint) // paling kanan vertical
                canvas.drawLine(nilaiBodyTableStartX, nilaiBodyTableStopY, nilaiBodyTableStopX, nilaiBodyTableStopY, paint) // paling bawah horizontal

                canvas.drawLine(590f, nilaiBodyTableStartY, 590f, nilaiBodyTableStopY, paint) // Jenis
                canvas.drawText("Total Harga Per Table", 400F, (nilaiBodyTableStartY+20f), title)
                canvas.drawText("${rupiah.rupiah(totalHarga.toLong())}", 600F, (nilaiBodyTableStartY+20f), title)
            }

            cekNomor++
        }
    }

    fun checkPermissions(): Boolean {
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            this@AdminPrintLaporanActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        // on below line we are creating a variable
        // for reading external storage permission
        var readStoragePermission = ContextCompat.checkSelfPermission(
            this@AdminPrintLaporanActivity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    var PERMISSION_CODE = 101
    fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE) {

            // on below line we are checking if result size is > 0
            if (grantResults.size > 0) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {

                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}