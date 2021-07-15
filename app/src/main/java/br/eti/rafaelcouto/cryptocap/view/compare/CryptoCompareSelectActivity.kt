package br.eti.rafaelcouto.cryptocap.view.compare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import br.eti.rafaelcouto.cryptocap.R

class CryptoCompareSelectActivity : AppCompatActivity() {

    private lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crypto_compare_select)

        setupLayout()
        setupEntryArgument()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun setupLayout() {
        title = getString(R.string.compare_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupEntryArgument() {
        navHost = supportFragmentManager.findFragmentById(R.id.fcvContent) as NavHostFragment

        val inflater = navHost.navController.navInflater
        val graph = inflater.inflate(R.navigation.main_graph)
        val arguments = bundleOf("isComparing" to true)

        navHost.navController.setGraph(graph, arguments)
    }
}
