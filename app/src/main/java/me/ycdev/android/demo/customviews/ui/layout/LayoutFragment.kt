package me.ycdev.android.demo.customviews.ui.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import me.ycdev.android.demo.customviews.R

class LayoutFragment : Fragment() {

    private lateinit var layoutViewModel: LayoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutViewModel = ViewModelProvider(this).get(LayoutViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_layout, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        layoutViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}