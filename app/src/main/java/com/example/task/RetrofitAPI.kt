package com.example.task

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface RetrofitAPI {
    // as we are making a post request to post a data
    // so we are annotating it with post
    // and along with that we are passing a parameter as users
    @POST("resMsg")
    fun  //on below line we are creating a method to post our data.
            createPost(@Body dataModal: DataModal?): Call<DataModal?>?
}
