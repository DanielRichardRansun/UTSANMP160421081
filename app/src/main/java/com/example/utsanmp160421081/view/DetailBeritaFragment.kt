package com.example.utsanmp160421081.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.example.utsanmp160421081.databinding.FragmentDetailBeritaBinding
import com.example.utsanmp160421081.viewmodel.DetailViewModel

class DetailBeritaFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var binding: FragmentDetailBeritaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //membinding untuk bisa mengambil data ui
        binding = FragmentDetailBeritaBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel dan memanggil getDetail dari viewmodel
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.getDetail()
        observeViewModel()
    }

    private fun observeViewModel() {
        var index = 0

        viewModel.newsLD.observe(viewLifecycleOwner, Observer {
            //mengarah ke viewmodel.newsLD livedata
            var News = it
            //tampil gambar dan detail dari news
            Picasso.get().load(viewModel.newsLD.value?.imageURL).into(binding.imageView2)
            binding.txtAuthorDetil.setText(News.author_name)
            binding.txtJudulDetil.setText(News.title)
            binding.txtIsiContent.setText(News.content[index ])   })


    }
}