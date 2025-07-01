package com.baag.treksahayatri.ui.traveller.notification

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baag.treksahayatri.R

class NotificationFragment : Fragment() {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_notification_traveller, container, false)
    }
}