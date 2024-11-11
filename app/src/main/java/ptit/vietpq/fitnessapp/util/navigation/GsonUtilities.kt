package ptit.vietpq.fitnessapp.util.navigation

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ptit.vietpq.fitnessapp.util.navigation.Constants.UTF_8
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.reflect.KType
import kotlin.reflect.javaType


internal object GsonUtilities {

    private val gson = Gson()

    /**
     * Function to parse [String] as [expectedType] using [gson]
     *
     * @param typeToken instance of [TypeToken] to help parse with [gson]
     * @return instance of [expectedType] from given [String]
     * @receiver [String]
     */
    fun <expectedType> String.parseToObject(typeToken: TypeToken<expectedType>): expectedType =
        gson.fromJson(URLDecoder.decode(this, UTF_8), typeToken.type)

    /**
     * Function to parse [sourceType] into Json-typed [String] using [Gson]
     *
     * @param source instance of [sourceType] to be parsed
     * @return [String] in the form of JSON from [source]
     */
    fun <sourceType> parseToJson(source: sourceType): String =
        URLEncoder.encode(gson.toJson(source), UTF_8)

    /**
     * Function to get [TypeToken] from given [KType]
     * @return [TypeToken] used for parsing JSON from given [KType]
     */
    @OptIn(ExperimentalStdlibApi::class)
    fun getTypeToken(type: KType): TypeToken<*> = TypeToken.get(type.javaType)
}