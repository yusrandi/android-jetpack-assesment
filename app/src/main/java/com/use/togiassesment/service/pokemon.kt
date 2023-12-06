package com.use.togiassesment.service

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PokemonResult>
)

data class PokemonResult(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class PokemonDetail(
    @SerializedName("abilities") val abilities: List<Ability>,
    @SerializedName("base_experience") val baseExperience: Int,
    @SerializedName("forms") val forms: List<Form>,
    @SerializedName("sprites") val sprites: Sprites
)

data class Ability(
    @SerializedName("ability") val ability: AbilityInfo,
    @SerializedName("is_hidden") val isHidden: Boolean,
    @SerializedName("slot") val slot: Int
)

data class AbilityInfo(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

data class Form(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
data class Sprites(
    @SerializedName("front_default") val frontDefault: String,
)
