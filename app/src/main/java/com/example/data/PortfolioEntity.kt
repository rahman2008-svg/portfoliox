package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

@Entity(tableName = "portfolios")
data class Portfolio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String = "",
    var profession: String = "",
    var tagline: String = "",
    var aboutMe: String = "",
    var email: String = "",
    var phone: String = "",
    var whatsApp: String = "",
    var address: String = "",
    var website: String = "",
    var profilePhoto: String = "",
    
    // JSON lists
    var educationJson: String = "[]",
    var experienceJson: String = "[]",
    var skillsJson: String = "[]",
    var achievementsJson: String = "[]",
    var projectsJson: String = "[]",
    var certificatesJson: String = "[]",
    var languagesJson: String = "[]",
    var socialMediaJson: String = "{}",
    var referencesJson: String = "[]",
    
    // Style configurations
    var selectedTemplate: String = "Modern", // Modern, Minimal, Dark, Light, Corporate, Student, Developer, Creative
    var themeColor: String = "#3F51B5", // Hex code (Indigo default)
    var fontFamily: String = "SansSerif", // SansSerif, Serif, Monospace
    var sectionOrderJson: String = "[\"personal\",\"education\",\"experience\",\"skills\",\"achievements\",\"projects\",\"certificates\",\"languages\",\"socials\",\"references\"]",
    
    // Analytics
    var viewsCount: Int = 0,
    var downloadsCount: Int = 0,
    var sharesCount: Int = 0,
    var visitorsCount: Int = 0,
    
    var createdAt: Long = System.currentTimeMillis()
) {
    // Convenience helper properties
    val educationList: List<Education>
        get() = JsonSerialization.deserializeList(educationJson) { Education.fromJsonObject(it) }

    val experienceList: List<Experience>
        get() = JsonSerialization.deserializeList(experienceJson) { Experience.fromJsonObject(it) }

    val skillsList: List<Skill>
        get() = JsonSerialization.deserializeList(skillsJson) { Skill.fromJsonObject(it) }

    val achievementsList: List<Achievement>
        get() = JsonSerialization.deserializeList(achievementsJson) { Achievement.fromJsonObject(it) }

    val projectsList: List<Project>
        get() = JsonSerialization.deserializeList(projectsJson) { Project.fromJsonObject(it) }

    val certificatesList: List<Certificate>
        get() = JsonSerialization.deserializeList(certificatesJson) { Certificate.fromJsonObject(it) }

    val languagesList: List<String>
        get() = JsonSerialization.deserializeStrings(languagesJson)

    val socialMedia: SocialMedia
        get() = try {
            SocialMedia.fromJsonObject(JSONObject(socialMediaJson))
        } catch (e: Exception) {
            SocialMedia()
        }

    val referencesList: List<Reference>
        get() = JsonSerialization.deserializeList(referencesJson) { Reference.fromJsonObject(it) }

    val sectionOrder: List<String>
        get() = JsonSerialization.deserializeStrings(sectionOrderJson)

    // Helper functions to set lists
    fun setEducation(list: List<Education>) {
        educationJson = JsonSerialization.serializeList(list) { it.toJsonObject() }
    }

    fun setExperience(list: List<Experience>) {
        experienceJson = JsonSerialization.serializeList(list) { it.toJsonObject() }
    }

    fun setSkills(list: List<Skill>) {
        skillsJson = JsonSerialization.serializeList(list) { it.toJsonObject() }
    }

    fun setAchievements(list: List<Achievement>) {
        achievementsJson = JsonSerialization.serializeList(list) { it.toJsonObject() }
    }

    fun setProjects(list: List<Project>) {
        projectsJson = JsonSerialization.serializeList(list) { it.toJsonObject() }
    }

    fun setCertificates(list: List<Certificate>) {
        certificatesJson = JsonSerialization.serializeList(list) { it.toJsonObject() }
    }

    fun setLanguages(list: List<String>) {
        languagesJson = JsonSerialization.serializeStrings(list)
    }

    fun setSocialMedia(socials: SocialMedia) {
        socialMediaJson = socials.toJsonObject().toString()
    }

    fun setReferences(list: List<Reference>) {
        referencesJson = JsonSerialization.serializeList(list) { it.toJsonObject() }
    }

    fun setSectionOrder(order: List<String>) {
        sectionOrderJson = JsonSerialization.serializeStrings(order)
    }
}
