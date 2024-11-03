package ptit.vietpq.fitnessapp.core

import androidx.annotation.MainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.Closeable
import javax.inject.Inject
import kotlin.coroutines.ContinuationInterceptor

interface HasEventFlow<E> {
  val eventFlow: Flow<E>
}

suspend fun debugCheckImmediateMainDispatcher() {
    val interceptor = currentCoroutineContext()[ContinuationInterceptor]
    check(interceptor === Dispatchers.Main.immediate) {
      "Expected ContinuationInterceptor to be Dispatchers.Main.immediate but was $interceptor"
    }
}

@MainThread
class EventChannel<E> @Inject
constructor() :
  Closeable,
  HasEventFlow<E> {
  private val _eventChannel = Channel<E>(Channel.UNLIMITED)

  override val eventFlow: Flow<E> = _eventChannel.receiveAsFlow()


  /**
   * Must be called in Dispatchers.Main.immediate, otherwise it will throw an exception.
   * If you want to send an event from other Dispatcher,
   * use `withContext(Dispatchers.Main.immediate) { eventChannel.send(event) }`
   */
  @MainThread
  suspend fun send(event: E) {
    debugCheckImmediateMainDispatcher()

    _eventChannel.trySend(event)
      .onFailure {

      }
      .onSuccess {
      }
      .getOrThrow()
  }

  override fun close() {
    _eventChannel.close()
  }
}