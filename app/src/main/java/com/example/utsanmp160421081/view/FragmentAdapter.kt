package com.example.utsanmp160421081

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(val activity: AppCompatActivity, val fragments:ArrayList<Fragment>)
    : FragmentStateAdapter(activity) {
        //mengembalikan jumlah fragment dalam daftar fragments
    override fun getItemCount(): Int {
        return fragments.size
    }
    //Mengembalikan fragment yang sesuai dengan posisi

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}