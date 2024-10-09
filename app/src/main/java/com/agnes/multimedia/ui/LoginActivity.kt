package com.agnes.multimedia.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.agnes.multimedia.R
import com.agnes.multimedia.databinding.ActivityLoginBinding
import com.agnes.multimedia.model.LoginRequest
import com.agnes.multimedia.model.LoginResponse
import com.agnes.multimedia.utils.Constants
import com.agnes.multimedia.viewmodel.LoginViewModel
import java.util.prefs.Preferences

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    val loginViewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        redirectUser()
        setContentView(binding.root)
    }

    fun redirectUser(){
        val sharedPreferences = getSharedPreferences(Constants.PREFS,Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(Constants.ACCESS_TOKEN,"")
        if(token!!.isNotBlank()){
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.btnLogin.setOnClickListener { validateLogin() }

        loginViewModel.errorLiveData.observe(this){error ->
            Toast.makeText(this,error, Toast.LENGTH_LONG).show()
        }
        loginViewModel.loginLiveData.observe(this){loginResponse ->
            Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
            persistLogin(loginResponse)
            startActivity(Intent(this,HomeActivity::class.java))

        }
    }
    private fun persistLogin(loginResponse: LoginResponse){
        val sharedPreferences = getSharedPreferences(Constants.PREFS,Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.FIRST_NAME,loginResponse.firstname)
        editor.putString(Constants.LAST_NAME,loginResponse.lastname)
        editor.putString(Constants.ACCESS_TOKEN,loginResponse.accessToken)
        editor.apply()
    }
    private fun validateLogin(){
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val error = false

        if (username.isBlank()){
            binding.tilUsername.error = "Username Required"
        }
        if (password.isBlank()){
            binding.tilPassword.error = "Passwod Required"
        }
        if(!error){
            val loginRequest = LoginRequest(username, password)
            loginViewModel.login(loginRequest)
        }
    }
}