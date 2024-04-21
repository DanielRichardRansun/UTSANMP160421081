package com.example.utsanmp160421081.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.example.utsanmp160421081.databinding.ItemHomeBinding
import com.example.utsanmp160421081.model.News


class HomeAdapter(val newsList:ArrayList<News>)  : RecyclerView.Adapter<HomeAdapter.NewsViewHolder>() {

    //class ViewHolder untuk setiap item berita
    class NewsViewHolder(var binding: ItemHomeBinding)
        :RecyclerView.ViewHolder(binding.root)

    //Membuat ViewHolder baru
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    //menjumlah total item berita
    override fun getItemCount(): Int {
        return newsList.size
    }
    // Metode yang dipanggil untuk mengikat data berita ke ViewHolder pada posisi tertentu
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.txtJudulBerita.text = newsList[position].title
        holder.binding.txtAuthor.text = newsList[position].author_name
        holder.binding.txtDeskripsi.text = newsList[position].description

        holder.binding.btnRead.setOnClickListener {
            val action = HomeFragmentDirections.actionDetailBeritaFragment(newsList[position].id.toString())
            Navigation.findNavController(it).navigate(action)
        }

        //menampilkan gambar dari url
        val picasso = Picasso.Builder(holder.itemView.context)
        picasso.listener { picasso, uri, exception ->
            exception.printStackTrace()
        }
        picasso.build().load(newsList[position].imageURL)
            .into(holder.binding.imageView, object: Callback {
                override fun onSuccess() {
                    holder.binding.logoLoadingFoto.visibility = View.INVISIBLE
                    holder.binding.imageView.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {
                    Log.e("picasso_error", e.toString())
                }
            })
    }
    // Metode untuk memperbarui daftar berita yang ditampilkan dalam RecyclerView
    fun updateNewsList(newNewsList: ArrayList<News>) {
        newsList.clear()
        newsList.addAll(newNewsList)
        notifyDataSetChanged()
    }
}