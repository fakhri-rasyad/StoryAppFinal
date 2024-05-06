package com.d121211017.stroyappsubmission.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.local.datastore
import com.d121211017.stroyappsubmission.databinding.ActivityMainBinding
import com.d121211017.stroyappsubmission.viewmodel.MainViewModel
import com.d121211017.stroyappsubmission.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private  lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(application.datastore)
        viewModel = getViewModel(this, pref)

        if(viewModel.checkForUserPref()){
            val intent = Intent(this, StoryListActivity::class.java)
            finish()
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity, pref: UserPreferences) : MainViewModel {
        val factory = ViewModelFactory.getInstance(appCompatActivity.application, pref)
        return ViewModelProvider(appCompatActivity, factory)[MainViewModel::class.java]
    }
}