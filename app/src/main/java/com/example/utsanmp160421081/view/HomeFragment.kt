package com.example.utsanmp160421081.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.utsanmp160421081.databinding.FragmentHomeBinding
import com.example.utsanmp160421081.viewmodel.ListViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel:ListViewModel
    private val homeListAdapter  = HomeAdapter(arrayListOf())
    private lateinit var binding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //membuat binding untuk mengambil element ui
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel dan mengambil refresh()
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        binding.RecyclerView.layoutManager = LinearLayoutManager(context)
        binding.RecyclerView.adapter = homeListAdapter

        observeViewModel()

        //refress swipe bawah
        binding.RefreshLayout.setOnRefreshListener {
            //nampilin indikator lodingnya
            binding.RecyclerView.visibility = View.GONE
            binding.txtError.visibility = View.GONE
            binding.logoLoading.visibility = View.VISIBLE
            viewModel.refresh()
            //nonaftikan setelah selesai
            binding.RefreshLayout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        //untuk live data jika ad kelsahan
        viewModel.newsLD.observe(viewLifecycleOwner, Observer {
            homeListAdapter.updateNewsList(it)
        })
        viewModel.newsLoadErrorLD.observe(viewLifecycleOwner, Observer {
            //jika ada kesalahan tampilin jika tidak tidak tampilin
            if(it == true) {
                binding.txtError?.visibility = View.VISIBLE
            } else {
                binding.txtError?.visibility = View.GONE
            }
        })
        // //untuk live data indikator loading
        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                binding.RecyclerView.visibility = View.GONE
                binding.logoLoading.visibility = View.VISIBLE
            } else {
                binding.RecyclerView.visibility = View.VISIBLE
                binding.logoLoading.visibility = View.GONE
            }
        })

    }


}