package ru.yandex.sky.gpstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.yandex.sky.gpstracker.databinding.ActivityMainBinding
import ru.yandex.sky.gpstracker.fragment.ListFragment
import ru.yandex.sky.gpstracker.fragment.MainFragment
import ru.yandex.sky.gpstracker.fragment.SettingFragment
import ru.yandex.sky.gpstracker.utils.openFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onButtonClick()
        openFragment(MainFragment.newInstance())
    }


    private fun onButtonClick() {
        binding.bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> openFragment(MainFragment.newInstance())
                R.id.list -> openFragment(ListFragment.newInstance())
                R.id.setting -> openFragment(SettingFragment())
            }
            true
        }
    }
}