package ptit.vietpq.fitnessapp.core

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.Dispatchers
import kotlin.Result
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

private const val STOP_TIMEOUT_MILLIS: Long = 5000

/**
 * A [SharingStarted] meant to be used with a [StateFlow] to expose data to the UI.
 *
 * When the UI stops observing, upstream flows stay active for some time to allow the system to
 * come back from a short-lived configuration change (such as rotations). If the UI stops
 * observing for longer, the cache is kept but the upstream flows are stopped. When the UI comes
 * back, the latest value is replayed and the upstream flows are executed again. This is done to
 * save resources when the app is in the background but let users switch between apps quickly.
 */
val WHILE_UI_SUBSCRIBED: SharingStarted = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS)

/**
 * https://github.com/Kotlin/kotlinx.coroutines/issues/1814
 */
@OptIn(ExperimentalContracts::class)
@Suppress("RedundantSuspendModifier")
suspend inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
    return try {
        val result = block()
        if (result is Response<*>) {
            if (result.isSuccessful) {
                Result.success(result)
            } else {
                Result.failure(HttpException(result))
            }
        } else {
            Result.success(result)
        }
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

@OptIn(ExperimentalContracts::class)
suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    contract { callsInPlace(apiCall, InvocationKind.AT_MOST_ONCE) }
    try {
        val response = apiCall()
        return if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(
                ApiException(
                    Status(
                        response.code(),
                        response.message()
                    )
                )
            )
        }
    } catch (e: Exception) {
        return Result.failure(e)
    }
}