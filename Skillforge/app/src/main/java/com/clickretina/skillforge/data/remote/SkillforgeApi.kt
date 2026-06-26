package com.clickretina.skillforge.data.remote

import com.clickretina.skillforge.data.model.SkillforgeResponse
import retrofit2.http.GET

/**
 * The whole app is powered by this one endpoint. The path is relative to the
 * raw.githubusercontent.com base URL configured in [NetworkModule].
 */
interface SkillforgeApi {
    @GET("android-assesment/notes/refs/heads/main/data.json")
    suspend fun getCatalog(): SkillforgeResponse
}
