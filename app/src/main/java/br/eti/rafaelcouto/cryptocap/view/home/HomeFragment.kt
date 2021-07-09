package br.eti.rafaelcouto.cryptocap.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.databinding.FragmentHomeBinding
import br.eti.rafaelcouto.cryptocap.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val cryptoAdapter = CryptoItemsAdapter()
    private var listener: CryptoItemsAdapter.OnItemClickListener? = null

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupObservers()

        homeViewModel.loadData()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun setupRecycler() {
        listener = object : CryptoItemsAdapter.OnItemClickListener {
            override fun onItemClick(id: Long) {
                Toast.makeText(requireContext(), "Selected $id", Toast.LENGTH_SHORT).show()
            }
        }

        cryptoAdapter.onItemClickListener = listener

        binding.rvCryptoList.run {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = cryptoAdapter
        }
    }

    private fun setupObservers() = homeViewModel.run {
        status.observe(viewLifecycleOwner) { handleStatus(it) }

        data.observe(viewLifecycleOwner) {
            cryptoAdapter.setItems(it)
        }
    }

    private fun handleStatus(status: Result.Status) = binding.run {
        rvCryptoList.isVisible = status == Result.Status.SUCCESS
        tvError.isVisible = status == Result.Status.ERROR
        pbLoading.isVisible = status == Result.Status.LOADING
    }
}
