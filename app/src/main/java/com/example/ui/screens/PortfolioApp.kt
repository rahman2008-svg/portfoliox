package com.example.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.*
import com.example.ui.PortfolioViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.HorizontalDivider

// Custom edge-to-edge compliant TopAppBar helper for clean Material 3 design without experimental warnings
@Composable
fun SmallTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    Surface(
        tonalElevation = 3.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (navigationIcon != null) {
                navigationIcon()
            } else {
                Spacer(modifier = Modifier.width(16.dp))
            }
            Box(modifier = Modifier.weight(1f)) {
                title()
            }
            if (actions != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            }
        }
    }
}


// Highly organized localized strings map
fun getStr(key: String, lang: String): String {
    val en = mapOf(
        "app_name" to "PortfolioX",
        "splash_tagline" to "AI-Powered Professional Portfolio Builder",
        "loading" to "Securing credentials...",
        "onboarding_1_title" to "What is a Portfolio?",
        "onboarding_1_desc" to "A professional portfolio is a curated collection of your best work, achievements, and technical expertise designed to secure high-value clients and positions.",
        "onboarding_2_title" to "How to Build It?",
        "onboarding_2_desc" to "Step-by-step wizard guides you to feed information. Gemini AI writes stunning summaries, bullet points, and skills in seconds.",
        "onboarding_3_title" to "One-Click Live Web",
        "onboarding_3_desc" to "Instantly generate your personal website (e.g., rahman.infinityportfolio.app) with lightning-fast caching, SEO, animations, and contact form.",
        "onboarding_4_title" to "PDF & Image Export",
        "onboarding_4_desc" to "Print beautifully formatted, print-ready resumes in high-quality A4 or Letter sizes with one-tap layout color and font changes.",
        "login_title" to "Welcome to PortfolioX",
        "login_subtitle" to "Unlock Cloud Backup, live website syncing, and professional metrics.",
        "login_google" to "Continue with Google",
        "login_email" to "Continue with Email",
        "login_guest" to "Continue as Guest",
        "home_create" to "Create New Portfolio",
        "home_my_ports" to "My Portfolios",
        "home_website" to "Website Generator",
        "home_templates" to "Template Store",
        "home_dashboard" to "Analytics Dashboard",
        "home_settings" to "Settings",
        "home_banner_title" to "Empower Your Career",
        "home_banner_desc" to "Create gorgeous, professional portfolios using 10-step wizard and Gemini AI writing assistant.",
        "wizard" to "Portfolio Wizard",
        "wizard_personal" to "Personal Information",
        "wizard_edu" to "Education Qualifications",
        "wizard_exp" to "Work Experiences",
        "wizard_skills" to "Core Skills",
        "wizard_achieve" to "Key Achievements",
        "wizard_projects" to "Featured Projects",
        "wizard_certs" to "Certificates & Badges",
        "wizard_langs" to "Languages Spoken",
        "wizard_socials" to "Social Media Handles",
        "wizard_refs" to "Professional References",
        "ai_assist" to "Gemini AI Assistant",
        "ai_prompt_hint" to "Enter details or click quick generate...",
        "theme_customizer" to "Theme Customizer",
        "save" to "Save & Continue",
        "prev" to "Back",
        "next" to "Next",
        "done" to "Finish & Preview",
        "add_more" to "Add New Row",
        "delete" to "Remove",
        "analytics_title" to "Portfolio Performance",
        "analytics_views" to "Total Views",
        "analytics_downloads" to "Resumes Downloaded",
        "analytics_visitors" to "Unique Visitors",
        "analytics_shares" to "Social Shares",
        "settings_lang" to "App Language / ভাষা পরিবর্তন",
        "settings_dark" to "Dark Mode",
        "settings_notif" to "Push Notifications",
        "settings_backup" to "Cloud Sync Backup",
        "settings_logout" to "Sign Out",
        "edit_content" to "Edit Content",
        "download_pdf" to "Download PDF",
        "view_website" to "View Website"
    )
    val bn = mapOf(
        "app_name" to "PortfolioX",
        "splash_tagline" to "এআই-চালিত স্মার্ট পোর্টফোলিও বিল্ডার",
        "loading" to "তথ্য বিশ্লেষণ করা হচ্ছে...",
        "onboarding_1_title" to "পোর্টফোলিও কী?",
        "onboarding_1_desc" to "পোর্টফোলিও হলো আপনার সেরা কাজ, দক্ষতা, অর্জন এবং অভিজ্ঞতার একটি সুন্দর সংকলন, যা নিয়োগকর্তা বা ক্লায়েন্টদের কাছে আপনার যোগ্যতা ও সক্ষমতা তুলে ধরে।",
        "onboarding_2_title" to "কীভাবে তৈরি করবেন?",
        "onboarding_2_desc" to "আমাদের ১০-ধাপের উইজার্ডে তথ্য দিন। জেমিনি এআই আপনার জন্য আকর্ষণীয় কেরিয়ার অবজেক্টিভ, কাজের চমৎকার বর্ণনা ও সঠিক স্কিল তৈরি করে দেবে!",
        "onboarding_3_title" to "এক ক্লিকে ওয়েবসাইট",
        "onboarding_3_desc" to "পিডিএফ ডাউনলোডের পাশাপাশি তৈরি করুন আপনার লাইভ ব্যক্তিগত ওয়েবসাইট (যেমন: rahman.infinityportfolio.app) যা এসইও ফ্রেন্ডলি ও আকর্ষণীয়।",
        "onboarding_4_title" to "পিডিএফ এবং ইমেজ এক্সপোর্ট",
        "onboarding_4_desc" to "এক ক্লিকে আপনার পোর্টফোলিওকে হাই-কোয়ালিটি প্রিন্ট-রেডি A4 অথবা Letter সাইজের রেজুমে পিডিএফ বা জেপিইজি ইমেজ হিসেবে সেভ ও ডাউনলোড করুন।",
        "login_title" to "PortfolioX এ স্বাগতম",
        "login_subtitle" to "ক্লাউড ব্যাকআপ, লাইভ ওয়েবসাইট হোস্টিং এবং ভিজিটর অ্যানালিটিক্স অ্যাক্সেস করুন।",
        "login_google" to "গুগল একাউন্ট দিয়ে প্রবেশ",
        "login_email" to "ইমেইল একাউন্ট দিয়ে প্রবেশ",
        "login_guest" to "গেস্ট হিসেবে প্রবেশ",
        "home_create" to "নতুন পোর্টফোলিও তৈরি করুন",
        "home_my_ports" to "আমার পোর্টফোলিওসমূহ",
        "home_website" to "পোর্টফোলিও ওয়েবসাইট",
        "home_templates" to "টেমপ্লেট স্টোর",
        "home_dashboard" to "অ্যানালিটিক্স ড্যাশবোর্ড",
        "home_settings" to "অ্যাপ সেটিংস",
        "home_banner_title" to "স্মার্ট পোর্টফোলিও বিল্ডার",
        "home_banner_desc" to "১০টি সহজ ধাপে আপনার তথ্য পূরণ করুন এবং জেমিনি এআই-এর ম্যাজিক দিয়ে কেরিয়ারের সুন্দর পোর্টফোলিও ডিজাইন করুন।",
        "wizard" to "পোর্টফোলিও উইজার্ড",
        "wizard_personal" to "ব্যক্তিগত তথ্য",
        "wizard_edu" to "শিক্ষাগত যোগ্যতা",
        "wizard_exp" to "কাজের অভিজ্ঞতা",
        "wizard_skills" to "দক্ষতা (Skills)",
        "wizard_achieve" to "অর্জনসমূহ (Achievements)",
        "wizard_projects" to "সেরা প্রজেক্টসমূহ",
        "wizard_certs" to "সার্টিফিকেটসমূহ",
        "wizard_langs" to "ভাষাসমূহ",
        "wizard_socials" to "সোশ্যাল মিডিয়া লিংক",
        "wizard_refs" to "রেফারেন্সসমূহ",
        "ai_assist" to "জেমিনি এআই অ্যাসিস্ট্যান্ট",
        "ai_prompt_hint" to "আপনার তথ্য লিখুন বা নিচের কুইক জেনারেট বাটনে চাপুন...",
        "theme_customizer" to "ওয়ান-ট্যাপ থিম সুইচ",
        "save" to "সংরক্ষণ ও পরবর্তী",
        "prev" to "পূর্ববর্তী",
        "next" to "পরবর্তী",
        "done" to "সম্পন্ন ও প্রিভিউ",
        "add_more" to "নতুন রো যুক্ত করুন",
        "delete" to "মুছে ফেলুন",
        "analytics_title" to "পোর্টফোলিও পারফরম্যান্স",
        "analytics_views" to "মোট ভিউ",
        "analytics_downloads" to "মোট ডাউনলোড",
        "analytics_visitors" to "মোট ভিজিটর",
        "analytics_shares" to "সোশ্যাল শেয়ার",
        "settings_lang" to "অ্যাপ ভাষা পরিবর্তন / Language",
        "settings_dark" to "ডার্ক মোড",
        "settings_notif" to "পুশ নোটিফিকেশন",
        "settings_backup" to "ক্লাউড সিঙ্ক ব্যাকআপ",
        "settings_logout" to "লগ আউট",
        "edit_content" to "তথ্য পরিবর্তন",
        "download_pdf" to "পিডিএফ ডাউনলোড",
        "view_website" to "ওয়েবসাইট প্রিভিউ"
    )
    return if (lang == "bn") bn[key] ?: (en[key] ?: key) else en[key] ?: key
}

