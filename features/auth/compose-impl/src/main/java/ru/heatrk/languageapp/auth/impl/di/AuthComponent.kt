package ru.heatrk.languageapp.auth.impl.di

import androidx.lifecycle.ViewModelProvider
import io.github.jan.supabase.SupabaseClient
import ru.heatrk.languageapp.core.navigation.api.Router
import scout.Component

object AuthComponent : Component(authScope) {
    val loginViewModelFactory: ViewModelProvider.Factory
        get() = get<LoginViewModelFactory>().instance

    val router: Router
        get() = get()

    val supabaseClient: SupabaseClient
        get() = get()
}
