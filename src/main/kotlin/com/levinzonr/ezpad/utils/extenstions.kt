package com.levinzonr.ezpad.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.net.URI
import java.util.*
import javax.servlet.http.HttpServletRequest

val HttpServletRequest.baseUrl: String
get() {
    val uri = URI.create(requestURL.toString())
    return "${uri.scheme}://${uri.authority}"
}


fun <T> Optional<T>.tryGet() : T? {
   return try {
       get()
   } catch (e: Exception) {
       null
   }
}
inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

fun <T> List<T>.first(n: Int) : List<T> {
    return if (size < n) this else subList(0, n)
}

inline fun <reified T> Gson.fromJsonFile(jsonFile: String) : T {
    val json = File("src/main/resources/$jsonFile").inputStream().readBytes().toString(Charsets.UTF_8)
    return this.fromJson<T>(json, object: TypeToken<T>() {}.type)
}

fun String?.contains(substring: String) : Boolean {
    return if (this == null)  false
    else contains(substring, true)
}

