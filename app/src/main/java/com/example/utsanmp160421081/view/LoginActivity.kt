package com.example.utsanmp160421081.view


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.utsanmp160421081.databinding.ActivityLoginBinding
import com.example.utsanmp160421081.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var shared: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // Variabel untuk menyimpan data pengguna
    var userId : Int = 0
    var email : String = ""
    var pass : String = ""
    var username : String = ""
    var firstname : String = ""
    var lastname : String = ""

    // Konstanta untuk menyimpan ke SharedPreferences
    companion object{
        val EMAIL = "EMAIL"
        val USERID = "USERID"
        val USERNAME = "USERNAME"
        val PASS = "PASS"
        val FIRSTNAME = "FIRSTNAME"
        val LASTNAME = "LASTNAME"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        //Mengirim dan mendapatkan data shared
        var sharedFile = "com.example.anmp160421081"
        shared = getSharedPreferences(sharedFile, Context.MODE_PRIVATE)

        // Memeriksa apakah pengguna sudah masuk sebelumnya
        var checkLogin = shared.getInt("USERID", -1)
        if (checkLogin != -1) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }

        //untuk bottonnya
        binding.btnLogIn.setOnClickListener {
            username = binding.txtUserName.text.toString()
            pass = binding.txtPass.text.toString()
            if (username != "" && pass != "") {
                viewModel.login(pass, username)
                observeViewModel()

            } else {
                Toast.makeText(
                    this,
                    "User dan Pass harus diisi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        //untuk button registernya
        binding.txtRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)

        }

    }

    //melihat perubahan pada ViewModel
    private fun observeViewModel() {
        viewModel.userLD.observe(this, Observer {User->
            if (User != null) {
                // Jika data pengguna ditemukan
                userId = User.id?.toInt() ?: -1
                email = User.email.toString()
                pass = User.password.toString()
                username = User.username.toString()
                firstname = User.firstName.toString()
                lastname = User.lastName.toString()

                // Menyimpan data pengguna ke SharedPreferences
                editor = shared.edit()
                editor.putInt(USERID, userId)
                editor.putString(EMAIL, email)
                editor.putString(USERNAME, username)
                editor.putString(PASS, pass)
                editor.putString(FIRSTNAME, firstname)
                editor.putString(LASTNAME, lastname)
                editor.apply()

                // Pindah ke MainActivity setelah login berhasil
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            else
            {
                Toast.makeText(application, "data anda salah", Toast.LENGTH_SHORT).show()
            }

        })

    }
}


