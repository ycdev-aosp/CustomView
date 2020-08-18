package me.ycdev.android.demo.customviews.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import me.ycdev.android.demo.customviews.R
import me.ycdev.android.demo.customviews.databinding.FragmentEventsBinding

class EventsFragment : Fragment() {
    lateinit var binding: FragmentEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CallRecordsCollector.callRecords.clear() // clear the records
        binding = FragmentEventsBinding.inflate(inflater)
        binding.oneInterceptDown.setOnCheckedChangeListener { _, checked ->
            binding.showcaseOne.interceptDown = checked
        }
        binding.oneInterceptOthers.setOnCheckedChangeListener { _, checked ->
            binding.showcaseOne.interceptOthers = checked
        }
        binding.oneHandled.setOnCheckedChangeListener { _, checked ->
            binding.showcaseOne.handled = checked
        }
        binding.oneDisallowIntercept.setOnCheckedChangeListener { _, checked ->
            binding.oneBtnHasListener.disallowIntercept = checked
            binding.oneBtnNoListener.disallowIntercept = checked
        }
        binding.oneBtnHasListener.setOnClickListener {
            Snackbar.make(binding.oneBtnHasListener, "Clicked", Snackbar.LENGTH_SHORT).show()
        }
        binding.oneBtnDisable.setOnClickListener {
            val enabled = !binding.oneBtnHasListener.isEnabled
            binding.oneBtnHasListener.isEnabled = enabled
            binding.oneBtnNoListener.isEnabled = enabled
            binding.oneBtnDisable.setText(if (enabled) R.string.events_btn_disable else R.string.events_btn_enable)
        }
        binding.oneBtnDisallowIntercept.setOnClickListener {
            binding.showcaseOne.requestDisallowInterceptTouchEvent(true)
        }
        return binding.root
    }
}