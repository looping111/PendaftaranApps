package com.example.pendaftaranapps.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pendaftaranapps.R
import com.example.pendaftaranapps.data.response.AddUpdateResponse
import com.example.pendaftaranapps.data.response.DataItem
import com.example.pendaftaranapps.data.retrofit.ApiConfig
import com.example.pendaftaranapps.databinding.ActivityAddUpdateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddUpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUpdateBinding
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val siswa = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DATA, DataItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<DataItem>(EXTRA_DATA)
        }

        if (siswa != null) {
            id = siswa.id!!.toInt()

            binding.btnSaveUpdate.text = getString(R.string.update)
            binding.btnDelete.visibility = View.VISIBLE

            binding.etNama.setText("${siswa.nama}")
            binding.etAlamat.setText("${siswa.alamat}")
            binding.etJK.setText("${siswa.jenisKelamin}")
            binding.etAgama.setText("${siswa.agama}")
            binding.etSekolahAsal.setText("${siswa.sekolahAsal}")
        } else {
            binding.btnSaveUpdate.text = getString(R.string.save)
            binding.btnDelete.visibility = View.GONE
        }

        binding.btnDelete.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val alamat = binding.etAlamat.text.toString()
            val agama = binding.etAgama.text.toString()
            val jenisKelamin = binding.etJK.text.toString()
            val sekolahAsal = binding.etSekolahAsal.text.toString()

            if (nama.isEmpty() || alamat.isEmpty() || agama.isEmpty() || jenisKelamin.isEmpty() || sekolahAsal.isEmpty()) {
                Toast.makeText(this, "Tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            } else {
                deleteSiswa(nama, alamat, agama, jenisKelamin, sekolahAsal)
            }
        }

        binding.btnSaveUpdate.setOnClickListener {
            val nama = binding.etNama.text.toString()
            val alamat = binding.etAlamat.text.toString()
            val jenisKelamin = binding.etJK.text.toString()
            val agama = binding.etAgama.text.toString()
            val sekolahAsal = binding.etSekolahAsal.text.toString()

            if (nama.isEmpty()) {
                binding.etNama.error = getString(R.string.error)
            } else if (alamat.isEmpty()) {
                binding.etAlamat.error = getString(R.string.error)
            } else if (jenisKelamin.isEmpty()) {
                binding.etJK.error = getString(R.string.error)
            } else if (agama.isEmpty()) {
                binding.etAgama.error = getString(R.string.error)
            } else if (sekolahAsal.isEmpty()) {
                binding.etSekolahAsal.error = getString(R.string.error)
            } else {
                if (binding.btnSaveUpdate.text == getString(R.string.save)) {
                    insertSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
                } else if (binding.btnSaveUpdate.text == getString(R.string.update)) {
                    updateSiswa(id, nama, alamat, jenisKelamin, agama, sekolahAsal)
                }
            }
        }
    }

    private fun deleteSiswa(
        nama: String,
        alamat: String,
        agama: String,
        jenisKelamin: String,
        sekolahAsal: String
    ) {
        showLoading(true)
        val client = ApiConfig.getApiService()
            .deleteSiswa(id, nama, alamat, agama, jenisKelamin, sekolahAsal)
        client.enqueue(object : Callback<AddUpdateResponse> {

            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@AddUpdateActivity, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<AddUpdateResponse>,
                response: Response<AddUpdateResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null)
                        Toast.makeText(this@AddUpdateActivity, "Succes", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@AddUpdateActivity, "onFailure", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun updateSiswa(
        id: Int,
        nama: String,
        alamat: String,
        jenisKelamin: String,
        agama: String,
        sekolahAsal: String
    ) {
        showLoading(true)

        val client = ApiConfig.getApiService().updateSiswa(id, nama, alamat, jenisKelamin, agama, sekolahAsal)

        client.enqueue(object : Callback<AddUpdateResponse>{
            override fun onResponse(
                call: Call<AddUpdateResponse>,
                response: Response<AddUpdateResponse>
            ) {
                showLoading(false)

                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        Toast.makeText(this@AddUpdateActivity, "Berhasil", Toast.LENGTH_SHORT ).show()
                        startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@AddUpdateActivity, "null", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this@AddUpdateActivity, "Gagal", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@AddUpdateActivity, "failed", Toast.LENGTH_SHORT)
            }
        })
    }
    private fun insertSiswa(
        nama: String,
        alamat: String,
        jenisKelamin: String,
        agama: String,
        sekolahAsal: String
    ) {
        showLoading(true)

        val client = ApiConfig.getApiService().addSiswa(nama, alamat, jenisKelamin, agama, sekolahAsal)
        client.enqueue(object : Callback<AddUpdateResponse> {
            override fun onResponse(
                call: Call<AddUpdateResponse>,
                response: Response<AddUpdateResponse>
            ) {
                showLoading(false)

                if (response.isSuccessful) {
                 val responseBody = response.body()

                 if (responseBody != null) {
                     Toast.makeText(this@AddUpdateActivity, "Success", Toast.LENGTH_SHORT).show()
                     startActivity(Intent(this@AddUpdateActivity, MainActivity::class.java))
                     finish()
                 }else{
                     Toast.makeText(this@AddUpdateActivity, "null", Toast.LENGTH_SHORT).show()
                 }
                } else {
                    Toast.makeText(this@AddUpdateActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AddUpdateResponse>, t: Throwable) {

            }
        })
    }
    private fun showLoading(isLoading: Boolean){
        if (isLoading) {
            binding.progresBar.visibility = View.VISIBLE
        } else {
            binding.progresBar.visibility = View.GONE
        }
    }

    companion object{
        const val EXTRA_DATA = "extra_data"
    }
}