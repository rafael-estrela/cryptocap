package br.eti.rafaelcouto.cryptocap.application

import android.app.Application
import br.eti.rafaelcouto.cryptocap.di.Modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        startDependencies()
    }

    private fun startDependencies() {
        startKoin {
            androidContext(this@Application)
            modules(Modules.all)
        }
    }
}
