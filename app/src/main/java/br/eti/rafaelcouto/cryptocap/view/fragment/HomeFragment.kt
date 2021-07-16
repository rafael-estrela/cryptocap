package br.eti.rafaelcouto.cryptocap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import br.eti.rafaelcouto.cryptocap.databinding.FragmentHomeBinding
import br.eti.rafaelcouto.cryptocap.router.abs.HomeRouterAbs
import br.eti.rafaelcouto.cryptocap.view.adapter.CryptoItemsAdapter
import br.eti.rafaelcouto.cryptocap.view.adapter.CryptoItemsLoadStateAdapter
import br.eti.rafaelcouto.cryptocap.view.home.HomeFragmentArgs
import br.eti.rafaelcouto.cryptocap.viewmodel.HomeViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val router: HomeRouterAbs by inject { parametersOf(findNavController()) }

    private val cryptoAdapter = CryptoItemsAdapter()
    private val headerAdapter = CryptoItemsLoadStateAdapter { cryptoAdapter.retry() }
    private val footerAdapter = CryptoItemsLoadStateAdapter { cryptoAdapter.retry() }
    private var listener: CryptoItemsAdapter.OnItemClickListener? = null

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

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
            override fun onItemClick(id: Long) = handleOnClickEvent(id)
        }

        cryptoAdapter.onItemClickListener = listener
        cryptoAdapter.addLoadStateListener { loadStates ->
            headerAdapter.loadState = loadStates.refresh
            footerAdapter.loadState = loadStates.append
        }

        binding.rvCryptoList.run {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = ConcatAdapter(headerAdapter, cryptoAdapter, footerAdapter)
            setHasFixedSize(true)
        }

        cryptoAdapter.submitData(lifecycle, PagingData.empty())
    }

    private fun setupObservers() {
        homeViewModel.data.observe(viewLifecycleOwner) {
            cryptoAdapter.submitData(lifecycle, it)
        }
    }

    private fun handleOnClickEvent(id: Long) {
        val args by navArgs<HomeFragmentArgs>()

        if (args.isComparing) {
            val result = bundleOf(CryptoDetailsFragment.SELECTED_ID_KEY to id)
            setFragmentResult(CryptoDetailsFragment.RESULT_KEY, result)

            router.goBack()
        } else {
            router.goToDetails(id)
        }
    }
}
