package ptit.vietpq.fitnessapp.data.remote.service

import ptit.vietpq.fitnessapp.data.remote.request.RegisterRequest
import ptit.vietpq.fitnessapp.data.remote.request.UserInfoRequest
import ptit.vietpq.fitnessapp.data.remote.response.LoginResponse
import ptit.vietpq.fitnessapp.data.remote.response.RegisterResponse
import ptit.vietpq.fitnessapp.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface AuthService {
    @POST("api/user/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("api/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

    @PUT("api/user/{username}")
    suspend fun updateUser(
        @Path("username") username: String,
        @Body userInfo: UserInfoRequest
    ): Response<UserInfoRequest>

    @GET("api/user/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): Response<UserResponse>

}