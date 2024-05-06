package com.d121211017.stroyappsubmission.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.local.datastore
import com.d121211017.stroyappsubmission.databinding.ActivityRegisterBinding
import com.d121211017.stroyappsubmission.makeToast
import com.d121211017.stroyappsubmission.viewmodel.RegisterViewModel
import com.d121211017.stroyappsubmission.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(application.datastore)
        viewModel = getViewModel(this, pref)

        viewModel.isButtonEnabled.observe(this){
            binding.registerButton.isEnabled = it
        }

        viewModel.loadingRegistration.observe(this){
            binding.apply {
                if(it){
                    binding.registerButton.visibility = View.INVISIBLE
                    binding.pgBar.visibility = View.VISIBLE
                } else {
                    binding.registerButton.visibility = View.VISIBLE
                    binding.pgBar.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.registrationSuccess.observe(this){
            val intent = Intent(this, LoginActivity::class.java)
            makeToast(this, getString(R.string.registration_success))
            startActivity(intent)
            this.finish()
        }

        viewModel.handleResponseError.observe(this){
            handleErrorResponse(it)
        }

        binding.apply {
            edRegisterName.addTextChangedListener(
                onTextChanged = {text, _, _, _ ->
                    viewModel.nameValidation(text.toString().trim())
                }
            )

            edRegisterEmail.addTextChangedListener(
                afterTextChanged = { editable ->
                    viewModel.emailValidation(editable.toString())
                }
            )
            edRegisterPassword.addTextChangedListener(
                afterTextChanged = { editable ->
                    viewModel.passwordValidation(editable.toString())
                }
            )

            registerButton.setOnClickListener {
                viewModel.postRegistration()
            }
        }
        playAnimation()
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity, pref: UserPreferences) : RegisterViewModel {
        val factory = ViewModelFactory.getInstance(appCompatActivity.application, pref)
        return ViewModelProvider(appCompatActivity, factory)[RegisterViewModel::class.java]
    }

    private fun handleErrorResponse(response: Pair<Boolean, String>){
        if(!response.first){
            makeToast(this, response.second)
        } else {
            binding.edRegisterEmail.error = response.second
        }
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.registerImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val edRegisterName = ObjectAnimator.ofFloat(binding.nameInputLayout, View.ALPHA, 1f).setDuration(1000)
        val edRegisterEmail = ObjectAnimator.ofFloat(binding.emailInputLayout, View.ALPHA, 1f).setDuration(1000)
        val edRegisterPassword = ObjectAnimator.ofFloat(binding.passwordInputLayout, View.ALPHA, 1f).setDuration(1000)
        val loginButton = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(1000)

        AnimatorSet().apply {
            playSequentially(edRegisterName, edRegisterEmail, edRegisterPassword, loginButton)
            start()
        }
    }
}