package ru.yandex.sky.gpstracker.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.yandex.sky.gpstracker.databinding.FragmentMainBinding
import ru.yandex.sky.gpstracker.utils.checkPermission
import ru.yandex.sky.gpstracker.utils.showToast


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingOsm()
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permission()
        checkLookPermission()

    }

    private fun settingOsm() {
        Configuration.getInstance()
            .load(
                activity as AppCompatActivity,
                activity?.getSharedPreferences("osm_pref", Context.MODE_PRIVATE)
            )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }

    private fun initOsm() = with(binding) {
        mapView.controller.setZoom(17.0)
        val provider = GpsMyLocationProvider(activity)
        val myOverload = MyLocationNewOverlay(provider, mapView)
        myOverload.enableMyLocation()
        myOverload.enableFollowLocation()
        myOverload.runOnFirstFix {
            mapView.overlays.clear()
            mapView.overlays.add(myOverload)
        }
    }

    private fun permission() {
        pLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    initOsm()
                } else {
                    showToast("Вы не дали разрешение на определение местоположения")
                }
            }
    }

    private fun checkLookPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkPermissionVersionAfter10()
        } else {
            checkPermissionVersionBefore10()
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermissionVersionAfter10() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            && checkPermission(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        ) {
            initOsm()
        } else {
            pLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            )
        }
    }

    private fun checkPermissionVersionBefore10() {
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            initOsm()
        } else {
            pLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }


}