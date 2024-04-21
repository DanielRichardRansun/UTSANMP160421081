package com.example.utsanmp160421081.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.utsanmp160421081.model.User
import org.json.JSONObject

class UserViewModel(application: Application, savedStateHandle: SavedStateHandle):
    AndroidViewModel(application) {
    val userLD = MutableLiveData<User?>()
    private val statusLD: MutableLiveData<String?> = MutableLiveData()
    val TAG = "volleyTag"


    private var queue: RequestQueue? = null

    fun update(id:String, password:String, firstname :String, lastname:String) {
        val q = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/anmp/uts/update.php"
        var stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("apiresult", response)
                val obj = JSONObject(response)
                if (obj.getString("result") == "OK") {
                    statusLD.value = obj.toString()
                } else {
                    if (obj.has("msg")) {
                        val msg = obj.getString("msg")
                        Log.d("error_volley", msg)
                    } else {
                        Log.d("error_volley", "Unknown error occurred")
                    }
                    userLD.value = null
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["password"] = password
                params["firstname"] = firstname
                params["lastname"] = lastname
                params["userid"] = id

                return params
            }
        }
        stringRequest.tag = TAG
        q.add(stringRequest)
    }


    fun login(password: String, username: String) {
        val q = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/anmp/uts/login.php"
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("apiresult", response)
                val obj = JSONObject(response)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONObject("data")
                    val sType = object : TypeToken<User>() {}.type
                    val result = Gson().fromJson<User>(data.toString(), sType)
                    val user1 = result as User

                    userLD.value = user1

                    Log.d("show_volley", result.toString())
                } else {
                    if (obj.has("msg")) {
                        val msg = obj.getString("msg")
                        Log.d("error_volley", msg)
                    } else {
                        Log.d("error_volley", "Unknown error occurred")
                    }
                    userLD.value = null
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["pass"] = password
                return params
            }
        }
        stringRequest.tag = TAG
        q.add(stringRequest)
    }

    fun regis(email:String, firstname:String, lastname:String, repassword: String, username: String) {
        val q = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/anmp/uts/registrasi.php"
        var stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("apiresult", response)
                val obj = JSONObject(response)
                if (obj.getString("result") == "OK") {
                    statusLD.value = obj.toString()
                } else {
                    val msg = obj.getString("msg")
                    Log.d("error_volley", msg)
                    statusLD.value = null
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["email"] = email
                params["pass"] = repassword
                params["firstname"] = firstname
                params["lastname"] = lastname

                return params
            }
        }
        stringRequest.tag = TAG
        q.add(stringRequest)
    }

    fun display_user(id: String){
        val q = Volley.newRequestQueue(getApplication())
        val url = "http://10.0.2.2/anmp/uts/userdisplay.php"
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("cekparams", response)
                val obj = JSONObject(response)
                if (obj.getString("result") == "OK") {
                    val data = obj.getJSONObject("data")
                    val sType = object : TypeToken<User>() {}.type
                    val result = Gson().fromJson<User>(data.toString(), sType)
                    val user1 = result as User

                    userLD.value = user1

                    Log.d("show_volley", result.toString())

                } else {
                    val msg = obj.getString("msg")
                    Log.d("error_volley", msg)
                    userLD.value = null
                }
            },
            Response.ErrorListener {
                Log.e("apiresult", it.message.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["userid"] = id
                return params
            }
        }
        stringRequest.tag = TAG
        q.add(stringRequest)
    }

    //memberikan akses ke LiveData yang mendapatkan status hasil dari API.
    fun getStatusLiveData(): MutableLiveData<String?> {
        return statusLD
    }

}