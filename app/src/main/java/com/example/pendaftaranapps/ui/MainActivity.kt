package com.example.pendaftaranapps.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pendaftaranapps.R
import com.example.pendaftaranapps.data.response.DataItem
import com.example.pendaftaranapps.data.response.ListSiswaResponse
import com.example.pendaftaranapps.data.retrofit.ApiConfig
import com.example.pendaftaranapps.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rcListSiswa.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rcListSiswa.addItemDecoration(itemDecoration)

        findSiswa()

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddUpdateActivity::class.java))
        }
    }

    private fun findSiswa(){
        showLoading(true)

        val client = ApiConfig.getApiService().getListSiswa()
        client.enqueue(object : Callback<ListSiswaResponse> {
            override fun onResponse(
                call: Call<ListSiswaResponse>,
                response: Response<ListSiswaResponse>
            ) {
                showLoading(false)

                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        setSiswaData(responseBody.data)
                    }
                }else{
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ListSiswaResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setSiswaData(dataSiswa:List<DataItem>) {
        val adapter = ListSiswaAdapter()
        adapter.submitList(dataSiswa)
        binding.rcListSiswa.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progresBar.visibility = View.VISIBLE
        } else {
            binding.progresBar.visibility = View.GONE
        }
    }
}