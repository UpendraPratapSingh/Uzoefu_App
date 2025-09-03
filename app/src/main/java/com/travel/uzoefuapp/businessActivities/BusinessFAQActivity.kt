package com.travel.uzoefuapp.businessActivities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.BusinessFAQ
import com.travel.uzoefuapp.adapter.BusinessFaqAdapter
import com.travel.uzoefuapp.adapter.FAQ
import com.travel.uzoefuapp.databinding.ActivityBusinessFaqactivityBinding
import java.util.*

class BusinessFAQActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusinessFaqactivityBinding
    private lateinit var faqAdapter: BusinessFaqAdapter
    private val faqs = mutableListOf(
        BusinessFAQ("What is the cancellation policy?", "Full refund if cancelled 24 hours before."),
        BusinessFAQ("What happens in case of bad weather?", "We reschedule or provide refund."),
        BusinessFAQ("Are there any medical conditions that would prevent me from participating?", "Yes, heart conditions and pregnancy are restricted."),
        BusinessFAQ("What equipment is provided, and what should I bring?", "We provide safety gear. Bring comfortable shoes."),
        BusinessFAQ("Can I use a card payment?", "Yes, all major cards are accepted."),
        BusinessFAQ("Are children allowed?", "Children above 5 years are allowed with an adult.")
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBusinessFaqactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backArrow.setOnClickListener { finish() }

        // Setup RecyclerView
        faqAdapter = BusinessFaqAdapter(faqs) { position ->
            showEditFaqBottomSheet(position)

        }

        binding.faqRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@BusinessFAQActivity)
            adapter = faqAdapter
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                Collections.swap(faqs, fromPos, toPos)
                faqAdapter.notifyItemMoved(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        })
        itemTouchHelper.attachToRecyclerView(binding.faqRecyclerView)

        // Add FAQ button

        binding.tvAddFaq.setOnClickListener {
            showAddFaqBottomSheet()
        }
    }
    private fun showAddFaqBottomSheet() {
        openFaqBottomSheet(null)
    }

    private fun showEditFaqBottomSheet(position: Int) {
        openFaqBottomSheet(position)
    }

    private fun openFaqBottomSheet(position: Int?) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_add_faq, null)
        bottomSheetDialog.setContentView(view)

        val etQuestion = view.findViewById<EditText>(R.id.etQuestion)
        val etAnswer = view.findViewById<EditText>(R.id.etAnswer)
        val btnSave = view.findViewById<Button>(R.id.btnSaveFaq)
        val ivDelete = view.findViewById<ImageView>(R.id.ivDelete)

        if (position != null) {
            // Editing existing FAQ
            etQuestion.setText(faqs[position].question)
            etAnswer.setText(faqs[position].answer)
            btnSave.text = "Update"
        }

        btnSave.setOnClickListener {
            val question = etQuestion.text.toString().trim()
            val answer = etAnswer.text.toString().trim()

            if (question.isEmpty() || answer.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (position == null) {
                // Add new
                faqs.add(BusinessFAQ(question, answer))
                faqAdapter.notifyItemInserted(faqs.size - 1)
            } else {
                // Update existing
                faqs[position].question = question
                faqs[position].answer = answer
                faqAdapter.notifyItemChanged(position)
            }

            bottomSheetDialog.dismiss()
        }

        ivDelete.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }


}
