package com.android.japri.ui.jasa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.japri.databinding.FragmentJasaBinding

class JasaFragment : Fragment() {

    private var _binding: FragmentJasaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[JasaViewModel::class.java]

        _binding = FragmentJasaBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textJasa
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the submission of the search query if needed
                binding.textJasa.text = query
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle the text change in the search view
//                textView.text = newText
                binding.textJasa.text = newText
                return true
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}