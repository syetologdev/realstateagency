package com.realestateagency.app.data

import android.util.Log
import com.realestateagency.app.models.Agent
import com.realestateagency.app.models.PropertyListing
import com.realestateagency.app.models.ShowingRequest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PropertyRepository {

    private val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://qyeyrqbdaykclxbqrkoo.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InF5ZXlycWJkYXlrY2x4YnFya29vIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjUzNjY1NzEsImV4cCI6MjA4MDk0MjU3MX0.Csw874oKpWjmrdmSZ_2SmLK2kBwgqnGc4-wBRsvKI6c"
    ) {
        install(Postgrest)
        install(Realtime)
    }


    // Получить все объекты недвижимости
    fun getAllProperties(): Flow<List<PropertyListing>> = flow {
        try {
            val properties = supabase.from("properties").select().decodeList<PropertyListing>()
            emit(properties)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching all properties: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Получить объект по ID
    fun getPropertyById(id: Int): Flow<PropertyListing?> = flow {
        try {
            val property = supabase.from("properties").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<PropertyListing>()
            emit(property)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching property by ID: ${e.message}")
            e.printStackTrace()
            emit(null)
        }
    }

    // Получить объекты по городу
    fun getPropertiesByCity(city: String): Flow<List<PropertyListing>> = flow {
        try {
            val properties = supabase.from("properties").select {
                filter {
                    eq("city", city)
                }
            }.decodeList<PropertyListing>()
            emit(properties)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching properties by city: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Получить объекты по типу
    fun getPropertiesByType(type: String): Flow<List<PropertyListing>> = flow {
        try {
            val properties = supabase.from("properties").select {
                filter {
                    eq("type", type)
                }
            }.decodeList<PropertyListing>()
            emit(properties)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching properties by type: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Получить объекты по диапазону цены
    fun getPropertiesByPriceRange(minPrice: Double, maxPrice: Double): Flow<List<PropertyListing>> = flow {
        try {
            val properties = supabase.from("properties").select {
                filter {
                    gte("price", minPrice)
                    lte("price", maxPrice)
                }
            }.decodeList<PropertyListing>()
            emit(properties)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching properties by price: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Поиск объектов по названию/адресу
    fun searchProperties(query: String): Flow<List<PropertyListing>> = flow {
        try {
            val properties = supabase.from("properties").select().decodeList<PropertyListing>()
            val filtered = properties.filter { property ->
                property.title.contains(query, ignoreCase = true) ||
                        property.address.contains(query, ignoreCase = true) ||
                        property.city.contains(query, ignoreCase = true)
            }
            emit(filtered)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error searching properties: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Получить популярные объекты (по просмотрам)
    fun getPopularProperties(limit: Int = 10): Flow<List<PropertyListing>> = flow {
        try {
            val properties = supabase.from("properties")
                .select()
                .decodeList<PropertyListing>()
                .sortedByDescending { it.viewsCount }
                .take(limit)
            emit(properties)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching popular properties: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Получить агента по ID
    fun getAgentById(id: Int): Flow<Agent?> = flow {
        try {
            val agent = supabase.from("agents").select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<Agent>()
            emit(agent)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching agent by ID: ${e.message}")
            e.printStackTrace()
            emit(null)
        }
    }

    // Получить всех агентов
    fun getAllAgents(): Flow<List<Agent>> = flow {
        try {
            val agents = supabase.from("agents").select().decodeList<Agent>()
            emit(agents)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching all agents: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Отправить запрос показа
    fun submitShowingRequest(request: ShowingRequest): Flow<Boolean> = flow {
        try {
            supabase.from("showing_requests").insert(request)
            emit(true)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error submitting showing request: ${e.message}")
            e.printStackTrace()
            emit(false)
        }
    }

    // Получить все запросы показа агента
    fun getShowingRequestsByAgent(agentId: Int): Flow<List<ShowingRequest>> = flow {
        try {
            val requests = supabase.from("showing_requests").select {
                filter {
                    eq("agent_id", agentId)
                }
            }.decodeList<ShowingRequest>()
            emit(requests)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching showing requests: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Получить все запросы показа по свойству
    fun getShowingRequestsByProperty(propertyId: Int): Flow<List<ShowingRequest>> = flow {
        try {
            val requests = supabase.from("showing_requests").select {
                filter {
                    eq("property_id", propertyId)
                }
            }.decodeList<ShowingRequest>()
            emit(requests)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error fetching showing requests by property: ${e.message}")
            e.printStackTrace()
            emit(emptyList())
        }
    }

    // Увеличить счётчик просмотров
    fun incrementViewCount(propertyId: Int): Flow<Boolean> = flow {
        try {
            val property = supabase.from("properties").select {
                filter {
                    eq("id", propertyId)
                }
            }.decodeSingle<PropertyListing>()

            supabase.from("properties").update(
                mapOf("views_count" to (property.viewsCount + 1))
            ) {
                filter { eq("id", propertyId) }
            }
            emit(true)
        } catch (e: Exception) {
            Log.e("PropertyRepo", "Error incrementing view count: ${e.message}")
            e.printStackTrace()
            emit(false)
        }
    }
}