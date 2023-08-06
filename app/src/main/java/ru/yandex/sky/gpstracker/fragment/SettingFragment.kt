package ru.yandex.sky.gpstracker.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ru.yandex.sky.gpstracker.R


class SettingFragment : PreferenceFragmentCompat() {
    private lateinit var timePref: Preference
    private lateinit var colorPref: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        timePref = findPreference("update_time")!!
        colorPref = findPreference("update_color")!!
        val changeListener = onChangeListener()
        timePref.onPreferenceChangeListener = changeListener
        colorPref.onPreferenceChangeListener = changeListener
        initPref()
    }

    private fun onChangeListener(): Preference.OnPreferenceChangeListener {
        return Preference.OnPreferenceChangeListener { pref, values ->
            when(pref.key) {
                "update_time" -> updateTime(values.toString())
                "update_color" -> updateColor(values)
            }
            true
        }
    }

    private fun updateTime(values: String) {
        val nameArray = resources.getStringArray(R.array.loc_time_update_name)
        val valueArray = resources.getStringArray(R.array.loc_time_update_values)
        val tittle = timePref.title.toString().substringBefore(":")
        timePref.title = "$tittle: ${nameArray[valueArray.indexOf(values)]}"
    }

    private fun updateColor(values: Any) {
        colorPref.icon?.setTint(Color.parseColor(values.toString()))
    }

    private fun initPref() {
        val pref = timePref.preferenceManager.sharedPreferences
        val nameArray = resources.getStringArray(R.array.loc_time_update_name)
        val valueArray = resources.getStringArray(R.array.loc_time_update_values)
        val tittle = timePref.title
        timePref.title = "$tittle: ${
            nameArray[valueArray
                .indexOf(pref?.getString("update_time", "3000"))]
        }"


        val colorTrack = pref?.getString("update_color", "#999999")
        colorPref.icon?.setTint(Color.parseColor(colorTrack))
    }

}
