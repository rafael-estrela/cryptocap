package br.eti.rafaelcouto.cryptocap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import br.eti.rafaelcouto.cryptocap.R
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.databinding.FragmentCryptoCompareBinding
import br.eti.rafaelcouto.cryptocap.domain.model.CryptoCompareUI
import br.eti.rafaelcouto.cryptocap.viewmodel.CryptoCompareViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class CryptoCompareFragment : Fragment() {

    private lateinit var binding: FragmentCryptoCompareBinding

    private val compareViewModel: CryptoCompareViewModel by viewModel()
    private val args by navArgs<CryptoCompareFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCryptoCompareBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = compareViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        setupObservers()

        compareViewModel.loadData(args.fromId, args.toId)
    }

    private fun setupLayout() {
        binding.btnSwap.setOnClickListener { compareViewModel.swap() }
    }

    private fun setupObservers() {
        compareViewModel.status.observe(viewLifecycleOwner) {
            binding.pbLoading.isVisible = it == Result.Status.LOADING
            binding.tvError.isVisible = it == Result.Status.ERROR

            val isSuccess = it == Result.Status.SUCCESS

            binding.clFrom.isVisible = isSuccess
            binding.clTo.isVisible = isSuccess
            binding.btnSwap.isVisible = isSuccess
            binding.tvEqual.isVisible = isSuccess
        }

        compareViewModel.from.observe(viewLifecycleOwner, updateLogoObserver(binding.ivLogoFrom))
        compareViewModel.to.observe(viewLifecycleOwner, updateLogoObserver(binding.ivLogoTo))
    }

    private fun updateLogoObserver(component: ImageView) = Observer<CryptoCompareUI.Element?> {
        it?.let { data ->
            Glide.with(this)
                .load(data.logoUrl)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_error)
                .into(component)
        }
    }
}
