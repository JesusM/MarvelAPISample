package com.marvelsample.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marvelsample.app.databinding.MainActivityBinding

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }
}