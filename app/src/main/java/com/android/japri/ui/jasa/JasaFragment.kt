package com.android.japri.ui.jasa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.japri.adapter.JasaAdapter
import com.android.japri.data.ResultState
import com.android.japri.data.request.JasaRequestBody
import com.android.japri.databinding.FragmentJasaBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.showLoading

class JasaFragment : Fragment() {

    private var _binding: FragmentJasaBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<JasaViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: JasaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJasaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = JasaAdapter()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvJasa.layoutManager = layoutManager
        binding.rvJasa.adapter = adapter

        getJasa()

//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                adapter.submitList(null)
//                searchJasa(JasaRequestBody(query.toString()))
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                adapter.submitList(null)
//                searchJasa(JasaRequestBody(newText.toString()))
//                return true
//            }
//        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText.toString())
                return true
            }
        })

    }

    private fun search(query: String?){
        if (query.toString().isBlank()){
            getJasa()
            binding.noDataFound.visibility = View.INVISIBLE
        } else {
//            adapter.submitList(null)
            searchJasa(JasaRequestBody(query.toString()))
        }
    }

    private fun getJasa(){
        viewModel.getJasa().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressBar.showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.progressBar.showLoading(false)
                        if(result.data.isEmpty()){
                            binding.noDataFound.visibility = View.VISIBLE
                        } else {
                            adapter.submitList(null)
                            binding.noDataFound.visibility = View.INVISIBLE
                            adapter.submitList(result.data)
                        }
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        binding.progressBar.showLoading(false)
                    }
                }
            }
        }
    }

    private fun searchJasa(requestBody: JasaRequestBody){
        viewModel.searchJasa(requestBody).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressBar.showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.progressBar.showLoading(false)
                        if(result.data.isEmpty()){
                            binding.noDataFound.visibility = View.VISIBLE
                        } else {
                            adapter.submitList(null)
                            binding.noDataFound.visibility = View.INVISIBLE
                            adapter.submitList(result.data)
                        }
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        binding.progressBar.showLoading(false)
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}