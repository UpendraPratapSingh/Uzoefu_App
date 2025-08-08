package com.travel.uzoefuapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.CreateAccountActivity

class OnboardingAdapter(
    private val context: Context,
    private val items: List<OnboardingItem>,
    private val viewPager: ViewPager
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.onboarding_item, container, false)

        val videoView = view.findViewById<VideoView>(R.id.videoView)
        val titleText = view.findViewById<TextView>(R.id.titleText)
        val descriptionText = view.findViewById<TextView>(R.id.descriptionText)
        val skipText = view.findViewById<TextView>(R.id.skipText)

        val item = items[position]

        val uri = Uri.parse("android.resource://${context.packageName}/${item.videoResId}")
        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            mp.setVolume(0f, 0f)
            videoView.start()
        }

        titleText.text = item.title

        val desc = item.description
        val spannable = SpannableString(desc)
        val firstSpace = desc.indexOf(" ")
        if (firstSpace != -1) {
            spannable.setSpan(StyleSpan(Typeface.BOLD),0, firstSpace, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        descriptionText.text = spannable

        skipText.setOnClickListener {
            if (position < items.size - 1) {
                viewPager.currentItem = position + 1
            } else {
                context.startActivity(Intent(context, CreateAccountActivity::class.java))
                if (context is Activity) {
                    (context as Activity).finish()
                }
            }
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = items.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`
}
