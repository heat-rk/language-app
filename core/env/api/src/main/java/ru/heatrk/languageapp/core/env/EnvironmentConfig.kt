package ru.heatrk.languageapp.core.env

interface EnvironmentConfig {
    val applicationId: String
    val supabaseAnonKey: String
    val supabaseUrl: String
    val supabaseStorageUrl: String
    val googleServerClientId: String
    val supabaseRedirectScheme: String
    val supabaseRedirectHost: String
}
