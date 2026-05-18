package com.raithavarta.app.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.raithavarta.app.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

/**
 * AI client using Groq API (free tier).
 *
 * Model  : meta-llama/llama-4-scout-17b-16e-instruct — multimodal vision, fast, free.
 * Key    : read from BuildConfig.GROQ_API_KEY (injected from local.properties at build time).
 * Endpoint: https://api.groq.com/openai/v1/chat/completions
 */
object GroqClient {

    private const val MODEL       = "meta-llama/llama-4-scout-17b-16e-instruct"
    private const val ENDPOINT    = "https://api.groq.com/openai/v1/chat/completions"
    private const val MAX_DIM     = 1024
    private const val JPEG_QUALITY = 85

    private val http: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    sealed class Result {
        data class Success(val text: String) : Result()
        data class Error(val message: String) : Result()
    }

    /** Returns true if an API key is available (from local.properties via BuildConfig). */
    fun isConfigured(): Boolean = BuildConfig.GROQ_API_KEY.isNotBlank()

    /**
     * Synchronous — must be called from a background thread / coroutine (Dispatchers.IO).
     */
    fun analyzeLeaf(context: Context, imageUri: Uri, prompt: String): Result {
        val apiKey = BuildConfig.GROQ_API_KEY
        if (apiKey.isBlank()) {
            return Result.Error("API key not set. Add GROQ_API_KEY to local.properties and rebuild.")
        }

        val base64 = try {
            encodeImage(context, imageUri)
        } catch (t: Throwable) {
            return Result.Error("Could not read image: ${t.message}")
        }

        val imageContent = JSONObject().apply {
            put("type", "image_url")
            put("image_url", JSONObject().apply {
                put("url", "data:image/jpeg;base64,$base64")
            })
        }
        val textContent = JSONObject().apply {
            put("type", "text")
            put("text", prompt)
        }
        val userMessage = JSONObject().apply {
            put("role", "user")
            put("content", JSONArray().put(imageContent).put(textContent))
        }
        val body = JSONObject().apply {
            put("model", MODEL)
            put("messages", JSONArray().put(userMessage))
            put("max_tokens", 1024)
            put("temperature", 0.4)
        }

        val request = Request.Builder()
            .url(ENDPOINT)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $apiKey")
            .post(body.toString().toRequestBody("application/json".toMediaType()))
            .build()

        return try {
            http.newCall(request).execute().use { resp ->
                val raw = resp.body?.string().orEmpty()
                if (!resp.isSuccessful) return Result.Error("HTTP ${resp.code}: ${parseError(raw)}")
                val text = parseText(raw)
                if (text.isBlank()) Result.Error("Empty response from AI.") else Result.Success(text)
            }
        } catch (t: Throwable) {
            Result.Error("Network error: ${t.message}")
        }
    }

    private fun parseText(raw: String): String = try {
        JSONObject(raw)
            .optJSONArray("choices")?.optJSONObject(0)
            ?.optJSONObject("message")?.optString("content", "").orEmpty().trim()
    } catch (_: Throwable) { "" }

    private fun parseError(raw: String): String = try {
        JSONObject(raw).optJSONObject("error")?.optString("message").orEmpty()
            .ifBlank { raw.take(200) }
    } catch (_: Throwable) { raw.take(200) }

    private fun encodeImage(context: Context, uri: Uri): String {
        val bytes = context.contentResolver.openInputStream(uri).use { it!!.readBytes() }
        val opts  = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, opts)
        var sample = 1
        while (opts.outWidth / sample > MAX_DIM || opts.outHeight / sample > MAX_DIM) sample *= 2
        val bmp: Bitmap = BitmapFactory.decodeByteArray(
            bytes, 0, bytes.size,
            BitmapFactory.Options().apply { inSampleSize = sample }
        ) ?: throw IllegalStateException("Image decode failed")
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, baos)
        bmp.recycle()
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
    }
}
