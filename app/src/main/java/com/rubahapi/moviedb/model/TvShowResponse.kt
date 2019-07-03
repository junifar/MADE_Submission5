package com.rubahapi.moviedb.model

import com.google.gson.annotations.SerializedName

data class TvShowResponse(
    @SerializedName("results")
    val tvShows: List<TvShow>
)