package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_nav, container, false)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(requireContext(), TelaRF3Activity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_workout -> {
                    val intent = Intent(requireContext(), TelaRF5Activity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_classes -> {
                    val intent = Intent(requireContext(), TelaRF6Activity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_health -> {
                    val intent = Intent(requireContext(), TelaRF7Activity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        return view
    }
}
