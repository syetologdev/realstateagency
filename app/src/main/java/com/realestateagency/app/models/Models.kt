package com.realestateagency.app.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PropertyListing(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("price")
    val price: Double,
    @SerialName("area")
    val area: Double,
    @SerialName("rooms")
    val rooms: Int,
    @SerialName("type")
    val type: String, // apartment, house, commercial
    @SerialName("address")
    val address: String,
    @SerialName("city")
    val city: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("agent_id")
    val agentId: Int,
    @SerialName("views_count")
    val viewsCount: Int = 0,
    @SerialName("created_at")
    val createdAt: String? = null
)

// Добавьте type alias для обратной совместимости
typealias Property = PropertyListing

@Serializable
data class Agent(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("phone")
    val phone: String,
    @SerialName("email")
    val email: String,
    @SerialName("experience_years")
    val experienceYears: Int,
    @SerialName("photo_url")
    val photoUrl: String
)

@Serializable
data class ShowingRequest(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("property_id")
    val propertyId: Int,
    @SerialName("agent_id")
    val agentId: Int,
    @SerialName("client_name")
    val clientName: String,
    @SerialName("client_phone")
    val clientPhone: String,
    @SerialName("client_email")
    val clientEmail: String,
    @SerialName("preferred_date")
    val preferredDate: String,
    @SerialName("message")
    val message: String = "",
    @SerialName("created_at")
    val createdAt: String? = null
)

data class LocalFavorite(
    val propertyId: Int,
    val addedAt: Long = System.currentTimeMillis()
)
