package com.example.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_nav, container, false)
        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(requireContext(), "Início", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_workout -> {
                    Toast.makeText(requireContext(), "Treino", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_classes -> {
                    Toast.makeText(requireContext(), "Aulas", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_health -> {
                    Toast.makeText(requireContext(), "Saúde", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        return view
    }
}



