package com.majorproject.roomify.data.api

import com.google.gson.JsonObject
import com.majorproject.roomify.constants.contentModerationEndpoint
import com.majorproject.roomify.constants.textCompletionsEndpoint
import com.majorproject.roomify.constants.textCompletionsTurboEndpoint
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface OpenAIApi {
    @POST(textCompletionsEndpoint)
    @Streaming
    fun textCompletionsWithStream(@Body body: JsonObject): Call<ResponseBody>

    @POST(textCompletionsTurboEndpoint)
    @Streaming
    fun textCompletionsTurboWithStream(@Body body: JsonObject): Call<ResponseBody>

    @POST(textCompletionsTurboEndpoint)
    fun textCompletionsTurbo(@Body body: JsonObject): Call<ResponseBody>

}