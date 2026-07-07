package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val MODEL_NAME = "gemini-3.5-flash"
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private suspend fun callGemini(prompt: String, systemInstruction: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Error: Gemini API Key is missing. Please add your GEMINI_API_KEY in the Secrets panel."
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent?key=$apiKey"
        val mediaType = "application/json; charset=utf-8".toMediaType()

        val jsonBody = JSONObject().apply {
            // contents
            val contentsArray = JSONArray().apply {
                val contentObj = JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        val partObj = JSONObject().apply {
                            put("text", prompt)
                        }
                        put(partObj)
                    }
                    put("parts", partsArray)
                }
                put(contentObj)
            }
            put("contents", contentsArray)

            // systemInstruction if present
            if (systemInstruction != null) {
                val sysInstObj = JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        val partObj = JSONObject().apply {
                            put("text", systemInstruction)
                        }
                        put(partObj)
                    }
                    put("parts", partsArray)
                }
                put("systemInstruction", sysInstObj)
            }
        }

        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody(mediaType))
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    val errMsg = try {
                        JSONObject(bodyString).getJSONObject("error").getString("message")
                    } catch (e: Exception) {
                        "HTTP ${response.code}"
                    }
                    return@withContext "API Error: $errMsg"
                }

                val jsonResponse = JSONObject(bodyString)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val content = candidates.getJSONObject(0).optJSONObject("content")
                    if (content != null) {
                        val parts = content.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            return@withContext parts.getJSONObject(0).optString("text", "")
                        }
                    }
                }
                "Error: No content generated."
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    suspend fun generateAboutMe(profession: String, tagline: String, skills: String): String {
        val systemInstruction = "You are an expert resume writer and career coach. Write a short, highly professional, compelling 'About Me' paragraph (3-4 sentences max)."
        val prompt = "Write an 'About Me' section for a professional with the following details:\nProfession: $profession\nTagline: $tagline\nSkills: $skills\n\nMake it engaging and direct."
        return callGemini(prompt, systemInstruction)
    }

    suspend fun generateCareerObjective(profession: String, tagline: String): String {
        val systemInstruction = "You are a professional CV writing expert. Write a strong, concise career objective statement (2 sentences max)."
        val prompt = "Write a career objective statement for a professional with the following details:\nProfession: $profession\nTagline: $tagline\n\nHighlight aspiration and value addition."
        return callGemini(prompt, systemInstruction)
    }

    suspend fun generateSkills(profession: String): String {
        val systemInstruction = "You are a tech recruiter. Provide a list of 5 key, modern professional skills suitable for a given profession. Format output as a simple list."
        val prompt = "Provide 5 essential skills for a professional with the profession: $profession. Do not write any introduction or conclusion, just output the 5 skills, one per line."
        return callGemini(prompt, systemInstruction)
    }

    suspend fun generateExperienceDescription(company: String, position: String): String {
        val systemInstruction = "You are an expert CV writer. Write 3 bullet points detailing professional responsibilities and achievements for a job role."
        val prompt = "Write 3 high-impact professional achievement bullet points for the role:\nPosition: $position\nCompany: $company\n\nStart each bullet with an action verb."
        return callGemini(prompt, systemInstruction)
    }

    suspend fun fixGrammar(text: String): String {
        val systemInstruction = "You are an expert copyeditor. Fix any grammatical, spelling, or stylistic errors in the text. Output only the corrected text, nothing else."
        val prompt = "Correct and fix the grammar for this text:\n\n$text"
        return callGemini(prompt, systemInstruction)
    }

    suspend fun professionalRewrite(text: String): String {
        val systemInstruction = "You are an executive resume writer. Rewrite the text to make it sound highly professional, impressive, and executive-level. Output only the rewritten text, nothing else."
        val prompt = "Rewrite this text professionally for a resume:\n\n$text"
        return callGemini(prompt, systemInstruction)
    }

    suspend fun translateBengaliToEnglish(text: String): String {
        val systemInstruction = "You are a professional translator. Translate the text from Bengali to English, maintaining standard professional vocabulary. Output only the translated text, nothing else."
        val prompt = "Translate this Bengali text to professional English:\n\n$text"
        return callGemini(prompt, systemInstruction)
    }

    suspend fun translateEnglishToBengali(text: String): String {
        val systemInstruction = "You are a professional translator. Translate the text from English to Bengali, maintaining a natural, polite, and professional tone. Output only the translated text, nothing else."
        val prompt = "Translate this English text to professional Bengali:\n\n$text"
        return callGemini(prompt, systemInstruction)
    }
}
