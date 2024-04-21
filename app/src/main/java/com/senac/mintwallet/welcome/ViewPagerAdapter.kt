package com.senac.mintwallet.welcome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.senac.mintwallet.R


class ViewPagerAdapter(private var context: Context) :PagerAdapter() {

    var sliderAllImages = intArrayOf(
        R.drawable.step1,
        R.drawable.step2,
        R.drawable.step3,
    )
    var sliderAllTitle = intArrayOf(
        R.string.welcome_step1_title,
        R.string.welcome_step2_title,
        R.string.welcome_step3_title,
    )
    var sliderAllDesc = intArrayOf(
        R.string.welcome_step1_decription,
        R.string.welcome_step2_decription,
        R.string.welcome_step3_decription,
    )

    override fun getCount(): Int {
        return sliderAllTitle.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.fragment_welcome_component, container, false)

        val sliderImage = view.findViewById<View>(R.id.component_image) as ImageView
        val sliderTitle = view.findViewById<View>(R.id.component_title) as TextView
        val sliderDesc = view.findViewById<View>(R.id.component_description) as TextView

        sliderImage.setImageResource(sliderAllImages[position])
        sliderTitle.setText(sliderAllTitle[position])
        sliderDesc.setText(sliderAllDesc[position])

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}
