package ptit.vietpq.fitnessapp.data.remote.service

import ptit.vietpq.fitnessapp.data.remote.request.ProgressRequest
import ptit.vietpq.fitnessapp.data.remote.response.ProgressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProgressService {
    @POST("/api/user-exercise-progress/")
    fun postProgress(@Body progress : ProgressRequest) : Response<ProgressResponse>
}