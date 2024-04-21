package com.senac.mintwallet.welcome

import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.senac.mintwallet.R
import com.senac.mintwallet.databinding.FragmentWelcomeMainBinding


class Main: Fragment()  {

    private var _binding: FragmentWelcomeMainBinding? = null
    private val binding get() = _binding!!

    private var viewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var dots: Array<TextView?>


    private val pagerListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            setDotIndicator(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        requireActivity().window.statusBarColor = resources.getColor(R.color.green);
        requireActivity().window.navigationBarColor = resources.getColor(if(isNightMode) R.color.black else R.color.white);

        binding.nextButton.setOnClickListener {
            if (getItem(0) < 2) {
                binding.slideViewPager.setCurrentItem(getItem(1), true)
            } else {
                //Todo
            }
        }
        binding.skipButton.setOnClickListener {
            //Todo
        }

        viewPagerAdapter = this.context?.let { ViewPagerAdapter(it) }
        binding.slideViewPager.setAdapter(viewPagerAdapter)
        setDotIndicator(0)
        binding.slideViewPager.addOnPageChangeListener(pagerListener);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var viewPagerListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            setDotIndicator(position)
            if (position == 1) {
                binding.nextButton.setText("Entrar")
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    fun setDotIndicator(position: Int) {
        dots = arrayOfNulls<TextView>(3);
        binding.dotIndicator.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(context)
            dots[i]?.text  = Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY)
            dots[i]?.textSize  = 35F
            dots[i]?.setTextColor(
                resources.getColor(R.color.active)
            )
            binding.dotIndicator.addView(dots[i])
        }
        dots[position]?.setTextColor(
            resources.getColor(R.color.inactive)
        )
    }

    private fun getItem(i: Int): Int {
        return binding.slideViewPager.currentItem + i
    }
 }