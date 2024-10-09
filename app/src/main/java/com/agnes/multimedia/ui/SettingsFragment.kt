package com.agnes.multimedia.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.agnes.multimedia.R
import com.agnes.multimedia.databinding.FragmentSettingsBinding
import com.agnes.multimedia.utils.Constants

class SettingsFragment : Fragment() {
    var fsbbinding : FragmentSettingsBinding? = null
    val binding get() = fsbbinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        fsbbinding = FragmentSettingsBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onResume() {
        super.onResume()
        binding.btnLogout.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes"){dialog, which ->
                    logoutUser()
                    dialog.dismiss()
                }
                .setNegativeButton("No"){dialog, which ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }
    private fun logoutUser(){
        val prefs = requireContext().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
//        edit.clear()
        editor.remove(Constants.FIRST_NAME)
        editor.remove(Constants.LAST_NAME)
        editor.remove(Constants.ACCESS_TOKEN)
        editor.apply()

        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fsbbinding = null
    }

}