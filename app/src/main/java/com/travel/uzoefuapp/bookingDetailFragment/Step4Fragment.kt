package com.travel.uzoefuapp.bookingDetailFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.uzoefuapp.databinding.FragmentStep4Binding


class Step4Fragment : Fragment() {
    private var _binding: FragmentStep4Binding? = null
    private val binding get() = _binding!!
    private var price: String? = null
    private var childrenPrice: String? = null
    private var activityId: String? = null
    private var address: String? = null
    private var town: String? = null
    private var productName: String? = null
    companion object {
        fun newInstance(
            price: String,
            childrenPrice: String,
            activityId: String,
            address: String,
            town: String,
            productName: String
        ): Step1Fragment {
            val fragment = Step1Fragment()
            val args = Bundle()
            args.putString("price", price)
            args.putString("childrenPrice", childrenPrice)
            args.putString("activityId", activityId)
            args.putString("address", address)
            args.putString("town", town)
            args.putString("productName", productName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStep4Binding.inflate(inflater, container, false)
        arguments?.let {
            price = it.getString("price")
            childrenPrice = it.getString("childrenPrice")
            activityId = it.getString("activityId")
            address = it.getString("address")
            town = it.getString("town")
            productName = it.getString("productName")
        }
        binding.activityName.text = productName


        return binding.root
    }
}