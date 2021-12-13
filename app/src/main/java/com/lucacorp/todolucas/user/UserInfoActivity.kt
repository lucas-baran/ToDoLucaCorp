package com.lucacorp.todolucas.user

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import coil.load
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

        binding.userImage.load("https://cdn.discordapp.com/avatars/188385753686999040/164e4788ec23ffc3e58fe39cb3451694.png")

        binding.takePictureButton.setOnClickListener {
            askCameraPermissionAndOpenCamera()
        }

        lifecycleScope.launch {
            val result = userWebService.getInfo()
            if (result.isSuccessful) {
                binding.userImage.load(result.body()?.avatar) {
                    // affiche une image en cas d'erreur:
                    error(R.drawable.ic_launcher_background)
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

    // register
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        val tmpFile = File.createTempFile("avatar", "jpeg")
        tmpFile.outputStream().use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        handleImage(tmpFile.toUri())
    }

    private fun handleImage(uri: Uri) {
        lifecycleScope.launch {
            val result = userWebService.updateAvatar(convert(uri))
            if (result.isSuccessful) {
                binding.userImage.load(result.body()?.avatar) {
                    // affiche une image en cas d'erreur:
                    error(R.drawable.ic_launcher_background)
                }
            }
        }
    }

    // use
    private fun openCamera() = takePicture.launch(null)

    // convert
    private fun convert(uri: Uri) =
        MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )
}