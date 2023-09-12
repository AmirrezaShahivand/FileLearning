package com.example.file

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.example.file.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

       // val file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = getExternalFilesDir(null)!!
        val path = file.path


        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_main , FragmentFile(path))
        transaction.commit()



    }
}