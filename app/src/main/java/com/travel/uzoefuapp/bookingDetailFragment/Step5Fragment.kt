package com.travel.uzoefuapp.bookingDetailFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.uzoefuapp.databinding.FragmentStep5Binding


class Step5Fragment : Fragment() {
    private var _binding: FragmentStep5Binding? = null
    private val binding get() = _binding!!
    private var activityId: String? = null
    private var productName: String? = null

    companion object {
        fun newInstance(
            activityId: String,
            productName: String
        ): Step5Fragment {
            val fragment = Step5Fragment()
            val args = Bundle()
            args.putString("activityId", activityId)
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
        _binding = FragmentStep5Binding.inflate(inflater, container, false)
        arguments?.let {
            activityId = it.getString("activityId")
            productName = it.getString("productName")
        }
        binding.activityName.text = productName

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("productName", productName)
        outState.putString("activityId", activityId)
    }
}