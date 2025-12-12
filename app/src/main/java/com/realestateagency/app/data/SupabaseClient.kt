package com.realestateagency.app.data

import com.realestateagency.app.Config
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest

val supabaseClient = createSupabaseClient(
    supabaseUrl = Config.SUPABASE_URL,
    supabaseKey = Config.SUPABASE_KEY
) {
    install(Postgrest)
}