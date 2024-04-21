package com.example.utsanmp160421081.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.utsanmp160421081.databinding.ActivityRegisterBinding
import com.example.utsanmp160421081.viewmodel.UserViewModel
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: UserViewModel

    // Variabel untuk menyimpan data pengguna
    var email : String = ""
    var password : String = ""
    var repassword : String = ""
    var username : String = ""
    var firstname : String = ""
    var lastname : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        //Untuk botton Register
        binding.btnRegist.setOnClickListener {
            val username = binding.txtUserReg.text.toString()
            val email= binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val repassword= binding.txtRePassword.text.toString()
            val firstname = binding.txtFirstName.text.toString()
            val lastname= binding.txtLastNameProf.text.toString()

            if(password != repassword)
            {
                Toast.makeText(application, "Password dan Repassword Tidak Sama", Toast.LENGTH_SHORT).show()
            }
            else if(password == "" && repassword == "")
            {
                Toast.makeText(application, "Password atau Repassword harus Diisi", Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.regis(email,firstname,lastname,repassword,username)
                observeViewModel()
            }
        }
    }

    //melihat perubahan pada ViewModel
    private fun observeViewModel() {

        viewModel.getStatusLiveData().observe(this, Observer {apiResult->

            if (apiResult != null) {
                val obj = JSONObject(apiResult)
                if (obj.getString("result") == "OK") {
                    Toast.makeText(application, "Regist Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(application, "Regist Unsuccess", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(application, "Register Unseccess", Toast.LENGTH_SHORT).show()
            }

        })
    }

}
