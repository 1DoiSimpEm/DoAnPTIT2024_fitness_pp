package ptit.vietpq.fitnessapp.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

object SpeechHelper {
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false

    /**
     * Initializes the TextToSpeech engine
     * @param context Application context
     * @param onInitListener Optional callback for initialization
     */
    fun initialize(context: Context, onInitListener: ((Boolean) -> Unit)? = null) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                val result = textToSpeech?.setLanguage(Locale.getDefault())

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("SpeechHelper", "Language not supported")
                    onInitListener?.invoke(false)
                } else {
                    onInitListener?.invoke(true)
                }
            } else {
                Log.e("SpeechHelper", "TextToSpeech initialization failed")
                onInitListener?.invoke(false)
            }
        }
    }

    /**
     * Speaks the given text
     * @param text Text to be spoken
     * @param pitch Pitch of the speech (1.0 is normal pitch)
     * @param speechRate Speed of speech (1.0 is normal speed)
     * @param onDoneListener Optional callback when speech is completed
     */
    fun speak(
        text: String,
        pitch: Float = 1.0f,
        speechRate: Float = 1.0f,
        onDoneListener: (() -> Unit)? = null
    ) {
        if (!isInitialized || isSpeaking()) {
            Log.e("SpeechHelper", "TextToSpeech not initialized")
            return
        }

        textToSpeech?.let { tts ->
            // Set pitch and speech rate
            tts.setPitch(pitch)
            tts.setSpeechRate(speechRate)

            // Set up utterance listener if a completion callback is provided
            onDoneListener?.let {
                tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}

                    override fun onDone(utteranceId: String?) {
                        it.invoke()
                    }

                    override fun onError(utteranceId: String?) {}
                })
            }

            // Speak the text
            val params = HashMap<String, String>()
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "SpeechHelper"
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, params)
        }
    }

    /**
     * Stops the current speech
     */
    fun stop() {
        textToSpeech?.stop()
    }

    /**
     * Shuts down the TextToSpeech engine
     */
    fun shutdown() {
        textToSpeech?.shutdown()
        isInitialized = false
    }

    /**
     * Checks if the TextToSpeech is currently speaking
     * @return Boolean indicating if speech is in progress
     */
    private fun isSpeaking(): Boolean {
        return textToSpeech?.isSpeaking == true
    }

    /**
     * Sets the language for speech
     * @param locale Desired language locale
     * @return Boolean indicating if language was successfully set
     */
    fun setLanguage(locale: Locale): Boolean {
        return if (isInitialized) {
            val result = textToSpeech?.setLanguage(locale)
            result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE
        } else {
            false
        }
    }
}