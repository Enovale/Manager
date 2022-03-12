package com.aliucord.manager.models.github

import com.google.gson.annotations.SerializedName

data class GithubUser(
    @SerializedName("login") val name: String,
    val contributions: Int
)