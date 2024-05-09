package com.d121211017.stroyappsubmission.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.databinding.ActivityLoginBinding
import com.d121211017.stroyappsubmission.makeToast
import com.d121211017.stroyappsubmission.viewmodel.LoginViewModel
import com.d121211017.stroyappsubmission.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel = getViewModel(this)
        setContentView(binding.root)

        viewModel.isButtonEnabled.observe(this){
            binding.loginButton.isEnabled = it
        }


        viewModel.isLoadingLogin.observe(this){
            binding.apply {
                if(it){
                    loginButton.visibility = View.INVISIBLE
                    pgBar.visibility = View.VISIBLE
                } else {
                    loginButton.visibility = View.VISIBLE
                    pgBar.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.errorResponse.observe(this){
            when (it.second) {
                "Invalid password" -> {
                    binding.edLoginPassword.error = "Invalid password"
                }
                "User not found" -> {
                    binding.edLoginEmail.error = "User not found"
                }
                else -> {
                    makeToast(this, it.second)
                }
            }
        }

        viewModel.isLoginSuccess.observe(this){
            if(it){
                val intent = Intent(this, StoryListActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }

        binding.apply {
            edLoginEmail.addTextChangedListener (
                afterTextChanged = {editable ->
                    viewModel.emailValidation(editable.toString())
                }
            )

            edLoginPassword.addTextChangedListener(
                afterTextChanged = {editable ->
                    viewModel.passwordValidation(editable.toString())
                }
            )

            loginButton.setOnClickListener {
                viewModel.postLogin()
            }
        }
        playAnimation()
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity) : LoginViewModel {
        val factory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(appCompatActivity, factory)[LoginViewModel::class.java]
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.loginImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val edLoginEmail = ObjectAnimator.ofFloat(binding.emailInputLayout, View.ALPHA, 1f).setDuration(1000)
        val edLoginPassword = ObjectAnimator.ofFloat(binding.passwordInputLayout, View.ALPHA, 1f).setDuration(1000)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(edLoginEmail, edLoginPassword, loginButton)
            start()
        }
    }
}