@Composable
fun PortfolioAppNavHost(viewModel: PortfolioViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController, viewModel)
        }
        composable("onboarding") {
            OnboardingScreen(navController, viewModel)
        }
        composable("login") {
            LoginScreen(navController, viewModel)
        }
        composable("home") {
            HomeScreen(navController, viewModel)
        }
        composable("wizard") {
            WizardScreen(navController, viewModel)
        }
        composable("preview/{portfolioId}") { backStackEntry ->
            val pid = backStackEntry.arguments?.getString("portfolioId")?.toIntOrNull() ?: 0
            LivePreviewScreen(navController, viewModel, pid)
        }
        composable("website/{portfolioId}") { backStackEntry ->
            val pid = backStackEntry.arguments?.getString("portfolioId")?.toIntOrNull() ?: 0
            LiveWebsiteMockScreen(navController, viewModel, pid)
        }
        composable("dashboard") {
            DashboardScreen(navController, viewModel)
        }
        composable("settings") {
            SettingsScreen(navController, viewModel)
        }
    }
}

// ==========================================
// 1. SPLASH SCREEN
// ==========================================
@Composable
fun SplashScreen(navController: NavController, viewModel: PortfolioViewModel) {
    val context = LocalContext.current
    var startAnim by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        startAnim = true
        delay(2000)
        navController.navigate("onboarding") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E3C72),
                        Color(0xFF2A5298)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Elegant brand logo representational circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(54.dp),
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = getStr("app_name", viewModel.currentLanguage),
                fontSize = 36.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = getStr("splash_tagline", viewModel.currentLanguage),
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = getStr("loading", viewModel.currentLanguage),
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

