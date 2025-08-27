package com.travel.uzoefuapp.productFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.FAQ
import com.travel.uzoefuapp.adapter.FAQAdapter
import com.travel.uzoefuapp.bookingActivities.BookingDetailStep1Activity
import com.travel.uzoefuapp.databinding.FragmentFAQBinding


class FAQFragment : Fragment() {
    private var _binding: FragmentFAQBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFAQBinding.inflate(inflater, container, false)



        val faqList = listOf(
            FAQ("What is the cancellation policy?", "You can cancel 24 hours before."),
            FAQ("Are there any medical conditions?", "Yes, heart patients should avoid."),
            FAQ("What happens in bad weather?", "The trip will be rescheduled."),
            FAQ("What equipment is provided, and what should i bring?", "No person under 60 Kg will be allowed to participate. ( we will once operational for a month change the weight restrictions in Mid October 2024)\n" +
                    "No PREGNANT Ladies/Woman.\n" +
                    "No heart conditions or physical disability.\n" +
                    "No person over the 120kg weight limit.\n" +
                    "Weather conditions are unpredictable. (please bring your jacket and sunblock)\n" +
                    "Strong winds and sometimes rain, affects the optimal operation at the zipline.\n" +
                    "In these circumstances, the lead guide may decide to impose some weight restrictions or temporarily close the line.\n" +
                    "If you are so affected, we will attempt to reschedule your zip for later the same day or the next day, and if this is not suitable, we will issue you with a voucher valid for one year (excluding peak periods).\n" +
                    "These decisions are made for the safety and well-being of our clients.\n" +
                    "If for any reason you want to cancel the trip once it starts we unfortunately still have to charge and no refund\n" +
                    "We have a no-refund policy. We will provide you with a voucher (valid for one year, excluding peak periods) if you are unable to complete the activity due to inclement weather.\n" +
                    " \n" +
                    "We require full payment prior to arrival - our booking system will generate an invoice for you to settle with in a few minutes of your booking. No Payment no Booking. Please arrive 15 minutes before your booking. If your late you unfortunately lose your time slot and no refunds. We will only cancel a booking in the event of safety concerns and will refund 100% - we do operate in all weather conditions! so weather is not a factor for cancellations unless we have thunder.. If your responsible for booking a group please ensure that all participants are aware of our Terms and Conditions as well all aspects of the adventure..")
        )

        val adapter = FAQAdapter(faqList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        return binding.root
    }
}