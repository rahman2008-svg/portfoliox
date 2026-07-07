package com.example.data

import org.json.JSONArray
import org.json.JSONObject

data class Education(
    var institute: String = "",
    var degree: String = "",
    var department: String = "",
    var gpa: String = "",
    var year: String = "",
    var logoUri: String = ""
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("institute", institute)
            put("degree", degree)
            put("department", department)
            put("gpa", gpa)
            put("year", year)
            put("logoUri", logoUri)
        }
    }

    companion object {
        fun fromJsonObject(obj: JSONObject): Education {
            return Education(
                institute = obj.optString("institute", ""),
                degree = obj.optString("degree", ""),
                department = obj.optString("department", ""),
                gpa = obj.optString("gpa", ""),
                year = obj.optString("year", ""),
                logoUri = obj.optString("logoUri", "")
            )
        }
    }
}

data class Experience(
    var company: String = "",
    var position: String = "",
    var joiningDate: String = "",
    var leavingDate: String = "",
    var description: String = ""
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("company", company)
            put("position", position)
            put("joiningDate", joiningDate)
            put("leavingDate", leavingDate)
            put("description", description)
        }
    }

    companion object {
        fun fromJsonObject(obj: JSONObject): Experience {
            return Experience(
                company = obj.optString("company", ""),
                position = obj.optString("position", ""),
                joiningDate = obj.optString("joiningDate", ""),
                leavingDate = obj.optString("leavingDate", ""),
                description = obj.optString("description", "")
            )
        }
    }
}

data class Skill(
    var name: String = "",
    var percentage: Int = 80
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("name", name)
            put("percentage", percentage)
        }
    }

    companion object {
        fun fromJsonObject(obj: JSONObject): Skill {
            return Skill(
                name = obj.optString("name", ""),
                percentage = obj.optInt("percentage", 80)
            )
        }
    }
}

data class Achievement(
    var title: String = ""
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("title", title)
        }
    }

    companion object {
        fun fromJsonObject(obj: JSONObject): Achievement {
            return Achievement(
                title = obj.optString("title", "")
            )
        }
    }
}

data class Project(
    var title: String = "",
    var description: String = "",
    var screenshotUri: String = "",
    var githubLink: String = "",
    var playStoreLink: String = "",
    var websiteLink: String = "",
    var techUsed: String = ""
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("title", title)
            put("description", description)
            put("screenshotUri", screenshotUri)
            put("githubLink", githubLink)
            put("playStoreLink", playStoreLink)
            put("websiteLink", websiteLink)
            put("techUsed", techUsed)
        }
    }

    companion object {
        fun fromJsonObject(obj: JSONObject): Project {
            return Project(
                title = obj.optString("title", ""),
                description = obj.optString("description", ""),
                screenshotUri = obj.optString("screenshotUri", ""),
                githubLink = obj.optString("githubLink", ""),
                playStoreLink = obj.optString("playStoreLink", ""),
                websiteLink = obj.optString("websiteLink", ""),
                techUsed = obj.optString("techUsed", "")
            )
        }
    }
}

data class Certificate(
    var name: String = "",
    var issuedBy: String = "",
    var issueDate: String = "",
    var attachmentUri: String = ""
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("name", name)
            put("issuedBy", issuedBy)
            put("issueDate", issueDate)
            put("attachmentUri", attachmentUri)
        }
    }

    companion object {
        fun fromJsonObject(obj: JSONObject): Certificate {
            return Certificate(
                name = obj.optString("name", ""),
                issuedBy = obj.optString("issuedBy", ""),
                issueDate = obj.optString("issueDate", ""),
                attachmentUri = obj.optString("attachmentUri", "")
            )
        }
    }
}

data class SocialMedia(
    var facebook: String = "",
    var instagram: String = "",
    var linkedIn: String = "",
    var github: String = "",
    var youtube: String = "",
    var twitter: String = "",
    var portfolioWebsite: String = ""
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("facebook", facebook)
            put("instagram", instagram)
            put("linkedIn", linkedIn)
            put("github", github)
            put("youtube", youtube)
            put("twitter", twitter)
            put("portfolioWebsite", portfolioWebsite)
        }
    }

    companion object {
        fun fromJsonObject(obj: JSONObject): SocialMedia {
            return SocialMedia(
                facebook = obj.optString("facebook", ""),
                instagram = obj.optString("instagram", ""),
                linkedIn = obj.optString("linkedIn", ""),
                github = obj.optString("github", ""),
                youtube = obj.optString("youtube", ""),
                twitter = obj.optString("twitter", ""),
                portfolioWebsite = obj.optString("portfolioWebsite", "")
            )
        }
    }
}

data class Reference(
    var name: String = "",
    var role: String = "", // e.g. Teacher, Manager, Client, Mentor
    var company: String = "",
    var emailOrContact: String = ""
) {
    fun toJsonObject(): JSONObject {
        return JSONObject().apply {
            put("name", name)
            put("role", role)
            put("company", company)
            put("emailOrContact", emailOrContact)
        }
    }

    companion object {
        fun fromJsonObject(obj: JSONObject): Reference {
            return Reference(
                name = obj.optString("name", ""),
                role = obj.optString("role", ""),
                company = obj.optString("company", ""),
                emailOrContact = obj.optString("emailOrContact", "")
            )
        }
    }
}

// Serialization List Helpers
object JsonSerialization {
    fun <T> serializeList(list: List<T>, converter: (T) -> JSONObject): String {
        val array = JSONArray()
        list.forEach { array.put(converter(it)) }
        return array.toString()
    }

    fun <T> deserializeList(json: String?, parser: (JSONObject) -> T): List<T> {
        if (json.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<T>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(parser(obj))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun serializeStrings(list: List<String>): String {
        val array = JSONArray()
        list.forEach { array.put(it) }
        return array.toString()
    }

    fun deserializeStrings(json: String?): List<String> {
        if (json.isNullOrEmpty()) return emptyList()
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
