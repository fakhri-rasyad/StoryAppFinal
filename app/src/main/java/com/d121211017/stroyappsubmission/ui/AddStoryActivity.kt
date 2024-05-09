package com.d121211017.stroyappsubmission.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.d121211017.stroyappsubmission.R
import com.d121211017.stroyappsubmission.data.local.UserPreferences
import com.d121211017.stroyappsubmission.data.local.datastore
import com.d121211017.stroyappsubmission.databinding.ActivityAddStoryBinding
import com.d121211017.stroyappsubmission.getImageUri
import com.d121211017.stroyappsubmission.makeToast
import com.d121211017.stroyappsubmission.viewmodel.AddStoryViewModel
import com.d121211017.stroyappsubmission.viewmodel.ViewModelFactory
import kotlinx.coroutines.runBlocking

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddStoryBinding
    private lateinit var viewModel : AddStoryViewModel
    private var currentImageUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UserPreferences.getInstance(application.datastore)

        viewModel = getViewModel(this)

        viewModel.apply {
            imageUri.observe(this@AddStoryActivity){
                if(it != null){
                    binding.addStoryThumbnail.setImageURI(it)
                }
            }
            isLoading.observe(this@AddStoryActivity){
                binding.apply {
                    pgBar.visibility = if(it) View.VISIBLE else View.INVISIBLE
                    buttonAdd.visibility = if(it) View.INVISIBLE else View.VISIBLE
                }
            }
            response.observe(this@AddStoryActivity){
                makeToast(this@AddStoryActivity, it)
            }

            isUploadSuccess.observe(this@AddStoryActivity){
                if(it){
                    finish()
                }
            }
        }

        binding.apply {
            galleryButton.setOnClickListener {
                startGallery()
            }

            cameraButton.setOnClickListener {
                startCamera()
            }

            buttonAdd.setOnClickListener {
                val desc = this.edAddDescription.text.toString()
                val uri = runBlocking { viewModel.imageUri.value }
                if(desc.isNotEmpty() && uri != null){
                    viewModel.postStory(desc)
                } else if(desc.isEmpty()){
                    this.descriptionInputLayout.error = getString(R.string.empy_error)
                } else if(uri == null){
                    makeToast(this@AddStoryActivity, getString(R.string.empty_image_error))
                }
            }
        }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setPhotoUri(uri)
        } else {
            Toast.makeText(this, getString(R.string.empty_image_error), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.setPhotoUri(currentImageUri!!)
        }
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity) : AddStoryViewModel {
        val factory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(appCompatActivity, factory)[AddStoryViewModel::class.java]
    }
}