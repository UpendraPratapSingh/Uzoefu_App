package com.travel.uzoefuapp.profileFragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.LoginActivity
import com.travel.uzoefuapp.bookingActivities.BookListActivity
import com.travel.uzoefuapp.dashboard.DashboardActivity
import com.travel.uzoefuapp.databinding.FragmentOverviewBinding
import com.travel.uzoefuapp.fragment.WishlistFragment


class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        binding.bookingsCons.setOnClickListener {
            val intent = Intent(requireContext(), BookListActivity::class.java)
            startActivity(intent)
        }

        binding.logoutAccount.setOnClickListener {
            openLogoutCustomPopup()
        }

        /*    binding.wishlistLayout.setOnClickListener {
                openFragment(WishlistFragment())
            }*/

        binding.wishlistLayout.setOnClickListener {
            (activity as? DashboardActivity)?.selectWishlistTab()
        }


        return binding.root
    }

    private fun openLogoutCustomPopup() {
        val dialogView = layoutInflater.inflate(R.layout.logout_popup, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }


    private fun openFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.userFrameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}