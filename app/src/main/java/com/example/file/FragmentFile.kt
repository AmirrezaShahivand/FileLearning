package com.example.file

import Adapter.FileAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.example.file.databinding.DialogAddFileBinding
import com.example.file.databinding.DialogAddFolderBinding
import com.example.file.databinding.FragmentFileBinding
import java.io.File
import java.nio.file.Files

// memeType => http://androidxref.com/4.4.4_r1/xref/frameworks/base/media/java/android/media/MediaFile.java#174

class FragmentFile(val path: String) : Fragment(), FileAdapter.FileEvent {
    lateinit var binding: FragmentFileBinding
    lateinit var myAdapter: FileAdapter

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFileBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ourFiles = File(path)
        binding.moduleBala.txtPath.text = ourFiles.name + ">"


        if (ourFiles.isDirectory) {
            // یه لیستی از فایل هایی که تو این مسیر وجود دارن رو بهم بر میگردونه.
//            ourFiles.listFiles()
            val listOfFiles = arrayListOf<File>()
            listOfFiles.addAll(ourFiles.listFiles()!!)

            myAdapter = FileAdapter(listOfFiles, this)
            binding.recyclerView.adapter = myAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(context)

            if (listOfFiles.size > 0) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.imgNoData.visibility = View.GONE


            } else {
                binding.recyclerView.visibility = View.GONE
                binding.imgNoData.visibility = View.VISIBLE
            }


        }

        binding.moduleBala.addFolder.setOnClickListener {
            createNewFolder()
        }
        binding.moduleBala.addFile.setOnClickListener {
            createNewFile()
        }
    }

    private fun createNewFile() {
        val dialog = AlertDialog.Builder(context).create()
        val addFileBinding = DialogAddFileBinding.inflate(layoutInflater)
        dialog.setView(addFileBinding.root)
        dialog.show()


        addFileBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }


        addFileBinding.btnDone.setOnClickListener {
            val nameOfFolder = addFileBinding.editFile.text.toString()

            // addres + / + name
            val newFile = File(path + File.separator + nameOfFolder)

            if (!newFile.exists()){

                if (newFile.createNewFile()){
                    myAdapter.addNewFile(newFile)
                    binding.recyclerView.scrollToPosition(0)

                    binding.recyclerView.visibility = View.VISIBLE
                    binding.imgNoData.visibility = View.GONE
                }


            }
            dialog.dismiss()

        }



    }

    private fun createNewFolder() {
        val dialog = AlertDialog.Builder(context).create()
        val addFolderBinding = DialogAddFolderBinding.inflate(layoutInflater)
        dialog.setView(addFolderBinding.root)
        dialog.show()


        addFolderBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }


        addFolderBinding.btnDone.setOnClickListener {
            val nameOfFolder = addFolderBinding.editFile.text.toString()

            // addres + / + name
            val newFile = File(path + File.separator + nameOfFolder)

            if (!newFile.exists()) {

                if (newFile.mkdir()) {
                    myAdapter.addNewFile(newFile)
                    binding.recyclerView.scrollToPosition(0)

                    binding.recyclerView.visibility = View.VISIBLE
                    binding.imgNoData.visibility = View.GONE
                }
            }
            dialog.dismiss()


        }


    }

    override fun onFileClicked(file: File, type: String) {

        val intent = Intent(Intent.ACTION_VIEW)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val fileProvider = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().packageName + ".provider",
                file
            )
            intent.setDataAndType(fileProvider, type)
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
        // دسترسی دادن به اکتیویتی مقصد برای خوندن اطلاعات
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)

    }

    override fun onFolderClicked(path: String) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_main, FragmentFile(path))
        transaction.addToBackStack(null)
        transaction.commit()

    }


}