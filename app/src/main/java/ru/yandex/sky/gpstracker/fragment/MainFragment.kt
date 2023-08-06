package ru.yandex.sky.gpstracker.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import ru.yandex.sky.gpstracker.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initOsm()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingOsm()
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
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
        mapView.controller.setZoom(20.0)
        val provider = GpsMyLocationProvider(activity)
        val myOverload = MyLocationNewOverlay(provider, mapView)
        myOverload.enableMyLocation()
        myOverload.enableFollowLocation()
        myOverload.runOnFirstFix{
            mapView.overlays.clear()
            mapView.overlays.add(myOverload)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}