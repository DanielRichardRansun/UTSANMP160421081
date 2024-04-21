package com.example.utsanmp160421081.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.utsanmp160421081.databinding.FragmentProfileBinding
import com.example.utsanmp160421081.viewmodel.UserViewModel
import org.json.JSONObject

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var shared: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // Variabel untuk menyimpan data pengguna
    var userId : Int = 0
    var username : String = ""
    var firstname : String = ""
    var lastname : String = ""
    var email : String = ""
    var password : String = ""
    var passwordprof : String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Mendapatkan SharedPreferences
        var sharedFile= "com.example.anmp160421081"
        shared = this.requireActivity().getSharedPreferences(sharedFile, Context.MODE_PRIVATE)
        editor  = shared.edit()

        // Mendapatkan data pengguna dari SharedPreferences
        password = shared.getString(LoginActivity.PASS,"").toString()
        username = shared.getString(LoginActivity.USERNAME,"").toString()
        // // Menampilkan data pengguna di UI
        binding.txtUsernameProf.setText(username)
        binding.txtOldPass.setText(password)
        binding.txtUsernameProf.isEnabled = false
        binding.txtOldPass.isEnabled = false

        //Untuk Button Logout
        binding.btnLogout.setOnClickListener {
            // Membersihkan data pengguna di SharedPreferences saat logout
            editor.putInt(LoginActivity.USERID, -1)
            editor.putString(LoginActivity.EMAIL, "")
            editor.putString(LoginActivity.PASS, "")
            editor.putString(LoginActivity.LASTNAME, "")
            editor.putString(LoginActivity.FIRSTNAME, "")
            editor.putString(LoginActivity.PASS, "")
            editor.apply()
            Log.d("userid", LoginActivity.USERID)

            val intent = Intent(this.activity, LoginActivity::class.java)
            startActivity(intent)
            this.requireActivity().finish()
        }

        //untuk botton update
        binding.btnUpdate.setOnClickListener {
            shared= requireActivity().getSharedPreferences(sharedFile, Context.MODE_PRIVATE)
            // Mendapatkan data pengguna yang diperbarui dari input fields
            userId = shared.getInt(LoginActivity.USERID,-1)
            password = shared.getString(LoginActivity.PASS,"").toString()
            username = binding.txtUsernameProf.text.toString()
            passwordprof = binding.txtPasswordProfile.text.toString()
            firstname = binding.txtFirstNameProfile.text.toString()
            lastname = binding.txtLastProf.text.toString()


            viewModel.update(userId.toString(),passwordprof,firstname,lastname)
            observeViewModel()

        }

    }

    //melihat perubahan pada ViewModel
    fun observeViewModel(){
        userId = shared.getInt(LoginActivity.USERID,-1)

        viewModel.getStatusLiveData().observe(viewLifecycleOwner, Observer {apiResult->

            if (apiResult != null && apiResult.isNotEmpty()) {
                val obj = JSONObject(apiResult)
                if (obj.getString("result") == "OK") {
                    Toast.makeText(requireContext(), "Updateing Success", Toast.LENGTH_SHORT).show()
                    Log.d("userid",LoginActivity.USERID)
                    viewModel.display_user(userId.toString())
                    displayObserveViewModel()

                } else {

                    Log.d("Update","...")
                }
            }
            else {

                Log.d("Update","...")
            }

        })

    }

    //mengamati perubahan pada ViewModel setelah pembaruan data pengguna
    fun displayObserveViewModel()
    {
        viewModel.userLD.observe(viewLifecycleOwner, Observer {User->

            if (User != null) {
                //menampilkan data pengguna yang berhasil diperbahario
                userId = User.id?.toInt() ?: -1
                email = User.email.toString()
                password = User.password.toString()
                username = User.username.toString()
                firstname = User.firstName.toString()
                lastname = User.lastName.toString()

                //menyimpan ke shared
                editor = shared.edit()
                editor.putInt(LoginActivity.USERID, userId)
                editor.putString(LoginActivity.EMAIL, email)
                editor.putString(LoginActivity.USERNAME, username)
                editor.putString(LoginActivity.PASS, passwordprof)
                editor.putString(LoginActivity.FIRSTNAME, firstname)
                editor.putString(LoginActivity.LASTNAME, lastname)
                editor.apply()

                password = shared.getString(LoginActivity.PASS,"").toString()
                username = shared.getString(LoginActivity.USERNAME,"").toString()
                binding.txtUsernameProf.setText(username)
                binding.txtOldPass.setText(password)
                binding.txtUsernameProf.isEnabled = false
                binding.txtOldPass.isEnabled = false
                binding.txtFirstNameProfile.setText("")
                binding.txtLastProf.setText("")
                binding.txtPasswordProfile.setText("")


            } else {
                Toast.makeText(requireContext(), "Gagal update data", Toast.LENGTH_SHORT).show()
            }

        })

    }



}