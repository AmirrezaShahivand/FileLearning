package Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.file.R
import com.example.file.databinding.ItemFileBinding
import java.io.File
import java.net.URLConnection

class FileAdapter(val data: ArrayList<File> , val fileEvent: FileEvent) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
    lateinit var binding: ItemFileBinding

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(file: File) {

            var fileType = ""
            binding.txtNameFile.text = file.name
            //       binding.imageView.setImageResource(R.drawable.ic_folder)

            if (file.isDirectory) {
                binding.imageView.setImageResource(R.drawable.ic_folder)
            } else {
                //         binding.imageView.setImageResource(R.drawable.ic_file)
                when {
                    isImage(file.path) -> {
                        binding.imageView.setImageResource(R.drawable.ic_image)
                        fileType = "image/*"
                    }

                    isVideo(file.path) -> {
                        binding.imageView.setImageResource(R.drawable.ic_video)
                        fileType = "video/*"
                    }

                    isZip(file.name) -> {
                        binding.imageView.setImageResource(R.drawable.ic_zip)
                        fileType = "application/zip"
                    }else -> {
                        binding.imageView.setImageResource(R.drawable.ic_file)
                    fileType = "text/plain"
                    }
                }


            }

            itemView.setOnClickListener {
                if (file.isDirectory){
                    fileEvent.onFolderClicked(file.path)
                }else{
                    fileEvent.onFileClicked(file ,fileType )
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemFileBinding.inflate(layoutInflater)
        return FileViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {

        holder.bindViews(data[position])
    }

    private fun isImage(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("image")
    }

    private fun isVideo(path: String): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("video")
    }

    private fun isZip(name: String): Boolean {
        return name.contains(".zip") || name.contains(".rar")
    }
    interface FileEvent{
        fun onFileClicked(file: File , type : String)
        fun onFolderClicked(path: String)
    }

}