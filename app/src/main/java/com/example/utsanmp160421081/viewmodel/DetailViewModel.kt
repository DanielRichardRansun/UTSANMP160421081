package com.example.utsanmp160421081.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.utsanmp160421081.model.News
import com.example.utsanmp160421081.view.DetailBeritaFragmentArgs

class DetailViewModel(application: Application, savedStateHandle: SavedStateHandle):
    AndroidViewModel(application) {
    val newsLD = MutableLiveData<News>()
    val TAG = "volleyTag"

    var id = DetailBeritaFragmentArgs.fromSavedStateHandle(savedStateHandle).newsId

    private var queue: RequestQueue? = null

    fun getDetail() {
        queue = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/anmp/uts/news.php?id=${id}"

        val stringRequest = StringRequest(
            Request.Method.GET, url, {
                val sType = object : TypeToken<News>() {}.type
                val result = Gson().fromJson<News>(it, sType)
                val news1 = result as News

                newsLD.value = news1

                Log.d("show_volley", it)
            },
            {
                Log.e("show_volley", it.toString())
            }
        )
        stringRequest.tag = TAG
        queue?.add(stringRequest)
    }
}