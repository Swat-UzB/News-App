package com.example.newsapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var newsAdapter: NewsAdapter
    private var newsList = ArrayList<News>()
    private var page = 1
    private val KEY = "bebc3f22-e284-494c-819d-22de0c817424"
    private val GUARDIAN_URL = "https://content.guardianapis.com/search?page=%d&q=%s&api-key=%s"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener {
            newsList.clear()
            getResponse()
        }

        binding.loadMoreButton.setOnClickListener {
            page += 1
            getResponse()

        }
    }

    private fun getResponse() {
        val url = getUrl()
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            try {
                extractNewsFromJson(response)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        },
            { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
            })
        queue.add(stringRequest)
    }

    private fun extractNewsFromJson(response: String) {
        val jsonObject = JSONObject(response)
        val getResponse = jsonObject.getJSONObject("response")
        val getResultArray = getResponse.getJSONArray("results")

        for (i in 0..9) {
            newsList.add(
                News(
                    getResultArray.getJSONObject(i).get("webTitle") as String,
                    getResultArray.getJSONObject(i).get("webUrl") as String
                )
            )
        }
        newsAdapter = NewsAdapter(newsList)
        binding.listResult.adapter = newsAdapter
    }

    private fun getUrl(): String {
        val searchWord = binding.searchEditText.text.toString()
        return String.format(GUARDIAN_URL, page, searchWord, KEY)
    }
}