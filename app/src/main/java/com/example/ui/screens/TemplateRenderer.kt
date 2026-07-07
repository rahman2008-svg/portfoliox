package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Portfolio
import org.json.JSONObject

// Helper to parse hex colors safely
fun parseColor(hex: String, default: Color = Color(0xFF3F51B5)): Color {
    return try {
        Color(AndroidColor.parseColor(hex))
    } catch (e: Exception) {
        default
    }
}

// Map font family strings
fun getFontFamily(fontName: String): FontFamily {
    return when (fontName) {
        "Serif" -> FontFamily.Serif
        "Monospace" -> FontFamily.Monospace
        else -> FontFamily.SansSerif
    }
}

// Simple deterministic QR Code representation generator for the portfolio website link
@Composable
fun SimpleQrCodeGenerator(content: String, qrSizeDp: Int = 120, color: Color = Color.Black) {
    Box(
        modifier = Modifier
            .size(qrSizeDp.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val rows = 11
            val cols = 11
            val cellW = size.width / cols
            val cellH = size.height / rows
            
            // Draw deterministic QR patterns based on content hash
            val hash = content.hashCode()
            for (r in 0 until rows) {
                for (c in 0 until cols) {
                    // Always draw finder patterns in corners
                    val isFinder = (r < 3 && c < 3) || (r < 3 && c >= cols - 3) || (r >= rows - 3 && c < 3)
                    val isFinderBorder = (r == 0 || r == 2 || c == 0 || c == 2) && (r < 3 && c < 3) ||
                                         (r == 0 || r == 2 || c == cols - 1 || c == cols - 3) && (r < 3 && c >= cols - 3) ||
                                         (r == rows - 1 || r == rows - 3 || c == 0 || c == 2) && (r >= rows - 3 && c < 3)
                    
                    val active = if (isFinder) {
                        isFinderBorder || (r == 1 && c == 1) || (r == 1 && c == cols - 2) || (r == rows - 2 && c == 1)
                    } else {
                        ((hash xor (r * 17 + c * 31)) % 3 == 0)
                    }
                    
                    if (active) {
                        drawRect(
                            color = color,
                            topLeft = androidx.compose.ui.geometry.Offset(c * cellW, r * cellH),
                            size = androidx.compose.ui.geometry.Size(cellW, cellH)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RenderPortfolioTemplate(
    portfolio: Portfolio,
    modifier: Modifier = Modifier,
    isWebsiteMode: Boolean = false,
    customThemeColor: String? = null,
    customFontFamily: String? = null,
    customSectionOrder: List<String>? = null
) {
    val template = portfolio.selectedTemplate
    val preset = com.example.data.TemplateRegistry.presets.firstOrNull { it.id == template || it.name == template }

    val themeColorHex = customThemeColor ?: portfolio.themeColor
    val brandColor = parseColor(themeColorHex)
    val fontFamilyName = customFontFamily ?: portfolio.fontFamily
    val font = getFontFamily(fontFamilyName)
    val sectionOrder = customSectionOrder ?: portfolio.sectionOrder

    // Background color based on preset or legacy templates
    val backgroundColor = when {
        preset != null -> parseColor(preset.backgroundColorHex)
        template == "Dark" -> Color(0xFF121212)
        template == "Light" -> Color(0xFFF9F9F9)
        template == "Minimal" -> Color.White
        template == "Creative" -> Color(0xFFFFFDE7)
        else -> MaterialTheme.colorScheme.surface
    }

    // Text color based on preset or legacy templates
    val textColor = when {
        preset != null -> parseColor(preset.textColorHex)
        template == "Dark" -> Color.White
        else -> Color(0xFF212121)
    }

    // Subtitle color based on preset or legacy templates
    val subtitleColor = when {
        preset != null -> parseColor(preset.subtitleColorHex)
        template == "Dark" -> Color.LightGray
        else -> Color(0xFF616161)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (preset != null && preset.id.startsWith("prem_")) {
            PremiumTemplateLayout(
                p = portfolio,
                preset = preset,
                brandColor = brandColor,
                font = font,
                textColor = textColor,
                subtitleColor = subtitleColor,
                isWebsiteMode = isWebsiteMode
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Iterating through customized section order (supports Drag & Drop/Reorder & Hide)
                sectionOrder.forEach { section ->
                    when (section) {
                    "personal" -> {
                        PersonalInfoSection(portfolio, brandColor, font, textColor, subtitleColor, template)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    "education" -> {
                        val eduList = portfolio.educationList
                        if (eduList.isNotEmpty()) {
                            SectionHeader("Education", brandColor, font, textColor)
                            eduList.forEach { edu ->
                                EducationRow(edu, brandColor, font, textColor, subtitleColor)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    "experience" -> {
                        val expList = portfolio.experienceList
                        if (expList.isNotEmpty()) {
                            SectionHeader("Experience", brandColor, font, textColor)
                            expList.forEach { exp ->
                                ExperienceRow(exp, brandColor, font, textColor, subtitleColor)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    "skills" -> {
                        val skills = portfolio.skillsList
                        if (skills.isNotEmpty()) {
                            SectionHeader("Skills", brandColor, font, textColor)
                            skills.forEach { skill ->
                                SkillRow(skill, brandColor, font, textColor, subtitleColor)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    "achievements" -> {
                        val achievements = portfolio.achievementsList
                        if (achievements.isNotEmpty()) {
                            SectionHeader("Achievements", brandColor, font, textColor)
                            achievements.forEach { ach ->
                                AchievementRow(ach, font, textColor)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    "projects" -> {
                        val projects = portfolio.projectsList
                        if (projects.isNotEmpty()) {
                            SectionHeader("Featured Projects", brandColor, font, textColor)
                            projects.forEach { proj ->
                                ProjectRow(proj, brandColor, font, textColor, subtitleColor)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    "certificates" -> {
                        val certs = portfolio.certificatesList
                        if (certs.isNotEmpty()) {
                            SectionHeader("Certificates", brandColor, font, textColor)
                            certs.forEach { cert ->
                                CertificateRow(cert, brandColor, font, textColor, subtitleColor)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    "languages" -> {
                        val langs = portfolio.languagesList
                        if (langs.isNotEmpty()) {
                            SectionHeader("Languages", brandColor, font, textColor)
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                langs.forEach { lang ->
                                    LanguageBadge(lang, brandColor, font)
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    "socials" -> {
                        val socials = portfolio.socialMedia
                        val websiteSlug = if (portfolio.name.isNotEmpty()) {
                            "${portfolio.name.lowercase().replace(" ", "")}.infinityportfolio.app"
                        } else {
                            "mysite.infinityportfolio.app"
                        }
                        
                        SectionHeader("Connect & QR Profile", brandColor, font, textColor)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                if (socials.facebook.isNotEmpty()) SocialLinkRow(Icons.Default.Link, "Facebook", socials.facebook, brandColor, textColor)
                                if (socials.instagram.isNotEmpty()) SocialLinkRow(Icons.Default.Link, "Instagram", socials.instagram, brandColor, textColor)
                                if (socials.linkedIn.isNotEmpty()) SocialLinkRow(Icons.Default.Link, "LinkedIn", socials.linkedIn, brandColor, textColor)
                                if (socials.github.isNotEmpty()) SocialLinkRow(Icons.Default.Code, "GitHub", socials.github, brandColor, textColor)
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            // Auto-generated QR Code profile representation
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                SimpleQrCodeGenerator("https://$websiteSlug", qrSizeDp = 100, color = brandColor)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "Scan Profile",
                                    fontSize = 11.sp,
                                    fontFamily = font,
                                    color = subtitleColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    "references" -> {
                        val refs = portfolio.referencesList
                        if (refs.isNotEmpty()) {
                            SectionHeader("References", brandColor, font, textColor)
                            refs.forEach { ref ->
                                ReferenceRow(ref, font, textColor, subtitleColor)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }

                // Web-specific or preview mode elements (Contact Form representation)
                if (isWebsiteMode) {
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    SectionHeader("Contact Me", brandColor, font, textColor)
                    ContactFormMock(brandColor, font)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, brandColor: Color, font: FontFamily, textColor: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = title.uppercase(),
            fontSize = 14.sp,
            fontFamily = font,
            fontWeight = FontWeight.Bold,
            color = brandColor,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(3.dp)
                .background(brandColor, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun PersonalInfoSection(
    p: Portfolio,
    brandColor: Color,
    font: FontFamily,
    textColor: Color,
    subtitleColor: Color,
    template: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mock Photo / Initial Avatar
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(brandColor.copy(alpha = 0.15f), CircleShape)
                .border(2.dp, brandColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (p.name.isNotEmpty()) p.name.take(1).uppercase() else "P",
                fontSize = 28.sp,
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                color = brandColor
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (p.name.isEmpty()) "Your Full Name" else p.name,
                fontSize = 22.sp,
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = if (p.profession.isEmpty()) "Profession (e.g. Kotlin Engineer)" else p.profession,
                fontSize = 14.sp,
                fontFamily = font,
                fontWeight = FontWeight.Medium,
                color = brandColor
            )
            if (p.tagline.isNotEmpty()) {
                Text(
                    text = "\"${p.tagline}\"",
                    fontSize = 12.sp,
                    fontFamily = font,
                    color = subtitleColor,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
    
    if (p.aboutMe.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = p.aboutMe,
            fontSize = 13.sp,
            fontFamily = font,
            color = textColor,
            lineHeight = 18.sp,
            textAlign = TextAlign.Justify
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Grid of personal contact items
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(textColor.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (p.email.isNotEmpty()) ContactBadgeRow(Icons.Default.Email, p.email, font, subtitleColor)
        if (p.phone.isNotEmpty()) ContactBadgeRow(Icons.Default.Phone, p.phone, font, subtitleColor)
        if (p.whatsApp.isNotEmpty()) ContactBadgeRow(Icons.Default.Send, "WhatsApp: ${p.whatsApp}", font, subtitleColor)
        if (p.address.isNotEmpty()) ContactBadgeRow(Icons.Default.LocationOn, p.address, font, subtitleColor)
    }
}

@Composable
fun ContactBadgeRow(icon: ImageVector, text: String, font: FontFamily, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = null, size = 14.dp, tint = color)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text, fontSize = 11.sp, fontFamily = font, color = color)
    }
}

@Composable
fun EducationRow(edu: com.example.data.Education, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(edu.degree, fontSize = 13.sp, fontFamily = font, fontWeight = FontWeight.Bold, color = textColor)
            Text(edu.year, fontSize = 12.sp, fontFamily = font, fontWeight = FontWeight.Bold, color = brandColor)
        }
        Text("${edu.institute} - ${edu.department}", fontSize = 12.sp, fontFamily = font, color = subtitleColor)
        if (edu.gpa.isNotEmpty()) {
            Text("GPA/CGPA: ${edu.gpa}", fontSize = 11.sp, fontFamily = font, color = subtitleColor, fontWeight = FontWeight.Light)
        }
        Divider(modifier = Modifier.padding(vertical = 4.dp), color = textColor.copy(alpha = 0.1f))
    }
}

@Composable
fun ExperienceRow(exp: com.example.data.Experience, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(exp.position, fontSize = 13.sp, fontFamily = font, fontWeight = FontWeight.Bold, color = textColor)
            Text("${exp.joiningDate} to ${exp.leavingDate}", fontSize = 11.sp, fontFamily = font, color = brandColor)
        }
        Text(exp.company, fontSize = 12.sp, fontFamily = font, fontWeight = FontWeight.Medium, color = subtitleColor)
        if (exp.description.isNotEmpty()) {
            Text(exp.description, fontSize = 11.sp, fontFamily = font, color = textColor, modifier = Modifier.padding(top = 2.dp))
        }
        Divider(modifier = Modifier.padding(vertical = 4.dp), color = textColor.copy(alpha = 0.1f))
    }
}

@Composable
fun SkillRow(skill: com.example.data.Skill, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(skill.name, fontSize = 12.sp, fontFamily = font, fontWeight = FontWeight.Bold, color = textColor)
            Text("${skill.percentage}%", fontSize = 12.sp, fontFamily = font, color = brandColor, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        // Progress representation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(brandColor.copy(alpha = 0.15f), RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(skill.percentage / 100f)
                    .background(brandColor, RoundedCornerShape(3.dp))
            )
        }
    }
}

@Composable
fun AchievementRow(ach: com.example.data.Achievement, font: FontFamily, textColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Star, contentDescription = null, size = 16.dp, tint = Color(0xFFFFB300))
        Spacer(modifier = Modifier.width(8.dp))
        Text(ach.title, fontSize = 12.sp, fontFamily = font, color = textColor)
    }
}

@Composable
fun ProjectRow(proj: com.example.data.Project, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, brandColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .background(brandColor.copy(alpha = 0.02f))
            .padding(8.dp)
    ) {
        Text(proj.title, fontSize = 13.sp, fontFamily = font, fontWeight = FontWeight.Bold, color = textColor)
        if (proj.techUsed.isNotEmpty()) {
            Text("Tech: ${proj.techUsed}", fontSize = 11.sp, fontFamily = font, color = brandColor, fontWeight = FontWeight.Medium)
        }
        if (proj.description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(proj.description, fontSize = 11.sp, fontFamily = font, color = subtitleColor)
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        
        // Link lines
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (proj.githubLink.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Code, contentDescription = null, size = 12.dp, tint = brandColor)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("GitHub", fontSize = 10.sp, fontFamily = font, color = brandColor, fontWeight = FontWeight.Bold)
                }
            }
            if (proj.websiteLink.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, size = 12.dp, tint = brandColor)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live Web", fontSize = 10.sp, fontFamily = font, color = brandColor, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CertificateRow(cert: com.example.data.Certificate, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Verified, contentDescription = null, size = 20.dp, tint = brandColor)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(cert.name, fontSize = 12.sp, fontFamily = font, fontWeight = FontWeight.Bold, color = textColor)
            Text("${cert.issuedBy} | ${cert.issueDate}", fontSize = 11.sp, fontFamily = font, color = subtitleColor)
        }
    }
}

@Composable
fun LanguageBadge(lang: String, brandColor: Color, font: FontFamily) {
    Box(
        modifier = Modifier
            .background(brandColor.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .border(1.dp, brandColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(lang, fontSize = 11.sp, fontFamily = font, color = brandColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SocialLinkRow(icon: ImageVector, platform: String, link: String, brandColor: Color, textColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, size = 14.dp, tint = brandColor)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$platform: $link",
            fontSize = 11.sp,
            color = textColor,
            maxLines = 1
        )
    }
}

@Composable
fun ReferenceRow(ref: com.example.data.Reference, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(ref.name, fontSize = 12.sp, fontFamily = font, fontWeight = FontWeight.Bold, color = textColor)
        Text("${ref.role} at ${ref.company}", fontSize = 11.sp, fontFamily = font, color = subtitleColor)
        if (ref.emailOrContact.isNotEmpty()) {
            Text("Contact: ${ref.emailOrContact}", fontSize = 11.sp, fontFamily = font, color = subtitleColor)
        }
        Divider(modifier = Modifier.padding(vertical = 4.dp), color = textColor.copy(alpha = 0.05f))
    }
}

@Composable
fun ContactFormMock(brandColor: Color, font: FontFamily) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        if (!sent) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Your Name", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Your Email", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = msg,
                onValueChange = { msg = it },
                label = { Text("Message", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            Button(
                onClick = { sent = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = brandColor)
            ) {
                Text("Send Message", fontFamily = font, fontWeight = FontWeight.Bold)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = brandColor, size = 32.dp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Thank you! Message sent successfully.", fontSize = 12.sp, fontFamily = font, color = Color.Gray, textAlign = TextAlign.Center)
            }
        }
    }
}

// Icon helper extension to set size easily
@Composable
fun Icon(imageVector: ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: Color) {
    Box(modifier = Modifier.size(size)) {
        androidx.compose.material3.Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.fillMaxSize()
        )
    }
}

// ====================================================================
// PREMIUM TEMPLATE ENGINE FOR THE 20 SPECIFIC STYLES
// ====================================================================

@Composable
fun PremiumTemplateLayout(
    p: Portfolio,
    preset: com.example.data.TemplatePreset,
    brandColor: Color,
    font: FontFamily,
    textColor: Color,
    subtitleColor: Color,
    isWebsiteMode: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (preset.id) {
            "prem_1" -> ClassicAcademicLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_2" -> ModernSplitLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_3" -> DarkPremiumLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_4" -> TeacherLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_5" -> StudentResumeLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_6" -> CreativeDesignerLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_7" -> EngineerLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_8" -> MedicalLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_9" -> BusinessProfileLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_10" -> FreelancerLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_11" -> TimelineLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_12" -> MagazineLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_13" -> NewspaperLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_14" -> InfographicLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_15" -> GlassmorphismLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_16" -> CorporateLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_17" -> DeveloperConsoleLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_18" -> PhotographerLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_19" -> MultiColorLayout(p, brandColor, font, textColor, subtitleColor)
            "prem_20" -> LuxuryPremiumLayout(p, brandColor, font, textColor, subtitleColor)
            else -> PersonalInfoSection(p, brandColor, font, textColor, subtitleColor, "Modern")
        }

        PremiumCommonFooter(p, brandColor, font, textColor, subtitleColor)

        if (isWebsiteMode) {
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            SectionHeader("Contact Me", brandColor, font, textColor)
            ContactFormMock(brandColor, font)
        }
    }
}

@Composable
fun PremiumSectionHeader(title: String, brandColor: Color, font: FontFamily, textColor: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(6.dp).background(brandColor, CircleShape))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title.uppercase(),
                fontSize = 11.sp,
                fontFamily = font,
                fontWeight = FontWeight.Bold,
                color = brandColor,
                letterSpacing = 1.sp
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(brandColor.copy(alpha = 0.15f)))
    }
}

@Composable
fun PremiumCommonFooter(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(brandColor.copy(alpha = 0.04f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val websiteSlug = if (p.name.isNotEmpty()) p.name.lowercase().replace(" ", "") + ".infinityportfolio.app" else "mysite.infinityportfolio.app"
        SimpleQrCodeGenerator("https://$websiteSlug", 85, brandColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "SCAN QR TO VISIT ONLINE SITE",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = font,
            color = brandColor,
            letterSpacing = 1.sp
        )
        Text(
            text = "https://$websiteSlug",
            fontSize = 10.sp,
            fontFamily = font,
            color = subtitleColor
        )
        Text(
            text = "© 2026 ${p.name.ifEmpty { "Portfolio" }}. Built with Infinity CV Premium. ✨",
            fontSize = 8.sp,
            color = subtitleColor.copy(alpha = 0.7f),
            fontFamily = font
        )
    }
}

// --------------------------------------------------------------------
// 1. CLASSIC ACADEMIC PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun ClassicAcademicLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(brandColor.copy(alpha = 0.12f), CircleShape)
                    .border(2.dp, brandColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(p.name.take(1).uppercase(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(p.name.ifEmpty { "Scholarly Faculty" }, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
                Text(p.profession.ifEmpty { "Dean & Professor of Computer Science" }, fontSize = 12.sp, color = textColor, fontFamily = font)
                Text(p.address.ifEmpty { "Harvard University, Cambridge" }, fontSize = 10.sp, color = subtitleColor, fontFamily = font)
            }
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(brandColor.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                    .border(1.dp, brandColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.School, null, size = 22.dp, tint = brandColor)
            }
        }

        PremiumSectionHeader("About & Academic Statement", brandColor, font, textColor)
        Text(p.aboutMe.ifEmpty { "Dedicated professor specializing in machine learning pipelines, distributed computation, and mentoring graduate research cohorts." }, fontSize = 12.sp, color = textColor, fontFamily = font, lineHeight = 16.sp)

        if (p.educationList.isNotEmpty()) {
            PremiumSectionHeader("Education & Credentials", brandColor, font, textColor)
            p.educationList.forEach { edu ->
                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(edu.degree, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                    Text("${edu.institute} | Dept: ${edu.department} (${edu.year})", fontSize = 11.sp, color = subtitleColor, fontFamily = font)
                    if (edu.gpa.isNotEmpty()) Text("Academic Score: ${edu.gpa}", fontSize = 10.sp, color = brandColor, fontFamily = font)
                }
            }
        }

        if (p.experienceList.isNotEmpty()) {
            PremiumSectionHeader("Faculty & Academic Experience", brandColor, font, textColor)
            p.experienceList.forEach { exp ->
                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(exp.position, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                    Text("${exp.company} | ${exp.joiningDate} to ${exp.leavingDate}", fontSize = 11.sp, color = subtitleColor, fontFamily = font)
                    if (exp.description.isNotEmpty()) Text(exp.description, fontSize = 10.sp, color = textColor.copy(alpha = 0.8f), fontFamily = font)
                }
            }
        }

        if (p.skillsList.isNotEmpty()) {
            PremiumSectionHeader("Subject Preferences & Skills", brandColor, font, textColor)
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                p.skillsList.forEach { sk ->
                    Box(modifier = Modifier.background(brandColor.copy(alpha = 0.1f), RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text(sk.name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
                    }
                }
            }
        }

        if (p.referencesList.isNotEmpty()) {
            PremiumSectionHeader("References", brandColor, font, textColor)
            p.referencesList.forEach { ref ->
                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(ref.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                    Text("${ref.role} at ${ref.company} | ${ref.emailOrContact}", fontSize = 10.sp, color = subtitleColor, fontFamily = font)
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 2. MODERN SPLIT LAYOUT
// --------------------------------------------------------------------
@Composable
fun ModernSplitLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brandColor, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(p.name.uppercase(), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black, fontFamily = font)
                Text(p.profession.uppercase(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black.copy(alpha = 0.7f), fontFamily = font)
                if (p.tagline.isNotEmpty()) Text("\"${p.tagline}\"", fontSize = 10.sp, color = Color.Black.copy(alpha = 0.6f), fontFamily = font)
            }
        }

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Left Column (Sidebar Style)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brandColor.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("CONNECT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
                if (p.email.isNotEmpty()) ContactBadgeRow(Icons.Default.Email, p.email, font, textColor)
                if (p.phone.isNotEmpty()) ContactBadgeRow(Icons.Default.Phone, p.phone, font, textColor)
                if (p.address.isNotEmpty()) ContactBadgeRow(Icons.Default.LocationOn, p.address, font, textColor)

                if (p.skillsList.isNotEmpty()) {
                    Text("SKILLS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
                    p.skillsList.take(4).forEach { sk ->
                        Column {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(sk.name, fontSize = 10.sp, color = textColor, fontFamily = font)
                                Text("${sk.percentage}%", fontSize = 9.sp, color = brandColor, fontWeight = FontWeight.Bold)
                            }
                            Box(modifier = Modifier.fillMaxWidth().height(4.dp).background(brandColor.copy(alpha = 0.15f), RoundedCornerShape(2.dp))) {
                                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(sk.percentage / 100f).background(brandColor, RoundedCornerShape(2.dp)))
                            }
                        }
                    }
                }
            }

            // Right Column (Main Details)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("EXECUTIVE BIOGRAPHY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
                Text(p.aboutMe.ifEmpty { "Experienced professional skilled in executing critical corporate mandates, team leadership, and strategic product growth." }, fontSize = 12.sp, color = textColor, fontFamily = font)

                if (p.experienceList.isNotEmpty()) {
                    Text("EXPERIENCE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
                    p.experienceList.forEach { exp ->
                        Column(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text(exp.position, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                            Text("${exp.company} | ${exp.joiningDate} - ${exp.leavingDate}", fontSize = 10.sp, color = subtitleColor, fontFamily = font)
                        }
                    }
                }

                if (p.projectsList.isNotEmpty()) {
                    Text("KEY INITIATIVES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
                    p.projectsList.take(3).forEach { proj ->
                        Column(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text(proj.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                            Text(proj.description, fontSize = 10.sp, color = subtitleColor, fontFamily = font)
                        }
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 3. DARK PREMIUM PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun DarkPremiumLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, brandColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .background(Color(0xFF111111), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp).background(brandColor, CircleShape), contentAlignment = Alignment.Center) {
                Text(p.name.take(1).uppercase(), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(p.name.ifEmpty { "Premium Leader" }, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                Text(p.profession.ifEmpty { "Chief Technical Architect" }, fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(brandColor.copy(alpha = 0.2f)))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("ABOUT ME", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            Text(p.aboutMe.ifEmpty { "High premium portfolio with black background and luxury golden highlights." }, fontSize = 12.sp, color = textColor, fontFamily = font)
        }

        if (p.skillsList.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("PREMIUM CAPABILITIES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
                p.skillsList.take(3).forEach { sk ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(sk.name, fontSize = 11.sp, color = textColor, fontFamily = font)
                        Text("${sk.percentage}%", fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
                    }
                }
            }
        }

        if (p.experienceList.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("HISTORIC PATHWAY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
                p.experienceList.take(2).forEach { exp ->
                    Column {
                        Text(exp.position, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                        Text(exp.company, fontSize = 10.sp, color = brandColor, fontFamily = font)
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 4. TEACHER PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun TeacherLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            colors = CardDefaults.cardColors(containerColor = brandColor.copy(alpha = 0.08f)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(54.dp).background(brandColor, CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.School, null, size = 26.dp, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(p.name.ifEmpty { "Honored Educator" }, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                    Text("Teaching Specialty: ${p.profession.ifEmpty { "English & Mathematics" }}", fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, brandColor.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Text("TEACHING PHILOSOPHY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            Spacer(modifier = Modifier.height(4.dp))
            Text("\"${p.aboutMe.ifEmpty { "To inspire curiosity, nurture critical thinking, and support every student's learning journey." }}\"", fontSize = 12.sp, color = textColor, fontFamily = font, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        }

        if (p.experienceList.isNotEmpty()) {
            Text("TEACHING RECORD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            p.experienceList.forEach { exp ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Icon(Icons.Default.Check, null, size = 14.dp, tint = brandColor)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(exp.position, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                        Text("${exp.company} | ${exp.joiningDate} - ${exp.leavingDate}", fontSize = 10.sp, color = subtitleColor)
                    }
                }
            }
        }

        if (p.achievementsList.isNotEmpty()) {
            Text("ACADEMIC RESULTS & RECOGNITIONS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            p.achievementsList.forEach { ach ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                    Icon(Icons.Default.Star, null, size = 12.dp, tint = Color(0xFFFFB300))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(ach.title, fontSize = 11.sp, color = textColor, fontFamily = font)
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 5. STUDENT RESUME
// --------------------------------------------------------------------
@Composable
fun StudentResumeLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Column {
            Text(p.name.ifEmpty { "Scholastic Student" }, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            Text(p.address.ifEmpty { "Dhaka University" }, fontSize = 11.sp, color = subtitleColor, fontFamily = font)
            Text(p.email.ifEmpty { "student@domain.com" }, fontSize = 10.sp, color = subtitleColor, fontFamily = font)
        }

        Divider(color = brandColor, thickness = 1.5.dp)

        Column {
            Text("OBJECTIVE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            Text(p.aboutMe.ifEmpty { "Passionate undergraduate student seeking academic opportunities, scholarships, or internship experience to apply computer science theory." }, fontSize = 12.sp, color = textColor, fontFamily = font)
        }

        if (p.educationList.isNotEmpty()) {
            Text("EDUCATION PATHWAY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            p.educationList.forEach { edu ->
                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(edu.degree, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                    Text("${edu.institute} | Dept: ${edu.department} (${edu.year})", fontSize = 10.sp, color = subtitleColor)
                    if (edu.gpa.isNotEmpty()) Text("CGPA Score: ${edu.gpa}", fontSize = 9.sp, color = brandColor, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (p.skillsList.isNotEmpty()) {
            Text("CORE SKILLS & SUBJECTS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                p.skillsList.forEach { sk ->
                    Box(modifier = Modifier.background(brandColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text(sk.name, fontSize = 9.sp, color = brandColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (p.achievementsList.isNotEmpty()) {
            Text("COMPETITIONS & ACTIVITIES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            p.achievementsList.forEach { ach ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("•", fontSize = 11.sp, color = brandColor, modifier = Modifier.padding(end = 6.dp))
                    Text(ach.title, fontSize = 11.sp, color = textColor)
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 6. CREATIVE DESIGNER PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun CreativeDesignerLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(brandColor, brandColor.copy(alpha = 0.5f))
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Text(p.name.ifEmpty { "Artistic Designer" }, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, fontFamily = font)
                Text(p.profession.ifEmpty { "Creative Director & Illustrator" }, fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f), fontFamily = font)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("DESIGN MANIFESTO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            Text(p.aboutMe.ifEmpty { "Designing playful interfaces, gorgeous illustrations, and vibrant brand assets with functional aesthetics." }, fontSize = 12.sp, color = textColor, fontFamily = font)
        }

        if (p.skillsList.isNotEmpty()) {
            Text("SOFTWARE COMPASS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            p.skillsList.forEach { sk ->
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(sk.name, fontSize = 10.sp, color = textColor, fontWeight = FontWeight.Bold)
                        Text("${sk.percentage}%", fontSize = 10.sp, color = brandColor, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(brandColor.copy(alpha = 0.15f), RoundedCornerShape(3.dp))) {
                        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(sk.percentage / 100f).background(brandColor, RoundedCornerShape(3.dp)))
                    }
                }
            }
        }

        if (p.projectsList.isNotEmpty()) {
            Text("GALLERY PIECES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            p.projectsList.forEach { proj ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = brandColor.copy(alpha = 0.03f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, brandColor.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(proj.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                        Text(proj.description, fontSize = 10.sp, color = subtitleColor)
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 7. ENGINEER PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun EngineerLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(54.dp).background(brandColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Build, null, size = 26.dp, tint = brandColor)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(p.name.ifEmpty { "Lead Systems Engineer" }, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                Text(p.profession.ifEmpty { "Principal DevOps & Mechanical Systems Analyst" }, fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
            }
        }

        PremiumSectionHeader("Engineering Profile", brandColor, font, textColor)
        Text(p.aboutMe.ifEmpty { "Designing optimized systems, industrial architectures, high-performance data processing nodes, and cloud infrastructures." }, fontSize = 12.sp, color = textColor, fontFamily = font)

        if (p.skillsList.isNotEmpty()) {
            PremiumSectionHeader("Technical Arsenal", brandColor, font, textColor)
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                p.skillsList.forEach { sk ->
                    Box(modifier = Modifier.background(brandColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text(sk.name, fontSize = 9.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
                    }
                }
            }
        }

        if (p.projectsList.isNotEmpty()) {
            PremiumSectionHeader("Engineered Ventures", brandColor, font, textColor)
            p.projectsList.forEach { pr ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(pr.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                    if (pr.techUsed.isNotEmpty()) Text("Arsenal: ${pr.techUsed}", fontSize = 10.sp, color = brandColor, fontWeight = FontWeight.Bold)
                    Text(pr.description, fontSize = 10.sp, color = subtitleColor)
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 8. MEDICAL PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun MedicalLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).background(Color.White, CircleShape).border(2.dp, brandColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🩺", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(p.name.ifEmpty { "Dr. Clinical Practitioner" }, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                Text(p.profession.ifEmpty { "MD, Specialist Physician" }, fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = brandColor.copy(alpha = 0.05f))) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("EMERGENCY PROTOCOL CONTACT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = brandColor)
                Text("Direct Contact: ${p.phone.ifEmpty { "+1 (555) 911-300" }}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
        }

        Text("CLINICAL STATEMENT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
        Text(p.aboutMe.ifEmpty { "Committed to delivering evidence-based patient therapies, cardiovascular health research, and medical consulting services." }, fontSize = 12.sp, color = textColor, fontFamily = font)

        if (p.experienceList.isNotEmpty()) {
            Text("CLINICAL RESIDENCY & HOSPITAL EXPERIENCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
            p.experienceList.forEach { exp ->
                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(exp.position, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                    Text(exp.company, fontSize = 10.sp, color = subtitleColor)
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 9. BUSINESS PROFILE
// --------------------------------------------------------------------
@Composable
fun BusinessProfileLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.size(54.dp).background(brandColor, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Business, null, size = 26.dp, tint = Color.White)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(p.name.ifEmpty { "Enterprise Corp" }, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
            Text(p.profession.ifEmpty { "Strategic Industry Consulting" }, fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = brandColor.copy(alpha = 0.05f))) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("OUR MISSION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
                    Text("Leveraging digital platforms to optimize standard user pipelines.", fontSize = 10.sp, color = textColor)
                }
            }
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = brandColor.copy(alpha = 0.05f))) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("OUR VISION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
                    Text("Expanding operations globally to secure high industry outcomes.", fontSize = 10.sp, color = textColor)
                }
            }
        }

        Text("BUSINESS OVERVIEW", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor)
        Text(p.aboutMe.ifEmpty { "High prestige corporate presentation outlining services, key personnel, client testimonials, and business results." }, fontSize = 12.sp, color = textColor)

        if (p.skillsList.isNotEmpty()) {
            Text("OUR SERVICES", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor)
            p.skillsList.forEach { sk ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                    Icon(Icons.Default.CheckCircle, null, size = 14.dp, tint = brandColor)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(sk.name, fontSize = 11.sp, color = textColor)
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 10. FREELANCER PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun FreelancerLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(46.dp).background(brandColor, CircleShape), contentAlignment = Alignment.Center) {
                Text("🚀", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(p.name.ifEmpty { "Independent Freelancer" }, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                Text(p.profession.ifEmpty { "Full Stack Contractor" }, fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = brandColor.copy(alpha = 0.05f))) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("FREELANCER RATES & PACKAGES", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Standard: $50/hr", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                    Text("Project: $1,200 Min", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
            }
        }

        Text("PITCH", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
        Text(p.aboutMe.ifEmpty { "Delivering stellar interfaces on time and under budget. Available for immediate freelance contract work." }, fontSize = 12.sp, color = textColor)

        if (p.skillsList.isNotEmpty()) {
            Text("SERVICES RENDERED", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                p.skillsList.forEach { sk ->
                    Box(modifier = Modifier.background(brandColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text(sk.name, fontSize = 9.sp, color = brandColor, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 11. TIMELINE PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun TimelineLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("CHRONOLOGICAL JOURNEY", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
        
        if (p.educationList.isNotEmpty()) {
            Text("Academic Path", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            p.educationList.forEach { edu ->
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(30.dp)) {
                        Box(modifier = Modifier.size(8.dp).background(brandColor, CircleShape))
                        Box(modifier = Modifier.width(2.dp).height(24.dp).background(brandColor.copy(alpha = 0.3f)))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(edu.degree, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                        Text("${edu.institute} | ${edu.year}", fontSize = 10.sp, color = subtitleColor)
                    }
                }
            }
        }

        if (p.experienceList.isNotEmpty()) {
            Text("Career Path", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
            p.experienceList.forEach { exp ->
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(30.dp)) {
                        Box(modifier = Modifier.size(8.dp).background(Color.Gray, CircleShape))
                        Box(modifier = Modifier.width(2.dp).height(24.dp).background(Color.LightGray))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(exp.position, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                        Text("${exp.company} (${exp.joiningDate} - ${exp.leavingDate})", fontSize = 10.sp, color = subtitleColor)
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 12. MAGAZINE STYLE
// --------------------------------------------------------------------
@Composable
fun MagazineLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = p.name.ifEmpty { "THE COVETED" }.uppercase(),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = font,
            color = brandColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            letterSpacing = 4.sp
        )
        Text(
            text = "VOLUME IV | CHRONICLE PROFILE",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = font,
            color = subtitleColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            letterSpacing = 2.sp
        )

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(textColor))

        Text(
            text = p.aboutMe.ifEmpty { "A feature biography detailing creative outputs and elite professional movements across global frameworks." },
            fontSize = 13.sp,
            fontFamily = font,
            color = textColor,
            lineHeight = 18.sp,
            textAlign = TextAlign.Justify
        )

        if (p.projectsList.isNotEmpty()) {
            PremiumSectionHeader("Featured Exhibits", brandColor, font, textColor)
            p.projectsList.take(2).forEach { proj ->
                Column {
                    Text(proj.title.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                    Text(proj.description, fontSize = 11.sp, color = subtitleColor, fontFamily = font)
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 13. NEWSPAPER STYLE
// --------------------------------------------------------------------
@Composable
fun NewspaperLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("THE DAILY RECORD", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, fontFamily = font, color = textColor)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("VOL. CXXI No. 42", fontSize = 8.sp, fontFamily = font, color = textColor)
                Text("JULY 2026", fontSize = 8.sp, fontFamily = font, color = textColor)
                Text("PRESTIGE EDIT", fontSize = 8.sp, fontFamily = font, color = textColor)
            }
            Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(textColor))
        }

        Text(
            text = "BREAKING: ${p.name.uppercase()} REDEFINES ${p.profession.uppercase()} WORKFLOWS!",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = font,
            color = textColor
        )

        Text(
            text = p.aboutMe.ifEmpty { "Exclusive reporter interview detailing professional achievements, technical background, and upcoming projects." },
            fontSize = 11.sp,
            fontFamily = font,
            color = textColor,
            lineHeight = 15.sp,
            textAlign = TextAlign.Justify
        )

        if (p.experienceList.isNotEmpty()) {
            Text("CAREER MILESTONES REPORT", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = font)
            p.experienceList.take(3).forEach { exp ->
                Column(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("• ${exp.position} AT ${exp.company.uppercase()}", fontSize = 10.sp, fontWeight = FontWeight.Bold, fontFamily = font)
                    Text(exp.description, fontSize = 10.sp, color = subtitleColor, fontFamily = font)
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 14. INFOGRAPHIC PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun InfographicLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("METRIC DASHBOARD", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)

        // Simulated Metrics Stats Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val stats = listOf(
                "Views" to p.viewsCount,
                "Downloads" to p.downloadsCount,
                "Shares" to p.sharesCount,
                "Visitors" to p.visitorsCount
            )
            stats.forEach { stat ->
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = brandColor.copy(alpha = 0.05f))
                ) {
                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stat.second.toString(), fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = brandColor)
                        Text(stat.first, fontSize = 8.sp, color = subtitleColor)
                    }
                }
            }
        }

        if (p.skillsList.isNotEmpty()) {
            Text("KPI PERFORMANCE METERS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
            p.skillsList.take(4).forEach { sk ->
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(sk.name, fontSize = 10.sp, color = textColor)
                        Text("${sk.percentage}% Efficiency", fontSize = 9.sp, color = brandColor, fontWeight = FontWeight.Bold)
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(brandColor.copy(alpha = 0.15f), RoundedCornerShape(3.dp))) {
                        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(sk.percentage / 100f).background(brandColor, RoundedCornerShape(3.dp)))
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 15. GLASSMORPHISM PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun GlassmorphismLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.25f))
            .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(46.dp).background(brandColor.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                    Text("💎", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(p.name.ifEmpty { "Glass Artist" }, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                    Text(p.profession.ifEmpty { "UI/UX Designer" }, fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.2f)))

            Text(p.aboutMe.ifEmpty { "High-end visual portfolios crafted using frosted glass containers, colorful blobs, and modern typography." }, fontSize = 12.sp, color = textColor, fontFamily = font)

            if (p.skillsList.isNotEmpty()) {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    p.skillsList.forEach { sk ->
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(sk.name, fontSize = 9.sp, color = textColor, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 16. CORPORATE CV
// --------------------------------------------------------------------
@Composable
fun CorporateLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(54.dp).background(brandColor, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Verified, null, size = 26.dp, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(p.name.ifEmpty { "Corporate Executive" }, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                Text(p.profession.ifEmpty { "Managing Director & Advisor" }, fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold, fontFamily = font)
            }
        }

        PremiumSectionHeader("Executive Summary", brandColor, font, textColor)
        Text(p.aboutMe.ifEmpty { "Dedicated executive offering years of corporate guidance, organizational structuring, and board-level consulting." }, fontSize = 12.sp, color = textColor)

        if (p.experienceList.isNotEmpty()) {
            PremiumSectionHeader("Corporate Engagements", brandColor, font, textColor)
            p.experienceList.forEach { exp ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(exp.position, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textColor)
                    Text(exp.company, fontSize = 10.sp, color = brandColor)
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 17. DEVELOPER CONSOLE STYLE
// --------------------------------------------------------------------
@Composable
fun DeveloperConsoleLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF070B19), RoundedCornerShape(10.dp))
            .border(1.dp, brandColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.size(8.dp).background(Color(0xFFFF5F56), CircleShape))
                Box(modifier = Modifier.size(8.dp).background(Color(0xFFFFBD2E), CircleShape))
                Box(modifier = Modifier.size(8.dp).background(Color(0xFF27C93F), CircleShape))
            }
            Text("terminal - bash", fontSize = 8.sp, color = brandColor, fontFamily = FontFamily.Monospace)
        }

        Text(
            text = "$ cat profile.json\n{\n  \"name\": \"${p.name.ifEmpty { "Developer" }}\",\n  \"role\": \"${p.profession.ifEmpty { "Coder" }}\"\n}",
            fontSize = 11.sp,
            color = brandColor,
            fontFamily = FontFamily.Monospace
        )

        Text("// About Me", fontSize = 10.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
        Text(p.aboutMe.ifEmpty { "Hacker and open source enthusiast building highly responsive modern mobile app stacks." }, fontSize = 11.sp, color = textColor, fontFamily = FontFamily.Monospace)

        if (p.skillsList.isNotEmpty()) {
            Text("// Technology Stack Array", fontSize = 10.sp, color = Color.Gray, fontFamily = FontFamily.Monospace)
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                p.skillsList.forEach { sk ->
                    Box(modifier = Modifier.border(1.dp, brandColor, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text(sk.name, fontSize = 9.sp, color = brandColor, fontFamily = FontFamily.Monospace)
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 18. PHOTOGRAPHER PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun PhotographerLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(p.name.ifEmpty { "Visual Storyteller" }, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = brandColor, fontFamily = font)
        Text("Camera Arsenal: ${p.profession.ifEmpty { "Leica SL2 & Canon EOS R5" }}", fontSize = 11.sp, color = subtitleColor, fontFamily = font)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(Color.DarkGray, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("📷", fontSize = 32.sp)
                Text("PHOTO COMPOSITIONS PORTFOLIO", fontSize = 10.sp, color = Color.LightGray, fontWeight = FontWeight.Bold, fontFamily = font)
            }
        }

        Text("CREATIVE VISION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor)
        Text(p.aboutMe.ifEmpty { "Capturing raw landscapes, fine arts studio configurations, and luxury branding model portfolios." }, fontSize = 12.sp, color = textColor)
    }
}

// --------------------------------------------------------------------
// 19. MULTI COLOR PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun MultiColorLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp).background(Color(0xFFFBCFE8), CircleShape), contentAlignment = Alignment.Center) {
                Text("🌈", fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(p.name.ifEmpty { "Vibrant Creator" }, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, fontFamily = font)
                Text(p.profession.ifEmpty { "Creative Generalist" }, fontSize = 11.sp, color = brandColor, fontWeight = FontWeight.Bold)
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("STORYBOOK BRIEF", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
                Text(p.aboutMe.ifEmpty { "Playful design blocks, happy pastel colors, and friendly illustrations." }, fontSize = 12.sp, color = Color(0xFF78350F))
            }
        }

        if (p.skillsList.isNotEmpty()) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                p.skillsList.forEach { sk ->
                    Box(modifier = Modifier.background(Color(0xFFD1FAE5), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Text(sk.name, fontSize = 9.sp, color = Color(0xFF065F46), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------------
// 20. LUXURY PREMIUM PORTFOLIO
// --------------------------------------------------------------------
@Composable
fun LuxuryPremiumLayout(p: Portfolio, brandColor: Color, font: FontFamily, textColor: Color, subtitleColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, brandColor, RoundedCornerShape(16.dp))
            .background(Color.Black, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("⚜️", fontSize = 24.sp)
        Text(p.name.ifEmpty { "Royal Dignitary" }.uppercase(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor, letterSpacing = 3.sp, fontFamily = font)
        Text(p.profession.ifEmpty { "International Liaison Officer" }.uppercase(), fontSize = 10.sp, color = brandColor, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, fontFamily = font)

        Box(modifier = Modifier.width(60.dp).height(1.dp).background(brandColor))

        Text(
            text = p.aboutMe.ifEmpty { "Exclusive luxury statement describing prestigious diplomatic histories, administrative wisdom, and board credentials." },
            fontSize = 12.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            fontFamily = font,
            lineHeight = 16.sp
        )

        if (p.achievementsList.isNotEmpty()) {
            Text("CROWN HONORS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = brandColor, letterSpacing = 1.sp, fontFamily = font)
            p.achievementsList.take(2).forEach { ach ->
                Text("♛ ${ach.title}", fontSize = 11.sp, color = textColor, fontFamily = font)
            }
        }
    }
}

