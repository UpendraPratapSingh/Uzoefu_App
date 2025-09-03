package com.travel.uzoefuapp.amenityFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.AmenitiesAdapter


class AmenitiesPageFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_amenities_page, container, false)

        recyclerView = view.findViewById(R.id.recyclerAmenities)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val amenities = arguments?.getStringArrayList(ARG_AMENITIES) ?: arrayListOf()
        recyclerView.adapter = AmenitiesAdapter(amenities)

        return view
    }

    companion object {
        private const val ARG_AMENITIES = "amenities"

        fun newInstance(amenities: List<String>): AmenitiesPageFragment {
            val fragment = AmenitiesPageFragment()
            val bundle = Bundle()
            bundle.putStringArrayList(ARG_AMENITIES, ArrayList(amenities))
            fragment.arguments = bundle
            return fragment
        }
    }
}