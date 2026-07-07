package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiService
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PortfolioViewModel(
    application: Application,
    private val repository: PortfolioRepository
) : AndroidViewModel(application) {

    // Language setting: "en" or "bn"
    var currentLanguage by mutableStateOf("bn") // default to Bengali as per request
    
    // Theme Mode
    var isDarkMode by mutableStateOf(false)
    
    // Active styling for custom template preview
    var activeThemeColor by mutableStateOf("#3F51B5") // Indigo default
    var activeFontFamily by mutableStateOf("SansSerif") // SansSerif, Serif, Monospace
    
    // Wizard state
    var currentStepIndex by mutableStateOf(0)
    var draftPortfolio by mutableStateOf(Portfolio())
    
    // Lists editable in Wizard (copied in/out of draftPortfolio)
    var draftEducationList by mutableStateOf(listOf<Education>())
    var draftExperienceList by mutableStateOf(listOf<Experience>())
    var draftSkillsList by mutableStateOf(listOf<Skill>())
    var draftAchievementsList by mutableStateOf(listOf<Achievement>())
    var draftProjectsList by mutableStateOf(listOf<Project>())
    var draftCertificatesList by mutableStateOf(listOf<Certificate>())
    var draftLanguagesList by mutableStateOf(listOf("English", "Bangla"))
    var draftSocialMedia by mutableStateOf(SocialMedia())
    var draftReferencesList by mutableStateOf(listOf<Reference>())
    var draftSectionOrder by mutableStateOf(listOf<String>())

    // AI Writing states
    var aiLoading by mutableStateOf(false)
    var aiResultText by mutableStateOf("")

    // DB portfolios
    val allPortfolios: StateFlow<List<Portfolio>> = repository.allPortfolios
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Prepopulate with a mock developer portfolio if database is completely empty
        viewModelScope.launch {
            repository.allPortfolios.collect { list ->
                if (list.isEmpty()) {
                    createDemoPortfolio()
                }
            }
        }
    }

    private suspend fun createDemoPortfolio() {
        val demo = Portfolio(
            name = "Anisur Rahman",
            profession = "Senior Android Developer",
            tagline = "Crafting beautiful, offline-first mobile applications.",
            aboutMe = "Highly passionate software engineer with 5+ years of experience in Android SDK, Kotlin, Jetpack Compose, and clean architecture patterns.",
            email = "rahman.anis@gmail.com",
            phone = "+8801712345678",
            whatsApp = "+8801712345678",
            address = "Dhanmondi, Dhaka, Bangladesh",
            website = "rahman.infinityportfolio.app",
            profilePhoto = "https://images.unsplash.com/photo-1534528741775-53994a69daeb",
            selectedTemplate = "Developer",
            themeColor = "#00796B"
        )
        demo.setEducation(listOf(
            Education("BUTEX", "B.Sc. in Textile Engineering", "Textile", "3.80", "2020", "BUTEX"),
            Education("Dhaka College", "HSC", "Science", "5.00", "2016", "DC")
        ))
        demo.setExperience(listOf(
            Experience("Nexvora Labs", "Lead Android Engineer", "2023-01", "Present", "Developed responsive portfolio editor app and implemented local Room caching."),
            Experience("Infinite Apps", "Junior Kotlin Developer", "2021-06", "2022-12", "Built custom graphics with Canvas drawings and integrated web views.")
        ))
        demo.setSkills(listOf(
            Skill("HTML", 95),
            Skill("CSS", 90),
            Skill("Flutter", 80),
            Skill("Photoshop", 85),
            Skill("Kotlin", 95),
            Skill("Jetpack Compose", 90)
        ))
        demo.setAchievements(listOf(
            Achievement("🏆 BUTEX Admission - 15th Merit Position"),
            Achievement("🏆 National Science Olympiad - Runner-up"),
            Achievement("🏆 Nexvora Hackathon - Champion")
        ))
        demo.setProjects(listOf(
            Project("Infinity Portfolio Builder", "An AI-powered offline portfolio generator with PDF export.", "", "https://github.com/anis/portfolio-builder", "https://play.google.com/store", "https://anis.infinityportfolio.app", "Kotlin, Compose, Room, Gemini API")
        ))
        demo.setCertificates(listOf(
            Certificate("Google Android Developer Certification", "Google Developers", "2024-05", "")
        ))
        demo.setLanguages(listOf("Bangla", "English", "Japanese"))
        demo.setSocialMedia(SocialMedia(
            facebook = "https://facebook.com/anis",
            instagram = "https://instagram.com/anis",
            linkedIn = "https://linkedin.com/in/anis",
            github = "https://github.com/anis"
        ))
        demo.setReferences(listOf(
            Reference("Dr. Shuvo Rahman", "Professor & Mentor", "BUTEX", "shuvo@butex.edu.bd")
        ))
        repository.insertPortfolio(demo)
    }

    // Load an existing portfolio to start editing or viewing
    fun selectPortfolio(portfolio: Portfolio) {
        draftPortfolio = portfolio.copy()
        activeThemeColor = portfolio.themeColor
        activeFontFamily = portfolio.fontFamily
        
        draftEducationList = portfolio.educationList
        draftExperienceList = portfolio.experienceList
        draftSkillsList = portfolio.skillsList
        draftAchievementsList = portfolio.achievementsList
        draftProjectsList = portfolio.projectsList
        draftCertificatesList = portfolio.certificatesList
        draftLanguagesList = portfolio.languagesList
        draftSocialMedia = portfolio.socialMedia
        draftReferencesList = portfolio.referencesList
        draftSectionOrder = portfolio.sectionOrder
        
        currentStepIndex = 0
    }

    // Start building a completely new blank portfolio
    fun startNewPortfolio() {
        draftPortfolio = Portfolio()
        activeThemeColor = "#3F51B5"
        activeFontFamily = "SansSerif"
        
        draftEducationList = emptyList()
        draftExperienceList = emptyList()
        draftSkillsList = listOf(
            Skill("HTML", 95),
            Skill("CSS", 90),
            Skill("Flutter", 80),
            Skill("Photoshop", 85)
        )
        draftAchievementsList = emptyList()
        draftProjectsList = emptyList()
        draftCertificatesList = emptyList()
        draftLanguagesList = listOf("English", "Bangla")
        draftSocialMedia = SocialMedia()
        draftReferencesList = emptyList()
        draftSectionOrder = listOf("personal","education","experience","skills","achievements","projects","certificates","languages","socials","references")
        
        currentStepIndex = 0
    }

    // Save wizard changes into memory (draftPortfolio) and database
    fun saveDraftToDb(onComplete: (Int) -> Unit = {}) {
        viewModelScope.launch {
            // Update draftPortfolio with list variables
            draftPortfolio.setEducation(draftEducationList)
            draftPortfolio.setExperience(draftExperienceList)
            draftPortfolio.setSkills(draftSkillsList)
            draftPortfolio.setAchievements(draftAchievementsList)
            draftPortfolio.setProjects(draftProjectsList)
            draftPortfolio.setCertificates(draftCertificatesList)
            draftPortfolio.setLanguages(draftLanguagesList)
            draftPortfolio.setSocialMedia(draftSocialMedia)
            draftPortfolio.setReferences(draftReferencesList)
            draftPortfolio.setSectionOrder(draftSectionOrder)
            
            draftPortfolio.themeColor = activeThemeColor
            draftPortfolio.fontFamily = activeFontFamily

            if (draftPortfolio.id == 0) {
                val newId = repository.insertPortfolio(draftPortfolio)
                draftPortfolio = draftPortfolio.copy(id = newId.toInt())
                onComplete(newId.toInt())
            } else {
                repository.updatePortfolio(draftPortfolio)
                onComplete(draftPortfolio.id)
            }
        }
    }

    // Increment views/downloads metrics for analytics
    fun incrementMetric(portfolioId: Int, metricType: String) {
        viewModelScope.launch {
            val p = repository.getPortfolioById(portfolioId) ?: return@launch
            when (metricType) {
                "views" -> p.viewsCount += 1
                "downloads" -> p.downloadsCount += 1
                "shares" -> p.sharesCount += 1
                "visitors" -> p.visitorsCount += 1
            }
            repository.updatePortfolio(p)
        }
    }

    fun deletePortfolio(portfolio: Portfolio) {
        viewModelScope.launch {
            repository.deletePortfolio(portfolio)
        }
    }

    // AI Assist generation methods
    fun executeAiTask(taskType: String, input1: String, input2: String = "", input3: String = "") {
        viewModelScope.launch {
            aiLoading = true
            aiResultText = ""
            try {
                val res = when (taskType) {
                    "about_me" -> GeminiService.generateAboutMe(input1, input2, input3)
                    "objective" -> GeminiService.generateCareerObjective(input1, input2)
                    "skills" -> GeminiService.generateSkills(input1)
                    "experience" -> GeminiService.generateExperienceDescription(input1, input2)
                    "grammar" -> GeminiService.fixGrammar(input1)
                    "rewrite" -> GeminiService.professionalRewrite(input1)
                    "translate_bn_en" -> GeminiService.translateBengaliToEnglish(input1)
                    "translate_en_bn" -> GeminiService.translateEnglishToBengali(input1)
                    else -> "Unknown AI Task."
                }
                aiResultText = res
            } catch (e: Exception) {
                aiResultText = "Error: ${e.message}"
            } finally {
                aiLoading = false
            }
        }
    }
}

class PortfolioViewModelFactory(
    private val application: Application,
    private val repository: PortfolioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortfolioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PortfolioViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
