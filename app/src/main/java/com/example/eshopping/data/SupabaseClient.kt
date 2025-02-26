package com.example.eshopping.data

import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.websocket.WebSockets

object SupabaseClient{
    @OptIn(SupabaseInternal::class)
    val supabaseClient = createSupabaseClient(
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRscWl0a2R6ZmtxaHFsa3ZlYmNpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzU1NTAzNTgsImV4cCI6MjA1MTEyNjM1OH0.u3ruYbyNEK7HqTc3_nmhhakaEGjoLuLucbhmjQzp4dw",
        supabaseUrl = "https://tlqitkdzfkqhqlkvebci.supabase.co"
    ){
        install(Realtime)
        install(Postgrest)
        install(Storage)
        httpConfig {
            this.install(WebSockets)
        }
    }
}



