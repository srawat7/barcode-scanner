package srawat.eng.mishi_challenge.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_home.*
import srawat.eng.mishi_challenge.R
import srawat.eng.mishi_challenge.di.inject
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {

    lateinit var currentFragment: Fragment
    lateinit var cartFragment: Fragment
    lateinit var profileFragment: Fragment
    lateinit var cameraFragment: Fragment

    @Inject
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        inject(this)
        viewModel.getItems()
        initObservers()
//        if (allPermissionsGranted()) {
//            initNavigation()
//        } else {
//            checkPermissions()
//        }
        initNavigation()
    }

    private fun initObservers() {
        viewModel.items.observe(this@HomeActivity, Observer<ItemsUiModel> {
            if (it.items.isNotEmpty()) {
                val badge = bottom_nav.getOrCreateBadge(R.id.cart_nav)
                badge.isVisible = true
                badge.number = it.items.size
            } else {
                val badge = bottom_nav.getBadge(R.id.cart_nav)
                if (badge != null) {
                    badge.isVisible = false
                    badge.clearNumber()
                }
            }
        })
    }

    private fun initNavigation() {
        cameraFragment = CameraFragment()
        cartFragment = CartFragment()
        profileFragment = ProfileFragment()

        supportFragmentManager.beginTransaction().add(R.id.container_fragment_main, cameraFragment)
            .hide(cameraFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment_main, cartFragment)
            .hide(cartFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment_main, profileFragment)
            .hide(profileFragment).commit()

        currentFragment = cameraFragment
        updateFragment(cameraFragment)

        bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.cart_nav -> {
                    (cameraFragment as CameraFragment).isCurrentFragment(false)
                    updateFragment(cartFragment)
                }
                R.id.camera_nav -> {
                    (cameraFragment as CameraFragment).isCurrentFragment(true)
                    updateFragment(cameraFragment)
                }
                R.id.profile_nav -> {
                    (cameraFragment as CameraFragment).isCurrentFragment(false)
                    updateFragment(profileFragment)
                }
            }
            true
        }

        bottom_nav.selectedItemId = R.id.camera_nav

    }

    private fun updateFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(currentFragment).show(fragment).commit()
        currentFragment = fragment
    }

//    private fun getRequiredPermissions(): Array<String?> {
//        try {
//            val requiredPermissions = packageManager.getPackageInfo(
//                packageName,
//                PackageManager.GET_PERMISSIONS
//            )
//            val requestPermissions = requiredPermissions.requestedPermissions
//
//            if (requestPermissions != null && requestPermissions.isNotEmpty()) {
//                return requestPermissions
//            } else {
//                return arrayOfNulls(0)
//            }
//        } catch (e: Exception) {
//            return arrayOfNulls(0)
//        }
//    }
//
//    private fun isPermissionGranted(permission: String?) =
//        ContextCompat.checkSelfPermission(
//            this,
//            permission!!
//        ) == PackageManager.PERMISSION_GRANTED
//
//
//    private fun checkPermissions() {
//        val permissionsToBeGranted: MutableList<String?> = ArrayList()
//        for (permission in getRequiredPermissions()) {
//            if (!isPermissionGranted(permission))
//                permissionsToBeGranted.add(permission)
//        }
//
//        if (permissionsToBeGranted.isNotEmpty()) {
//            ActivityCompat.requestPermissions(
//                this,
//                permissionsToBeGranted.toTypedArray(),
//                PERMISSIONS_REQUEST_CODE
//            )
//        }
//    }
//
//    private fun allPermissionsGranted(): Boolean {
//        for (permission in getRequiredPermissions()) {
//            if (!isPermissionGranted(permission))
//                return false
//        }
//
//        return true
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (allPermissionsGranted()) {
//            initNavigation()
//        } else {
//            val alertBuilder = AlertDialog.Builder(this).setTitle("Permission Required")
//                .setMessage("All permissions required to proceed")
//                .setPositiveButton("Grant Permissions") { dialog, which -> checkPermissions() }
//
//            alertBuilder.show()
//        }
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//
//    companion object {
//        private const val PERMISSIONS_REQUEST_CODE = 1
//    }
}