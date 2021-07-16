package br.eti.rafaelcouto.cryptocap.view.details

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.eti.rafaelcouto.cryptocap.R
import br.eti.rafaelcouto.cryptocap.application.network.model.Result
import br.eti.rafaelcouto.cryptocap.databinding.FragmentCryptoDetailsBinding
import br.eti.rafaelcouto.cryptocap.viewmodel.CryptoDetailsViewModel
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel

class CryptoDetailsFragment : Fragment() {

    companion object {
        const val RESULT_KEY = "compare_select_results"
        const val SELECTED_ID_KEY = "compare_selected_id"
    }

    private val detailsViewModel: CryptoDetailsViewModel by viewModel()
    private val args by navArgs<CryptoDetailsFragmentArgs>()

    private lateinit var navController: NavController
    private lateinit var binding: FragmentCryptoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBehavior()
    }

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

        navController = findNavController()
        detailsViewModel.loadData(args.id)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()

        inflater.inflate(R.menu.details_menu, menu)

        menu.findItem(R.id.item_favorite)?.setIcon(
            if (detailsViewModel.isFavorite.value == true)
                R.drawable.ic_favorite_active
            else
                R.drawable.ic_favorite
        )
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.item_compare -> {
            val directions = CryptoDetailsFragmentDirections.fragmentDetailsToFragmentCompareSelect(true)
            navController.navigate(directions)

            true
        }
        R.id.item_favorite -> {
            detailsViewModel.handleFavorite()

            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun setupBehavior() {
        setHasOptionsMenu(true)

        setFragmentResultListener(RESULT_KEY) { _, data ->
            val fromId = args.id
            val toId = data.getLong(SELECTED_ID_KEY)

            val directions = CryptoDetailsFragmentDirections.fragmentDetailsToFragmentCompare(fromId, toId)
            navController.navigate(directions)
        }
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

        detailsViewModel.isFavorite.observe(viewLifecycleOwner) {
            requireActivity().invalidateOptionsMenu()
        }
    }
}
