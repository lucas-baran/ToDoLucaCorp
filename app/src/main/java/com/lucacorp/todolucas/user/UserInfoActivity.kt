package com.lucacorp.todolucas.user

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.lucacorp.todolucas.BuildConfig
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.ActivityUserInfoBinding
import com.lucacorp.todolucas.network.Api
import com.lucacorp.todolucas.network.UserInfo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserInfoBinding

    private val userViewModel: UserInfoViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { accepted ->
            if (accepted) openCamera()
            else showExplanationDialog()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }

        binding.uploadImageButton.setOnClickListener {
            pickImage()
        }

        binding.validateUserInfoButton.setOnClickListener {
            userViewModel.updateUserInfo(UserInfo(
                email = binding.emailEditText.text.toString(),
                firstName = binding.firstnameEditText.text.toString(),
                lastName = binding.lastnameEditText.text.toString()))
            finish()
        }

        // on lance une coroutine car `collect` est `suspend`
        lifecycleScope.launch {
            userViewModel.userInfo.collectLatest {
                binding.userImage.load(it?.avatar) {
                    // affiche une image en cas d'erreur:
                    error(R.drawable.ic_launcher_background)
                    transformations(CircleCropTransformation())
                }

                binding.emailEditText.setText(userViewModel.userInfo.value.email);
                binding.firstnameEditText.setText(userViewModel.userInfo.value.firstName);
                binding.lastnameEditText.setText(userViewModel.userInfo.value.lastName);
            }
        }
    }

    override fun onResume() {
        super.onResume()

        userViewModel.refresh()
    }

    private fun askCameraPermissionAndOpenCamera() {
        val camPermission = Manifest.permission.CAMERA
        val permissionStatus = checkSelfPermission(camPermission)
        val isAlreadyAccepted = permissionStatus == PackageManager.PERMISSION_GRANTED
        when {
            isAlreadyAccepted -> openCamera()
            shouldShowRequestPermissionRationale(camPermission) -> showExplanationDialog()
            else -> requestPermissionLauncher.launch(camPermission)
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this)
            .setMessage("On a besoin de la caméra sivouplé ! 🥺")
            .setPositiveButton("Bon, ok") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun handleImage(uri: Uri) {
        lifecycleScope.launch {
            userViewModel.updateUserAvatar(convert(uri))
        }
    }

    // create a temp file and get a uri for it
    private val photoUri by lazy {
        FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID +".fileprovider",
            File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) handleImage(photoUri)
        else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
    }

    // use
    private fun openCamera() = takePicture.launch(photoUri)

    // convert
    private fun convert(uri: Uri) =
        MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )

    // register
    private val pickInGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null) Toast.makeText(this, "Oh no! 😢", Toast.LENGTH_LONG).show()
        else handleImage(it)
    }

    // use
    private fun pickImage() = pickInGallery.launch("image/*")
}