package br.eti.rafaelcouto.cryptocap.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.eti.rafaelcouto.cryptocap.R
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.databinding.FragmentCryptoDetailsBinding
import br.eti.rafaelcouto.cryptocap.viewmodel.CryptoDetailsViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class CryptoDetailsFragment : Fragment() {

    private val detailsViewModel: CryptoDetailsViewModel by viewModel()
    private lateinit var binding: FragmentCryptoDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoDetailsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = detailsViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        setupObservers()
        loadData()
    }

    private fun setupLayout() {
        binding.rgVariation.setOnCheckedChangeListener { _, checkedId ->
            detailsViewModel.updateSelection(checkedId)
        }
    }

    private fun setupObservers() {
        detailsViewModel.status.observe(viewLifecycleOwner) {
            binding.pbLoading.isVisible = it == Result.Status.LOADING
            binding.tvError.isVisible = it == Result.Status.ERROR
            binding.clContent.isVisible = it == Result.Status.SUCCESS
        }

        detailsViewModel.content.observe(viewLifecycleOwner) {
            it?.let { data ->
                val url = data.logoUrl

                Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
                    .into(binding.ivLogo)
            }
        }
    }

    private fun loadData() {
        val args by navArgs<CryptoDetailsFragmentArgs>()

        detailsViewModel.loadData(args.id)
    }
}
