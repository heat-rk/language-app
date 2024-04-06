package ru.heatrk.languageapp.core.data.supabase.di

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import ru.heatrk.languageapp.core.data.supabase.BuildConfig
import scout.definition.Registry

fun Registry.useSupaBaseBeans() {
    singleton<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY,
        ) {
            defaultSerializer = KotlinXSerializer(get())

            install(Auth) {
                flowType = FlowType.PKCE
                scheme = BuildConfig.SUPABASE_REDIRECT_SCHEME
                host = BuildConfig.SUPABASE_REDIRECT_HOST
            }

            install(Postgrest)
            install(Storage)
        }
    }
}
