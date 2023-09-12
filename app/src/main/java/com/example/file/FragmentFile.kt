package com.example.file

import Adapter.FileAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.file.databinding.FragmentFileBinding
import java.io.File
import java.nio.file.Files

// memeType => http://androidxref.com/4.4.4_r1/xref/frameworks/base/media/java/android/media/MediaFile.java#174

class FragmentFile(val path: String) : Fragment() , FileAdapter.FileEvent{
    lateinit var binding: FragmentFileBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ourFiles = File(path)
        binding.moduleBala.txtPath.text = ourFiles.name + ">"


        if (ourFiles.isDirectory) {
            // یه لیستی از فایل هایی که تو این مسیر وجود دارن رو بهم بر میگردونه.
//            ourFiles.listFiles()
            val listOfFiles = arrayListOf<File>()
            listOfFiles.addAll(ourFiles.listFiles()!!)

            if (listOfFiles.size > 0) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.imgNoData.visibility = View.GONE

                val myAdapter = FileAdapter(listOfFiles , this)
                binding.recyclerView.adapter = myAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.imgNoData.visibility = View.VISIBLE
            }


        }

    }

    override fun onFileClicked(file: File, type: String) {

        val intent = Intent(Intent.ACTION_VIEW)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            val fileProvider = FileProvider.getUriForFile(
                requireContext() ,
                requireActivity().packageName + ".provider" ,
                file
            )
            intent.setDataAndType(fileProvider , type)
        }else{
            intent.setDataAndType(Uri.fromFile(file) , type)
        }
        // دسترسی دادن به اکتیویتی مقصد برای خوندن اطلاعات
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)

    }

    override fun onFolderClicked(path: String) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_main , FragmentFile(path))
        transaction.addToBackStack(null)
        transaction.commit()

    }


}