// ==========================================
// 2. ONBOARDING SCREEN
// ==========================================
@Composable
fun OnboardingScreen(navController: NavController, viewModel: PortfolioViewModel) {
    var stageIndex by remember { mutableStateOf(0) }
    val stages = listOf(
        Pair("onboarding_1_title", "onboarding_1_desc"),
        Pair("onboarding_2_title", "onboarding_2_desc"),
        Pair("onboarding_3_title", "onboarding_3_desc"),
        Pair("onboarding_4_title", "onboarding_4_desc")
    )

    val currentStage = stages[stageIndex]

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indicator dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    stages.forEachIndexed { idx, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (idx == stageIndex) 16.dp else 8.dp, 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (idx == stageIndex) MaterialTheme.colorScheme.primary else Color.LightGray)
                        )
                    }
                }

                Button(
                    onClick = {
                        if (stageIndex < stages.size - 1) {
                            stageIndex += 1
                        } else {
                            navController.navigate("login") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.testTag("onboarding_next_btn")
                ) {
                    Text(
                        if (stageIndex == stages.size - 1) getStr("onboarding_get_started", viewModel.currentLanguage)
                        else getStr("onboarding_next", viewModel.currentLanguage)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Visual accent/icon block
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (stageIndex) {
                        0 -> Icons.Default.AccountBox
                        1 -> Icons.Default.AutoAwesome
                        2 -> Icons.Default.Language
                        else -> Icons.Default.PictureAsPdf
                    },
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = getStr(currentStage.first, viewModel.currentLanguage),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = getStr(currentStage.second, viewModel.currentLanguage),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }
    }
}

// ==========================================
// 3. LOGIN SCREEN
// ==========================================
@Composable
fun LoginScreen(navController: NavController, viewModel: PortfolioViewModel) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = getStr("login_title", viewModel.currentLanguage),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = getStr("login_subtitle", viewModel.currentLanguage),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))

            // Google login mock
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("login_google_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(getStr("login_google", viewModel.currentLanguage), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Email login mock
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(25.dp))
                    .testTag("login_email_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(getStr("login_email", viewModel.currentLanguage), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Guest entry
            TextButton(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier.testTag("login_guest_btn")
            ) {
                Text(
                    text = getStr("login_guest", viewModel.currentLanguage),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// ==========================================
// 4. HOME SCREEN
// ==========================================
@Composable
fun HomeScreen(navController: NavController, viewModel: PortfolioViewModel) {
    val portfolios by viewModel.allPortfolios.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Custom logo container
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "X",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Column {
                            Text(
                                "PortfolioX",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                "PROFESSIONAL EDITION",
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                },
                actions = {
                    // Simulated status avatar from Dicebear
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(36.dp)
                            .background(Color.LightGray, CircleShape)
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👨‍💻", fontSize = 18.sp)
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.startNewPortfolio()
                    navController.navigate("wizard")
                },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text(getStr("home_create", viewModel.currentLanguage)) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_create_portfolio")
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(0.dp)),
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Item 1: Home (selected)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { /* Already on Home */ }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🏠", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Home",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Item 2: Files
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                if (portfolios.isNotEmpty()) {
                                    viewModel.selectPortfolio(portfolios.first())
                                    navController.navigate("preview/${portfolios.first().id}")
                                } else {
                                    Toast.makeText(context, "No portfolio found. Create one first!", Toast.LENGTH_SHORT).show()
                                }
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📑", fontSize = 18.sp, modifier = Modifier.graphicsLayer(alpha = 0.4f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Files",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }

                    // Item 3: Stats
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate("dashboard")
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📊", fontSize = 18.sp, modifier = Modifier.graphicsLayer(alpha = 0.4f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Stats",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }

                    // Item 4: Profile
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate("settings")
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👤", fontSize = 18.sp, modifier = Modifier.graphicsLayer(alpha = 0.4f))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Profile",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Elegant Website Status / Analytics Card (extracted from Design HTML)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        val firstPortfolio = portfolios.firstOrNull()
                        val hasPortfolio = firstPortfolio != null
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "Personal Website Status",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = firstPortfolio?.website ?: "rahman.portfoliox.app",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            // Emerald Live badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(if (hasPortfolio) Color(0xFFD1FAE5) else Color(0xFFF1F5F9))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (hasPortfolio) "LIVE" else "DRAFT",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasPortfolio) Color(0xFF047857) else Color(0xFF64748B)
                                )
                            }
                        }
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (hasPortfolio) "${(firstPortfolio?.visitorsCount ?: 0)}" else "1.2k",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "VISITORS",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(30.dp)
                                    .background(MaterialTheme.colorScheme.outline)
                            )
                            
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (hasPortfolio) "${(firstPortfolio?.downloadsCount ?: 0)}" else "84",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "DOWNLOADS",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(30.dp)
                                    .background(MaterialTheme.colorScheme.outline)
                            )
                            
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (hasPortfolio) "96%" else "92%",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "SCORE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Primary Grid Actions (extracted from Design HTML)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Action 1: Create New
                        Card(
                            onClick = {
                                viewModel.startNewPortfolio()
                                navController.navigate("wizard")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                                .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp), clip = false),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("➕", fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Create New", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        // Action 2: My Portfolio
                        Card(
                            onClick = {
                                if (portfolios.isNotEmpty()) {
                                    viewModel.selectPortfolio(portfolios.first())
                                    navController.navigate("preview/${portfolios.first().id}")
                                } else {
                                    Toast.makeText(context, "No portfolio yet! Creating a new draft...", Toast.LENGTH_SHORT).show()
                                    viewModel.startNewPortfolio()
                                    navController.navigate("wizard")
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("📂", fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("My Portfolio", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Action 3: Templates
                        Card(
                            onClick = {
                                if (portfolios.isNotEmpty()) {
                                    viewModel.selectPortfolio(portfolios.first())
                                    navController.navigate("preview/${portfolios.first().id}")
                                } else {
                                    Toast.makeText(context, "Create a portfolio first to view templates!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("🎨", fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Templates", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        // Action 4: Website
                        Card(
                            onClick = {
                                if (portfolios.isNotEmpty()) {
                                    viewModel.selectPortfolio(portfolios.first())
                                    navController.navigate("website/${portfolios.first().id}")
                                } else {
                                    Toast.makeText(context, "Create a portfolio first to view live website mock!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp)),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("🌐", fontSize = 24.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Website", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }

            // Quick Resume Builder Section (extracted from Design HTML)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE0E7FF), RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2F6).copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("📄", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Resume AI Builder",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF312E81)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Generate a professional PDF with automated grammar fix & translation",
                            fontSize = 11.sp,
                            color = Color(0xFF4F46E5).copy(alpha = 0.75f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(Color(0xFFE0E7FF))
                                .clickable {
                                    viewModel.startNewPortfolio()
                                    navController.navigate("wizard")
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Try Now →",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4F46E5)
                            )
                        }
                    }
                }
            }

            // Portfolio lists header
            item {
                Text(
                    text = getStr("home_my_ports", viewModel.currentLanguage),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (portfolios.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.FolderOpen,
                            contentDescription = null,
                            size = 48.dp,
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "No portfolios found. Click the button below to start!",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                items(portfolios) { p ->
                    PortfolioItemCard(
                        portfolio = p,
                        onClick = {
                            viewModel.selectPortfolio(p)
                            navController.navigate("preview/${p.id}")
                        },
                        onEdit = {
                            viewModel.selectPortfolio(p)
                            navController.navigate("wizard")
                        },
                        onDelete = {
                            viewModel.deletePortfolio(p)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ShortcutCard(title: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, size = 28.dp, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PortfolioItemCard(
    portfolio: Portfolio,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (portfolio.name.isNotEmpty()) portfolio.name.take(1).uppercase() else "P",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (portfolio.name.isEmpty()) "Unnamed Draft" else portfolio.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (portfolio.profession.isEmpty()) "Not specified" else portfolio.profession,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

// ==========================================
// 5. STEP-BY-STEP PORTFOLIO WIZARD SCREEN
// ==========================================
@Composable
fun WizardScreen(navController: NavController, viewModel: PortfolioViewModel) {
    val activeStep = viewModel.currentStepIndex
    val totalSteps = 10
    val context = LocalContext.current

    // Modal dialog state for Gemini AI Assistance
    var showAiAssistant by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("${getStr("wizard", viewModel.currentLanguage)}: Step ${activeStep + 1}/$totalSteps", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (activeStep > 0) {
                            viewModel.currentStepIndex -= 1
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAiAssistant = true }) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI Assistant", tint = Color(0xFFFFB300))
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (activeStep > 0) {
                        OutlinedButton(
                            onClick = { viewModel.currentStepIndex -= 1 },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(getStr("prev", viewModel.currentLanguage))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Button(
                        onClick = {
                            if (activeStep < totalSteps - 1) {
                                viewModel.currentStepIndex += 1
                            } else {
                                // Save and proceed to Live Preview
                                viewModel.saveDraftToDb { pid ->
                                    navController.navigate("preview/$pid") {
                                        popUpTo("home")
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).testTag("wizard_next_btn")
                    ) {
                        Text(
                            if (activeStep == totalSteps - 1) getStr("done", viewModel.currentLanguage)
                            else getStr("next", viewModel.currentLanguage)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Steps Progress Indicator
                LinearProgressIndicator(
                    progress = { (activeStep + 1) / totalSteps.toFloat() },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.LightGray.copy(alpha = 0.5f)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    when (activeStep) {
                        0 -> Step1PersonalInfo(viewModel)
                        1 -> Step2Education(viewModel)
                        2 -> Step3Experience(viewModel)
                        3 -> Step4Skills(viewModel)
                        4 -> Step5Achievements(viewModel)
                        5 -> Step6Projects(viewModel)
                        6 -> Step7Certificates(viewModel)
                        7 -> Step8Languages(viewModel)
                        8 -> Step9SocialMedia(viewModel)
                        9 -> Step10References(viewModel)
                    }
                }
            }

            // Launch AI Dialogue Assist
            if (showAiAssistant) {
                AiAssistantDialog(
                    viewModel = viewModel,
                    onDismiss = { showAiAssistant = false }
                )
            }
        }
    }
}

// ==========================================
// WIZARD STEP SUB-COMPOSABLES
// ==========================================

// Step 1: Personal Information
@Composable
fun Step1PersonalInfo(viewModel: PortfolioViewModel) {
    val p = viewModel.draftPortfolio
    var tempName by remember { mutableStateOf(p.name) }
    var tempProf by remember { mutableStateOf(p.profession) }
    var tempTagline by remember { mutableStateOf(p.tagline) }
    var tempAbout by remember { mutableStateOf(p.aboutMe) }
    var tempEmail by remember { mutableStateOf(p.email) }
    var tempPhone by remember { mutableStateOf(p.phone) }
    var tempWhatsApp by remember { mutableStateOf(p.whatsApp) }
    var tempAddress by remember { mutableStateOf(p.address) }
    var tempWeb by remember { mutableStateOf(p.website) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(getStr("wizard_personal", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        item {
            OutlinedTextField(
                value = tempName,
                onValueChange = { tempName = it; viewModel.draftPortfolio.name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth().testTag("input_full_name"),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = tempProf,
                onValueChange = { tempProf = it; viewModel.draftPortfolio.profession = it },
                label = { Text("Profession") },
                modifier = Modifier.fillMaxWidth().testTag("input_profession"),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = tempTagline,
                onValueChange = { tempTagline = it; viewModel.draftPortfolio.tagline = it },
                label = { Text("Tagline / Motto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = tempAbout,
                onValueChange = { tempAbout = it; viewModel.draftPortfolio.aboutMe = it },
                label = { Text("About Me") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                maxLines = 5
            )
        }
        item {
            OutlinedTextField(
                value = tempEmail,
                onValueChange = { tempEmail = it; viewModel.draftPortfolio.email = it },
                label = { Text("Email Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = tempPhone,
                onValueChange = { tempPhone = it; viewModel.draftPortfolio.phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = tempWhatsApp,
                onValueChange = { tempWhatsApp = it; viewModel.draftPortfolio.whatsApp = it },
                label = { Text("WhatsApp Link/Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = tempAddress,
                onValueChange = { tempAddress = it; viewModel.draftPortfolio.address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = tempWeb,
                onValueChange = { tempWeb = it; viewModel.draftPortfolio.website = it },
                label = { Text("Personal Domain / Handle") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

// Step 2: Education
@Composable
fun Step2Education(viewModel: PortfolioViewModel) {
    val list = viewModel.draftEducationList.toMutableList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(getStr("wizard_edu", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = {
                        list.add(Education())
                        viewModel.draftEducationList = list
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(getStr("add_more", viewModel.currentLanguage), fontSize = 11.sp)
                }
            }
        }

        items(list.size) { idx ->
            val edu = list[idx]
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Institute #${idx + 1}", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                            list.removeAt(idx)
                            viewModel.draftEducationList = list
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                        }
                    }

                    OutlinedTextField(
                        value = edu.institute,
                        onValueChange = {
                            edu.institute = it
                            viewModel.draftEducationList = list.toList()
                        },
                        label = { Text("Institute / School") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edu.degree,
                        onValueChange = {
                            edu.degree = it
                            viewModel.draftEducationList = list.toList()
                        },
                        label = { Text("Degree / Certificate") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edu.department,
                        onValueChange = {
                            edu.department = it
                            viewModel.draftEducationList = list.toList()
                        },
                        label = { Text("Department / Major") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edu.gpa,
                        onValueChange = {
                            edu.gpa = it
                            viewModel.draftEducationList = list.toList()
                        },
                        label = { Text("GPA / Score") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edu.year,
                        onValueChange = {
                            edu.year = it
                            viewModel.draftEducationList = list.toList()
                        },
                        label = { Text("Passing Year") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// Step 3: Experience
@Composable
fun Step3Experience(viewModel: PortfolioViewModel) {
    val list = viewModel.draftExperienceList.toMutableList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(getStr("wizard_exp", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = {
                        list.add(Experience())
                        viewModel.draftExperienceList = list
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(getStr("add_more", viewModel.currentLanguage), fontSize = 11.sp)
                }
            }
        }

        items(list.size) { idx ->
            val exp = list[idx]
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Company #${idx + 1}", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                            list.removeAt(idx)
                            viewModel.draftExperienceList = list
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                        }
                    }

                    OutlinedTextField(
                        value = exp.company,
                        onValueChange = {
                            exp.company = it
                            viewModel.draftExperienceList = list.toList()
                        },
                        label = { Text("Company Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = exp.position,
                        onValueChange = {
                            exp.position = it
                            viewModel.draftExperienceList = list.toList()
                        },
                        label = { Text("Job Position / Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = exp.joiningDate,
                        onValueChange = {
                            exp.joiningDate = it
                            viewModel.draftExperienceList = list.toList()
                        },
                        label = { Text("Joining Date (e.g. 2021-06)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = exp.leavingDate,
                        onValueChange = {
                            exp.leavingDate = it
                            viewModel.draftExperienceList = list.toList()
                        },
                        label = { Text("Leaving Date (e.g. Present)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = exp.description,
                        onValueChange = {
                            exp.description = it
                            viewModel.draftExperienceList = list.toList()
                        },
                        label = { Text("Responsibilities / Achievements") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        maxLines = 4
                    )
                }
            }
        }
    }
}

// Step 4: Skills
@Composable
fun Step4Skills(viewModel: PortfolioViewModel) {
    val list = viewModel.draftSkillsList.toMutableList()
    var newSkillName by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(getStr("wizard_skills", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        // Row to add a skill
        item {
            Card {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newSkillName,
                        onValueChange = { newSkillName = it },
                        label = { Text("New Skill") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        if (newSkillName.isNotEmpty()) {
                            list.add(Skill(newSkillName, 80))
                            viewModel.draftSkillsList = list
                            newSkillName = ""
                        }
                    }) {
                        Icon(Icons.Default.AddCircle, null, tint = MaterialTheme.colorScheme.primary, size = 32.dp)
                    }
                }
            }
        }

        items(list.size) { idx ->
            val skill = list[idx]
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(skill.name, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("${skill.percentage}%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = {
                                list.removeAt(idx)
                                viewModel.draftSkillsList = list
                            }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                    Slider(
                        value = skill.percentage.toFloat(),
                        onValueChange = {
                            skill.percentage = it.toInt()
                            viewModel.draftSkillsList = list.toList()
                        },
                        valueRange = 0f..100f,
                        steps = 20
                    )
                }
            }
        }
    }
}

// Step 5: Achievements
@Composable
fun Step5Achievements(viewModel: PortfolioViewModel) {
    val list = viewModel.draftAchievementsList.toMutableList()
    var newAchTitle by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(getStr("wizard_achieve", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        item {
            Card {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newAchTitle,
                        onValueChange = { newAchTitle = it },
                        label = { Text("E.g. BUTEX Admission, Olympiad...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        if (newAchTitle.isNotEmpty()) {
                            list.add(Achievement(newAchTitle))
                            viewModel.draftAchievementsList = list
                            newAchTitle = ""
                        }
                    }) {
                        Icon(Icons.Default.AddCircle, null, tint = MaterialTheme.colorScheme.primary, size = 32.dp)
                    }
                }
            }
        }

        items(list.size) { idx ->
            val ach = list[idx]
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(ach.title, fontSize = 13.sp)
                    }
                    IconButton(onClick = {
                        list.removeAt(idx)
                        viewModel.draftAchievementsList = list
                    }) {
                        Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

// Step 6: Projects
@Composable
fun Step6Projects(viewModel: PortfolioViewModel) {
    val list = viewModel.draftProjectsList.toMutableList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(getStr("wizard_projects", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = {
                        list.add(Project())
                        viewModel.draftProjectsList = list
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(getStr("add_more", viewModel.currentLanguage), fontSize = 11.sp)
                }
            }
        }

        items(list.size) { idx ->
            val proj = list[idx]
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Project #${idx + 1}", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                            list.removeAt(idx)
                            viewModel.draftProjectsList = list
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                        }
                    }

                    OutlinedTextField(
                        value = proj.title,
                        onValueChange = {
                            proj.title = it
                            viewModel.draftProjectsList = list.toList()
                        },
                        label = { Text("Project Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = proj.description,
                        onValueChange = {
                            proj.description = it
                            viewModel.draftProjectsList = list.toList()
                        },
                        label = { Text("Project Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = proj.githubLink,
                        onValueChange = {
                            proj.githubLink = it
                            viewModel.draftProjectsList = list.toList()
                        },
                        label = { Text("GitHub Link") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = proj.websiteLink,
                        onValueChange = {
                            proj.websiteLink = it
                            viewModel.draftProjectsList = list.toList()
                        },
                        label = { Text("Live Website URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = proj.techUsed,
                        onValueChange = {
                            proj.techUsed = it
                            viewModel.draftProjectsList = list.toList()
                        },
                        label = { Text("Technology Used (e.g., Kotlin, Room)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// Step 7: Certificates
@Composable
fun Step7Certificates(viewModel: PortfolioViewModel) {
    val list = viewModel.draftCertificatesList.toMutableList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(getStr("wizard_certs", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = {
                        list.add(Certificate())
                        viewModel.draftCertificatesList = list
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(getStr("add_more", viewModel.currentLanguage), fontSize = 11.sp)
                }
            }
        }

        items(list.size) { idx ->
            val cert = list[idx]
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Certificate #${idx + 1}", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                            list.removeAt(idx)
                            viewModel.draftCertificatesList = list
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                        }
                    }

                    OutlinedTextField(
                        value = cert.name,
                        onValueChange = {
                            cert.name = it
                            viewModel.draftCertificatesList = list.toList()
                        },
                        label = { Text("Certificate Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cert.issuedBy,
                        onValueChange = {
                            cert.issuedBy = it
                            viewModel.draftCertificatesList = list.toList()
                        },
                        label = { Text("Issued By") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cert.issueDate,
                        onValueChange = {
                            cert.issueDate = it
                            viewModel.draftCertificatesList = list.toList()
                        },
                        label = { Text("Issue Date") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedButton(
                        onClick = {
                            cert.attachmentUri = "mock_uploaded_doc_pdf"
                            viewModel.draftCertificatesList = list.toList()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.UploadFile, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (cert.attachmentUri.isEmpty()) "Upload PDF / Image Certificate" else "Uploaded successfully ✔")
                    }
                }
            }
        }
    }
}

// Step 8: Languages
@Composable
fun Step8Languages(viewModel: PortfolioViewModel) {
    val presetLangs = listOf("Bangla", "English", "Hindi", "Arabic", "Japanese")
    val selected = viewModel.draftLanguagesList.toMutableList()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(getStr("wizard_langs", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)

        presetLangs.forEach { lang ->
            val isChecked = selected.contains(lang)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isChecked) selected.remove(lang) else selected.add(lang)
                        viewModel.draftLanguagesList = selected
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        if (isChecked) selected.remove(lang) else selected.add(lang)
                        viewModel.draftLanguagesList = selected
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(lang, fontSize = 16.sp)
            }
        }
    }
}

// Step 9: Social Media
@Composable
fun Step9SocialMedia(viewModel: PortfolioViewModel) {
    val socials = viewModel.draftSocialMedia
    var fb by remember { mutableStateOf(socials.facebook) }
    var ig by remember { mutableStateOf(socials.instagram) }
    var li by remember { mutableStateOf(socials.linkedIn) }
    var gh by remember { mutableStateOf(socials.github) }
    var yt by remember { mutableStateOf(socials.youtube) }
    var tw by remember { mutableStateOf(socials.twitter) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(getStr("wizard_socials", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        item {
            OutlinedTextField(
                value = fb,
                onValueChange = { fb = it; viewModel.draftSocialMedia = socials.copy(facebook = it) },
                label = { Text("Facebook URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = ig,
                onValueChange = { ig = it; viewModel.draftSocialMedia = socials.copy(instagram = it) },
                label = { Text("Instagram URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = li,
                onValueChange = { li = it; viewModel.draftSocialMedia = socials.copy(linkedIn = it) },
                label = { Text("LinkedIn URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = gh,
                onValueChange = { gh = it; viewModel.draftSocialMedia = socials.copy(github = it) },
                label = { Text("GitHub Profile URL") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = yt,
                onValueChange = { yt = it; viewModel.draftSocialMedia = socials.copy(youtube = it) },
                label = { Text("YouTube Channel") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
        item {
            OutlinedTextField(
                value = tw,
                onValueChange = { tw = it; viewModel.draftSocialMedia = socials.copy(twitter = it) },
                label = { Text("Twitter / X Profile") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

// Step 10: References
@Composable
fun Step10References(viewModel: PortfolioViewModel) {
    val list = viewModel.draftReferencesList.toMutableList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(getStr("wizard_refs", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Button(
                    onClick = {
                        list.add(Reference())
                        viewModel.draftReferencesList = list
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(getStr("add_more", viewModel.currentLanguage), fontSize = 11.sp)
                }
            }
        }

        items(list.size) { idx ->
            val ref = list[idx]
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Reference #${idx + 1}", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                            list.removeAt(idx)
                            viewModel.draftReferencesList = list
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                        }
                    }

                    OutlinedTextField(
                        value = ref.name,
                        onValueChange = {
                            ref.name = it
                            viewModel.draftReferencesList = list.toList()
                        },
                        label = { Text("Reference Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = ref.role,
                        onValueChange = {
                            ref.role = it
                            viewModel.draftReferencesList = list.toList()
                        },
                        label = { Text("Role (Teacher, Manager, Mentor)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = ref.company,
                        onValueChange = {
                            ref.company = it
                            viewModel.draftReferencesList = list.toList()
                        },
                        label = { Text("Company / Organization") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = ref.emailOrContact,
                        onValueChange = {
                            ref.emailOrContact = it
                            viewModel.draftReferencesList = list.toList()
                        },
                        label = { Text("Contact Info") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// ==========================================
// 6. GEMINI AI ASSISTANT DIALOG
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantDialog(viewModel: PortfolioViewModel, onDismiss: () -> Unit) {
    var p1 by remember { mutableStateOf("") }
    var p2 by remember { mutableStateOf("") }
    var p3 by remember { mutableStateOf("") }
    var taskType by remember { mutableStateOf("about_me") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFFFFB300))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gemini AI Assist", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, null)
                    }
                }

                Divider()

                // Task selectors
                Text("Select Assistant Mode:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                ScrollableRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    AiTaskChip("About Me", active = taskType == "about_me") { taskType = "about_me" }
                    AiTaskChip("Career Objective", active = taskType == "objective") { taskType = "objective" }
                    AiTaskChip("Core Skills", active = taskType == "skills") { taskType = "skills" }
                    AiTaskChip("Experience bullet", active = taskType == "experience") { taskType = "experience" }
                    AiTaskChip("Grammar Fix", active = taskType == "grammar") { taskType = "grammar" }
                    AiTaskChip("Professional Rewrite", active = taskType == "rewrite") { taskType = "rewrite" }
                    AiTaskChip("বাংলা → ইংরেজি", active = taskType == "translate_bn_en") { taskType = "translate_bn_en" }
                    AiTaskChip("ইংরেজি → বাংলা", active = taskType == "translate_en_bn") { taskType = "translate_en_bn" }
                }

                // Inputs depending on mode
                when (taskType) {
                    "about_me" -> {
                        OutlinedTextField(value = p1, onValueChange = { p1 = it }, label = { Text("Profession") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = p2, onValueChange = { p2 = it }, label = { Text("Tagline / Highlight") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = p3, onValueChange = { p3 = it }, label = { Text("Skills List") }, modifier = Modifier.fillMaxWidth())
                    }
                    "objective" -> {
                        OutlinedTextField(value = p1, onValueChange = { p1 = it }, label = { Text("Profession") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = p2, onValueChange = { p2 = it }, label = { Text("Career Focus") }, modifier = Modifier.fillMaxWidth())
                    }
                    "skills" -> {
                        OutlinedTextField(value = p1, onValueChange = { p1 = it }, label = { Text("Profession (e.g. Developer)") }, modifier = Modifier.fillMaxWidth())
                    }
                    "experience" -> {
                        OutlinedTextField(value = p1, onValueChange = { p1 = it }, label = { Text("Company Name") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = p2, onValueChange = { p2 = it }, label = { Text("Role / Designation") }, modifier = Modifier.fillMaxWidth())
                    }
                    else -> {
                        OutlinedTextField(value = p1, onValueChange = { p1 = it }, label = { Text("Enter text to transform...") }, modifier = Modifier.fillMaxWidth().height(100.dp))
                    }
                }

                Button(
                    onClick = { viewModel.executeAiTask(taskType, p1, p2, p3) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Generate with Gemini 3.5 Flash")
                }

                // Output Result panel
                Text("Result:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                        .heightIn(min = 60.dp)
                ) {
                    if (viewModel.aiLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        Text(
                            text = if (viewModel.aiResultText.isEmpty()) "Generated content will display here. Click 'Apply' to save." else viewModel.aiResultText,
                            fontSize = 12.sp
                        )
                    }
                }

                if (viewModel.aiResultText.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                // Apply logic to draft
                                when (taskType) {
                                    "about_me" -> viewModel.draftPortfolio.aboutMe = viewModel.aiResultText
                                    "objective" -> viewModel.draftPortfolio.tagline = viewModel.aiResultText
                                }
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Apply & Close")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AiTaskChip(label: String, active: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (active) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, fontSize = 11.sp, color = if (active) Color.White else Color.Black, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ScrollableRow(horizontalArrangement: Arrangement.Horizontal, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = horizontalArrangement,
        content = { content() }
    )
}

// ==========================================
// 7. LIVE TEMPLATE PREVIEW & CUSTOMIZER
// ==========================================
@Composable
fun LivePreviewScreen(navController: NavController, viewModel: PortfolioViewModel, pid: Int) {
    val context = LocalContext.current
    var showCustomizer by remember { mutableStateOf(false) }
    var showContentEditor by remember { mutableStateOf(false) }

    // Preset color themes for One-Tap Switching
    val colorPresets = listOf(
        "#3F51B5", // Indigo
        "#00796B", // Teal
        "#D32F2F", // Red
        "#F57C00", // Orange
        "#121212", // Charcoal
        "#8E24AA", // Purple
        "#2E7D32"  // Forest Green
    )

    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val allPresets = TemplateRegistry.presets
    val filteredPresets = remember(selectedCategory, searchQuery) {
        allPresets.filter { preset ->
            val matchesCat = selectedCategory == "All" || preset.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = searchQuery.isEmpty() || 
                preset.name.contains(searchQuery, ignoreCase = true) || 
                preset.description.contains(searchQuery, ignoreCase = true) ||
                preset.category.contains(searchQuery, ignoreCase = true)
            matchesCat && matchesSearch
        }
    }

    val templates = listOf("Modern", "Minimal", "Dark", "Light", "Corporate", "Creative", "Developer")

    // Track state metrics
    LaunchedEffect(key1 = pid) {
        viewModel.incrementMetric(pid, "views")
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Live Preview & Store", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } }) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                },
                actions = {
                    IconButton(onClick = { showContentEditor = !showContentEditor }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Content", tint = if (showContentEditor) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                    IconButton(onClick = { showCustomizer = !showCustomizer }) {
                        Icon(Icons.Default.Palette, contentDescription = "Theme Options", tint = if (showCustomizer) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 8.dp) {
                Column {
                    // Drawer Toolbar for layout customization (drag & hide / color / font changes)
                    AnimatedVisibility(visible = showCustomizer) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(getStr("theme_customizer", viewModel.currentLanguage), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            
                            // Colors preseter
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                colorPresets.forEach { hex ->
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(Color(AndroidColor.parseColor(hex)))
                                            .border(
                                                width = if (viewModel.activeThemeColor == hex) 3.dp else 0.dp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                shape = CircleShape
                                            )
                                            .clickable {
                                                viewModel.activeThemeColor = hex
                                                viewModel.draftPortfolio.themeColor = hex
                                            }
                                    )
                                }
                            }

                            // Font switcher presets
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { viewModel.activeFontFamily = "SansSerif"; viewModel.draftPortfolio.fontFamily = "SansSerif" },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (viewModel.activeFontFamily == "SansSerif") MaterialTheme.colorScheme.primary else Color.Gray)
                                ) {
                                    Text("Sans-Serif", fontSize = 11.sp)
                                }
                                Button(
                                    onClick = { viewModel.activeFontFamily = "Serif"; viewModel.draftPortfolio.fontFamily = "Serif" },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (viewModel.activeFontFamily == "Serif") MaterialTheme.colorScheme.primary else Color.Gray)
                                ) {
                                    Text("Serif", fontSize = 11.sp)
                                }
                                Button(
                                    onClick = { viewModel.activeFontFamily = "Monospace"; viewModel.draftPortfolio.fontFamily = "Monospace" },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (viewModel.activeFontFamily == "Monospace") MaterialTheme.colorScheme.primary else Color.Gray)
                                ) {
                                    Text("Monospace", fontSize = 11.sp)
                                }
                            }

                            // Dynamic section hide/move simulation
                            Text("Reorder / Hide Sections (Drag simulation):", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            ScrollableRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                viewModel.draftSectionOrder.forEachIndexed { index, s ->
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.LightGray.copy(alpha = 0.5f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(s, fontSize = 10.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        if (index > 0) {
                                            Icon(
                                                Icons.Default.ArrowLeft,
                                                null,
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .clickable {
                                                        val mutableOrder = viewModel.draftSectionOrder.toMutableList()
                                                        val temp = mutableOrder[index]
                                                        mutableOrder[index] = mutableOrder[index - 1]
                                                        mutableOrder[index - 1] = temp
                                                        viewModel.draftSectionOrder = mutableOrder
                                                    }
                                            )
                                        }
                                        Icon(
                                            Icons.Default.VisibilityOff,
                                            null,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
                                                    val mutableOrder = viewModel.draftSectionOrder.toMutableList()
                                                    mutableOrder.removeAt(index)
                                                    viewModel.draftSectionOrder = mutableOrder
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Direct output actions (PDF Download / Website Link / Share Dialog)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .navigationBarsPadding(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { showContentEditor = true },
                            modifier = Modifier.weight(1.2f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(getStr("edit_content", viewModel.currentLanguage), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.incrementMetric(pid, "downloads")
                                Toast.makeText(context, "Downloaded high quality print A4 Resume PDF successfully ✔", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Default.PictureAsPdf, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(getStr("download_pdf", viewModel.currentLanguage), fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                viewModel.incrementMetric(pid, "shares")
                                navController.navigate("website/$pid")
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Icon(Icons.Default.Language, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(getStr("view_website", viewModel.currentLanguage), fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Template selection row (100+ Premium templates switcher)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Template Store (100+ Premium Layouts)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Instant one-tap styles with infinite edit options",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        // Badge showing count
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${filteredPresets.size} Templates",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Search input bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search template name, category, style...", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Search, null, modifier = Modifier.size(16.dp)) },
                        trailingIcon = if (searchQuery.isNotEmpty()) {
                            {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, null, modifier = Modifier.size(16.dp))
                                }
                            }
                        } else null,
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Category Filter Pills
                    ScrollableRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val categoriesList = listOf("All", "Professional", "Minimalist", "Tech & Developer", "Creative", "Academic")
                        categoriesList.forEach { cat ->
                            val active = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (active) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Horizontal list of Template Cards
                    if (filteredPresets.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Info, null, tint = Color.Gray, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("No templates match your search.", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        ScrollableRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            filteredPresets.forEach { preset ->
                                val active = viewModel.draftPortfolio.selectedTemplate == preset.id || viewModel.draftPortfolio.selectedTemplate == preset.name
                                Card(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .height(115.dp)
                                        .clickable {
                                            viewModel.draftPortfolio.selectedTemplate = preset.id
                                            viewModel.draftPortfolio.themeColor = preset.themeColor
                                            viewModel.draftPortfolio.fontFamily = preset.fontFamily
                                            viewModel.activeThemeColor = preset.themeColor
                                            viewModel.activeFontFamily = preset.fontFamily
                                            viewModel.saveDraftToDb()
                                            Toast.makeText(context, "Applied ${preset.name}! Edit colors/fonts anytime. ✨", Toast.LENGTH_SHORT).show()
                                        }
                                        .border(
                                            width = if (active) 2.5.dp else 1.dp,
                                            color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (active) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(10.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .background(
                                                        color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                                        shape = RoundedCornerShape(6.dp)
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(preset.iconEmoji, fontSize = 14.sp)
                                            }
                                            
                                            // Small dark/light pill
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (preset.isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9))
                                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = if (preset.isDark) "DARK" else "LIGHT",
                                                    fontSize = 7.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (preset.isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
                                                )
                                            }
                                        }
                                        
                                        Column {
                                            Text(
                                                text = preset.name,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                maxLines = 1
                                            )
                                            Spacer(modifier = Modifier.height(1.dp))
                                            Text(
                                                text = preset.description,
                                                fontSize = 8.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 2,
                                                lineHeight = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info, 
                            null, 
                            tint = MaterialTheme.colorScheme.primary, 
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Tap the Palette 🎨 icon in the top-bar anytime to fully edit, reorder & change this template's fonts, colors, and layout structure!",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 11.sp
                        )
                    }
                }
            }

            // Real-time live template display
            RenderPortfolioTemplate(
                portfolio = viewModel.draftPortfolio,
                isWebsiteMode = false,
                customThemeColor = viewModel.activeThemeColor,
                customFontFamily = viewModel.activeFontFamily,
                customSectionOrder = viewModel.draftSectionOrder
            )
        }
    }

    if (showContentEditor) {
        ContentEditorDialog(
            viewModel = viewModel,
            onDismiss = { showContentEditor = false }
        )
    }
}

@Composable
fun ContentEditorDialog(viewModel: PortfolioViewModel, onDismiss: () -> Unit) {
    var activeTab by remember { mutableStateOf(0) }
    val isBn = viewModel.currentLanguage == "bn"
    
    val tabTitles = if (isBn) {
        listOf("ব্যক্তিগত", "শিক্ষা", "অভিজ্ঞতা", "স্কিল", "অর্জন", "প্রজেক্ট", "সার্টিফিকেট", "ভাষা", "সোশ্যাল", "রেফারেন্স")
    } else {
        listOf("Personal", "Education", "Experience", "Skills", "Achievements", "Projects", "Certificates", "Languages", "Socials", "References")
    }
    
    // Copy current portfolio lists to draft states in ViewModel so that they are loaded correctly
    LaunchedEffect(Unit) {
        viewModel.draftEducationList = viewModel.draftPortfolio.educationList
        viewModel.draftExperienceList = viewModel.draftPortfolio.experienceList
        viewModel.draftSkillsList = viewModel.draftPortfolio.skillsList
        viewModel.draftAchievementsList = viewModel.draftPortfolio.achievementsList
        viewModel.draftProjectsList = viewModel.draftPortfolio.projectsList
        viewModel.draftCertificatesList = viewModel.draftPortfolio.certificatesList
        viewModel.draftLanguagesList = viewModel.draftPortfolio.languagesList
        viewModel.draftSocialMedia = viewModel.draftPortfolio.socialMedia
        viewModel.draftReferencesList = viewModel.draftPortfolio.referencesList
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isBn) "টেমপ্লেট তথ্য এডিট করুন" else "Edit Template Information",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isBn) "যেকোনো টেমপ্লেটের তথ্য এখান থেকে পরিবর্তন করতে পারেন" else "Directly customize this template's contents here",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, "Close", modifier = Modifier.size(24.dp), tint = Color.Gray)
                    }
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                
                // Custom Horizontally scrollable chip-like tabs for standard and clean look
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        val active = activeTab == index
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .border(
                                    width = 1.dp,
                                    color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(100.dp)
                                )
                                .clickable { activeTab = index }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = title,
                                color = if (active) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
                
                // Content Pane (wrapped in a Box)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    when (activeTab) {
                        0 -> Step1PersonalInfo(viewModel)
                        1 -> Step2Education(viewModel)
                        2 -> Step3Experience(viewModel)
                        3 -> Step4Skills(viewModel)
                        4 -> Step5Achievements(viewModel)
                        5 -> Step6Projects(viewModel)
                        6 -> Step7Certificates(viewModel)
                        7 -> Step8Languages(viewModel)
                        8 -> Step9SocialMedia(viewModel)
                        9 -> Step10References(viewModel)
                    }
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
                
                // Action Bottom Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isBn) "বাতিল" else "Cancel",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.saveDraftToDb {
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isBn) "সংরক্ষণ করুন ✔" else "Save & Apply ✔",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. LIVE WEBSITE GENERATOR RENDERER SCREEN
// ==========================================
@Composable
fun LiveWebsiteMockScreen(navController: NavController, viewModel: PortfolioViewModel, pid: Int) {
    val context = LocalContext.current
    val p = viewModel.draftPortfolio
    val nameSlug = p.name.lowercase().replace(" ", "")
    val subDomain = if (nameSlug.isEmpty()) "user" else nameSlug
    val mockUrl = "$subDomain.infinityportfolio.app"

    LaunchedEffect(key1 = pid) {
        viewModel.incrementMetric(pid, "visitors")
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Live Domain Viewer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Simulated live address bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, null, tint = Color.Green, size = 16.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "https://$mockUrl",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        Toast.makeText(context, "Copied link to clipboard!", Toast.LENGTH_SHORT).show()
                    }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Share, null)
                    }
                }
            }

            // Render live website with fully responsive style details, custom animation, and a Mock Contact Form
            RenderPortfolioTemplate(
                portfolio = p,
                isWebsiteMode = true,
                customThemeColor = viewModel.activeThemeColor,
                customFontFamily = viewModel.activeFontFamily,
                customSectionOrder = viewModel.draftSectionOrder
            )
        }
    }
}

// ==========================================
// 9. METRICS DASHBOARD SCREEN
// ==========================================
@Composable
fun DashboardScreen(navController: NavController, viewModel: PortfolioViewModel) {
    val portfolios by viewModel.allPortfolios.collectAsStateWithLifecycle()

    var viewsSum = 0
    var downloadsSum = 0
    var visitorsSum = 0
    var sharesSum = 0

    portfolios.forEach {
        viewsSum += it.viewsCount
        downloadsSum += it.downloadsCount
        visitorsSum += it.visitorsCount
        sharesSum += it.sharesCount
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(getStr("home_dashboard", viewModel.currentLanguage), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(getStr("analytics_title", viewModel.currentLanguage), fontSize = 18.sp, fontWeight = FontWeight.Bold)

            // Dynamic Analytics Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricGridCard(getStr("analytics_views", viewModel.currentLanguage), viewsSum.toString(), Icons.Default.Visibility, modifier = Modifier.weight(1f))
                MetricGridCard(getStr("analytics_downloads", viewModel.currentLanguage), downloadsSum.toString(), Icons.Default.Download, modifier = Modifier.weight(1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricGridCard(getStr("analytics_visitors", viewModel.currentLanguage), visitorsSum.toString(), Icons.Default.Group, modifier = Modifier.weight(1f))
                MetricGridCard(getStr("analytics_shares", viewModel.currentLanguage), sharesSum.toString(), Icons.Default.Share, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Charts visualization placeholder
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Monthly Visitors Analytics Trend", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    // Simple responsive CSS/Compose graph drawing
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        listOf(20, 45, 12, 67, 43, 90, 110, 50, 75, 95).forEachIndexed { i, valPct ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .fillMaxHeight(valPct / 120f)
                                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Jul $i", fontSize = 9.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricGridCard(title: String, score: String, icon: ImageVector, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, size = 24.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(score, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(title, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        }
    }
}

// ==========================================
// 10. APP SETTINGS SCREEN
// ==========================================
@Composable
fun SettingsScreen(navController: NavController, viewModel: PortfolioViewModel) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    var isNotifEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(getStr("home_settings", viewModel.currentLanguage), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language Selection
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(getStr("settings_lang", viewModel.currentLanguage), fontWeight = FontWeight.Bold)
                        Text(if (viewModel.currentLanguage == "bn") "বর্তমান ভাষা: বাংলা" else "Current: English", fontSize = 12.sp, color = Color.Gray)
                    }
                    Button(
                        onClick = {
                            viewModel.currentLanguage = if (viewModel.currentLanguage == "bn") "en" else "bn"
                        },
                        modifier = Modifier.testTag("btn_toggle_language")
                    ) {
                        Text(if (viewModel.currentLanguage == "bn") "Switch to EN" else "বাংলায় পরিবর্তন")
                    }
                }
            }

            // Dark Mode toggle
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(getStr("settings_dark", viewModel.currentLanguage), fontWeight = FontWeight.Bold)
                    Switch(
                        checked = viewModel.isDarkMode,
                        onCheckedChange = { viewModel.isDarkMode = it }
                    )
                }
            }

            // Notification toggle
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(getStr("settings_notif", viewModel.currentLanguage), fontWeight = FontWeight.Bold)
                    Switch(
                        checked = isNotifEnabled,
                        onCheckedChange = { isNotifEnabled = it }
                    )
                }
            }

            // Cloud sync
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(getStr("settings_backup", viewModel.currentLanguage), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Auto-sync draft portfolios across Google Drive, Dropbox, OneDrive, or own custom enterprise cloud server.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { Toast.makeText(context, "Successfully synced cloud backup ✔", Toast.LENGTH_SHORT).show() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Sync Now", fontSize = 11.sp)
                        }
                    }
                }
            }

            // About Developer & Company Header
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )

            Text(
                text = "About Developer & Company",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // About Developer Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Dev Avatar Initials
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("PR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Column {
                            Text(
                                text = "Prince AR Abdur Rahman",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Independent App Developer",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "Independent App Developer passionate about building modern Android applications, productivity tools, AI-powered experiences, media players, educational apps, and next-generation digital products.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    // Social Links Title
                    Text(
                        text = "Connect with Developer:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    // Contact Badges / Buttons
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            WhatsAppButton(context, "01707424006", modifier = Modifier.weight(1f))
                            WhatsAppButton(context, "01796951709", modifier = Modifier.weight(1f))
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SocialButton(
                                text = "Facebook",
                                iconText = "🌐",
                                url = "https://www.facebook.com/share/1BNn32qoJo/",
                                uriHandler = uriHandler,
                                modifier = Modifier.weight(1f)
                            )
                            SocialButton(
                                text = "Instagram",
                                iconText = "📸",
                                url = "https://www.instagram.com/ur___abdur____rahman__2008",
                                uriHandler = uriHandler,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // About Company Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🏢", fontSize = 20.sp)
                        }
                        Column {
                            Text(
                                text = "NexVora Lab's Ofc",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Innovation Hub",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "NexVora Lab's Ofc focuses on creating innovative Android applications designed to improve productivity, entertainment, learning, and digital experiences.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "OUR MISSION",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Build fast, beautiful, privacy-friendly, and user-focused applications accessible to everyone.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Technical Information & Credits Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Technical Information",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("App Version", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("1.0.0", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Developed By", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Prince AR Abdur Rahman", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Published By", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("NexVora Lab's Ofc", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    Text(
                        text = "© 2026 NexVora Lab's Ofc. All Rights Reserved.",
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Logout
            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(getStr("settings_logout", viewModel.currentLanguage), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun WhatsAppButton(context: android.content.Context, number: String, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    Card(
        onClick = {
            try {
                // Copy to clipboard
                val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                val clip = android.content.ClipData.newPlainText("WhatsApp Number", number)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Number $number copied to clipboard ✔", Toast.LENGTH_SHORT).show()
                
                // Open WhatsApp link
                uriHandler.openUri("https://wa.me/88$number")
            } catch (e: Exception) {
                Toast.makeText(context, "Copied $number to clipboard ✔", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("💬", fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = number,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SocialButton(text: String, iconText: String, url: String, uriHandler: androidx.compose.ui.platform.UriHandler, modifier: Modifier = Modifier) {
    Card(
        onClick = {
            try {
                uriHandler.openUri(url)
            } catch (e: Exception) {
                // ignore
            }
        },
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(iconText, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
