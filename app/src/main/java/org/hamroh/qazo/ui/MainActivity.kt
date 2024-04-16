package org.hamroh.qazo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.hamroh.qazo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding get() = _binding!!
    private var _binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}