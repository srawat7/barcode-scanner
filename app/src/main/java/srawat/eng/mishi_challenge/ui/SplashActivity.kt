package srawat.eng.mishi_challenge.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import srawat.eng.mishi_challenge.R

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce_anim)

//        val textViewToBounce = findViewById<TextView>(R.id.textViewToBounce)
//        textViewToBounce.setOnClickListener {
//            textViewToBounce.startAnimation(bounceAnimation)
//        }

        val imageToBounce = findViewById<ImageView>(R.id.mishi_icon)
        imageToBounce.startAnimation(bounceAnimation)

        Handler().postDelayed({
            //startHomeActivity()
            if(allPermissionsGranted()) {
                startHomeActivity()
            } else {
                checkPermissions()
            }
        }, 2000)
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getRequiredPermissions(): Array<String?> {
        try {
            val requiredPermissions = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_PERMISSIONS
            )
            val requestPermissions = requiredPermissions.requestedPermissions

            if (requestPermissions != null && requestPermissions.isNotEmpty()) {
                return requestPermissions
            } else {
                return arrayOfNulls(0)
            }
        } catch (e: Exception) {
            return arrayOfNulls(0)
        }
    }

    private fun isPermissionGranted(permission: String?) =
        ContextCompat.checkSelfPermission(
            this,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED


    private fun checkPermissions() {
        val permissionsToBeGranted: MutableList<String?> = ArrayList()
        for (permission in getRequiredPermissions()) {
            if (!isPermissionGranted(permission))
                permissionsToBeGranted.add(permission)
        }

        if (permissionsToBeGranted.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToBeGranted.toTypedArray(),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getRequiredPermissions()) {
            if (!isPermissionGranted(permission))
                return false
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (allPermissionsGranted()) {
            startHomeActivity()
        } else {
            val alertBuilder = AlertDialog.Builder(this).setTitle("Permission Required")
                .setMessage("All permissions required to proceed")
                .setPositiveButton("Grant Permissions") { dialog, which -> checkPermissions() }

            alertBuilder.show()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 1
    }
}