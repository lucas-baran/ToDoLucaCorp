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
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.lucacorp.todolucas.BuildConfig
import com.lucacorp.todolucas.R
import com.lucacorp.todolucas.databinding.ActivityUserInfoBinding
import com.lucacorp.todolucas.network.Api
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserInfoBinding
    private val userWebService = Api.userWebService

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

        lifecycleScope.launch {
            val result = userWebService.getInfo()
            if (result.isSuccessful) {
                binding.userImage.load(result.body()?.avatar) {
                    // affiche une image en cas d'erreur:
                    error(R.drawable.ic_launcher_background)
                    transformations(CircleCropTransformation())
                }
            }
            else{
                binding.userImage.load(R.drawable.ic_launcher_background) {
                    transformations(CircleCropTransformation())
                }
            }
        }
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
            val result = userWebService.updateAvatar(convert(uri))
            if (result.isSuccessful) {
                binding.userImage.load(result.body()?.avatar) {
                    // affiche une image en cas d'erreur:
                    error(R.drawable.ic_launcher_background)
                    transformations(CircleCropTransformation())
                }
            }
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
}