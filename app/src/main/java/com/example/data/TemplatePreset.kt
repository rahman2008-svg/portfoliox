package com.example.data

data class TemplatePreset(
    val id: String,
    val name: String,
    val category: String,
    val themeColor: String, // Primary brand hex
    val fontFamily: String, // SansSerif, Serif, Monospace
    val backgroundColorHex: String, // Solid surface background
    val textColorHex: String, // Main text color
    val subtitleColorHex: String, // Subtitle/helper text color
    val isDark: Boolean,
    val styleTemplate: String, // "Modern", "Minimal", "Dark", "Light", "Corporate", "Creative", "Developer", "Academic"
    val description: String,
    val iconEmoji: String
)

object TemplateRegistry {
    val presets: List<TemplatePreset> = listOf(
        // ====================================================================
        // THE 20 SPECIFIED PREMIUM TEMPLATES (First class citizens)
        // ====================================================================
        TemplatePreset("prem_1", "Classic Academic Portfolio", "Academic", "#1E3A8A", "Serif", "#FFFFFF", "#1E3A8A", "#475569", false, "Academic", "White + Navy Blue academic layout with circular photo and university crest header.", "🎓"),
        TemplatePreset("prem_2", "Modern Split Layout", "Professional", "#D4AF37", "SansSerif", "#000000", "#FFFFFF", "#E5C158", true, "Modern", "Black + Gold full-width background with elegant split column details.", "🌟"),
        TemplatePreset("prem_3", "Dark Premium Portfolio", "Professional", "#D4AF37", "SansSerif", "#121212", "#FFFFFF", "#B59410", true, "Dark", "Very Premium dark look with glowing golden card highlights and QR profiling.", "👑"),
        TemplatePreset("prem_4", "Teacher Portfolio", "Academic", "#2563EB", "SansSerif", "#F0F9FF", "#1E3A8A", "#2563EB", false, "Light", "Blue theme designed for educators with teaching philosophy, results and feedback.", "🍎"),
        TemplatePreset("prem_5", "Student Resume", "Minimalist", "#16A34A", "SansSerif", "#FFFFFF", "#1F2937", "#16A34A", false, "Minimal", "Minimal white layout with clean green accents, optimized for objective, activities & achievements.", "🎒"),
        TemplatePreset("prem_6", "Creative Designer Portfolio", "Creative", "#EC4899", "SansSerif", "#FAF5FF", "#1E1B4B", "#EC4899", false, "Creative", "Large visual header with creative gradients, designer gallery and software ratings.", "🎨"),
        TemplatePreset("prem_7", "Engineer Portfolio", "Tech & Developer", "#0284C7", "SansSerif", "#F3F4F6", "#1F2937", "#0284C7", false, "Modern", "Gray + Blue tech palette showcasing programming, internship, and engineering projects.", "⚙️"),
        TemplatePreset("prem_8", "Medical Portfolio", "Professional", "#DC2626", "SansSerif", "#FFFFFF", "#1F2937", "#DC2626", false, "Light", "White + Red healthcare theme with doctor credentials, specialization and emergency contact.", "🩺"),
        TemplatePreset("prem_9", "Business Profile", "Professional", "#0F172A", "SansSerif", "#F8FAFC", "#0F172A", "#475569", false, "Corporate", "Corporate identity layout showcasing company mission, team, clients and business results.", "🏢"),
        TemplatePreset("prem_10", "Freelancer Portfolio", "Creative", "#8B5CF6", "SansSerif", "#FAF5FF", "#1E1B4B", "#8B5CF6", false, "Creative", "Purple themed personal pitch, packages pricing, client reviews & services list.", "💼"),
        TemplatePreset("prem_11", "Timeline Portfolio", "Professional", "#059669", "SansSerif", "#FCFCFC", "#111827", "#059669", false, "Corporate", "Interactive dual timeline view with education on the left and experience on the right.", "⏳"),
        TemplatePreset("prem_12", "Magazine Style", "Creative", "#C026D3", "Serif", "#FDF8F7", "#111827", "#C026D3", false, "Academic", "Large cover visual page, generous editorial spacing and literary serif headings.", "📖"),
        TemplatePreset("prem_13", "Newspaper Style", "Minimalist", "#000000", "Serif", "#F9F6F0", "#000000", "#333333", false, "Minimal", "Classic monochrome newspaper column layout with custom top headline styling.", "📰"),
        TemplatePreset("prem_14", "Infographic Portfolio", "Creative", "#F97316", "SansSerif", "#FDF8F5", "#1E293B", "#F97316", false, "Modern", "Rich data infographics style containing visual rating meters, metrics, and progress bars.", "📊"),
        TemplatePreset("prem_15", "Glassmorphism Portfolio", "Creative", "#3B82F6", "SansSerif", "#EFF6FF", "#1E3A8A", "#3B82F6", false, "Creative", "Frosted glass translucent container panels with glowing colorful background nodes.", "💎"),
        TemplatePreset("prem_16", "Corporate CV", "Professional", "#2563EB", "SansSerif", "#F8FAFC", "#0F172A", "#2563EB", false, "Corporate", "Blue & White traditional executive style structured for elite corporate careers.", "👔"),
        TemplatePreset("prem_17", "Developer Portfolio", "Tech & Developer", "#10B981", "Monospace", "#090D16", "#ECFDF5", "#10B981", true, "Developer", "Dark compiler-style terminal layout with live git metrics and formatted code comments.", "💻"),
        TemplatePreset("prem_18", "Photographer Portfolio", "Creative", "#111827", "SansSerif", "#FFFFFF", "#111827", "#6B7280", false, "Minimal", "Striking full-width photography portfolio view designed for visual storytellers.", "📷"),
        TemplatePreset("prem_19", "Multi Color Portfolio", "Creative", "#8B5CF6", "SansSerif", "#FFFDF9", "#1E293B", "#E11D48", false, "Creative", "Dynamic multi-hue pastel card layout featuring playful icons and vibrant tags.", "🌈"),
        TemplatePreset("prem_20", "Luxury Premium Portfolio", "Professional", "#D4AF37", "Serif", "#0A0A0A", "#FFFFFF", "#D4AF37", true, "Dark", "Elite Black + Gold + White visual theme with majestic typography and refined margins.", "⚜️"),

        // ====================================================================
        // ADDITIONAL REQUESTED PORTFOLIO CATEGORIES (To secure 100+ templates total)
        // ====================================================================
        TemplatePreset("extra_1", "Minimal Resume", "Minimalist", "#1E293B", "SansSerif", "#FFFFFF", "#0F172A", "#475569", false, "Minimal", "Ultra compact single-page resume with tight density formatting.", "📄"),
        TemplatePreset("extra_2", "Executive Portfolio", "Professional", "#0F172A", "Serif", "#F8FAFC", "#1E293B", "#475569", false, "Corporate", "Polished executive presentation for board directors and leaders.", "💼"),
        TemplatePreset("extra_3", "Elegant Tutor Profile", "Academic", "#1E3A8A", "SansSerif", "#F8FAFC", "#0F172A", "#2563EB", false, "Light", "Welcoming, trust-oriented tutor outline for academic advisors.", "🎓"),
        TemplatePreset("extra_4", "University Admission Profile", "Academic", "#854D0E", "Serif", "#FFFBEB", "#422006", "#B45309", false, "Academic", "High impact application outline with highlight sections.", "🏫"),
        TemplatePreset("extra_5", "Scholarship Portfolio", "Academic", "#047857", "Serif", "#ECFDF5", "#064E3B", "#059669", false, "Academic", "Academic credentials presentation structured for scholarship boards.", "🏅"),
        TemplatePreset("extra_6", "Startup Founder Profile", "Professional", "#7C3AED", "SansSerif", "#F9F8FF", "#1F1A3A", "#7C3AED", false, "Modern", "Dynamic layout with vision, funding milestones, and team pitches.", "🚀"),
        TemplatePreset("extra_7", "Content Creator Portfolio", "Creative", "#EC4899", "SansSerif", "#FFF1F2", "#4C0519", "#EC4899", false, "Creative", "Showcases video views, metrics, YouTube/TikTok cards and sponsorships.", "🎬"),
        TemplatePreset("extra_8", "Influencer Profile", "Creative", "#8B5CF6", "SansSerif", "#FAF5FF", "#1E1B4B", "#8B5CF6", false, "Creative", "Social engagement statistics, media kits, and brand partnerships.", "🤳"),
        TemplatePreset("extra_9", "Research Scholar Portfolio", "Academic", "#1E3A8A", "Serif", "#FFFFFF", "#111827", "#475569", false, "Academic", "PhD thesis lists, research grants, citations, and journals catalog.", "🔬"),
        TemplatePreset("extra_10", "Artist & Illustrator Portfolio", "Creative", "#F59E0B", "SansSerif", "#FFFBEB", "#451A03", "#D97706", false, "Creative", "Immersive design gallery layout to display artwork and commissions.", "🎨"),

        // ====================================================================
        // CATEGORY: PROFESSIONAL (20 Templates)
        // ====================================================================
        TemplatePreset("prof_1", "Indigo Executive", "Professional", "#3F51B5", "SansSerif", "#FFFFFF", "#212121", "#616161", false, "Corporate", "Deep indigo professional layout for corporate careers.", "💼"),
        TemplatePreset("prof_2", "Slate Partner", "Professional", "#475569", "SansSerif", "#F8FAFC", "#0F172A", "#475569", false, "Corporate", "Slate modern clean finish, perfect for partners and consultants.", "🏛️"),
        TemplatePreset("prof_3", "Emerald Trust", "Professional", "#059669", "SansSerif", "#F0FDF4", "#1F2937", "#4B5563", false, "Corporate", "Forest emerald green with high-trust contrast styling.", "📈"),
        TemplatePreset("prof_4", "Navy Admiral", "Professional", "#1E3A8A", "Serif", "#FFFFFF", "#111827", "#4B5563", false, "Academic", "Deep navy corporate layout with traditional editorial serif details.", "⚓"),
        TemplatePreset("prof_5", "Steel Executive", "Professional", "#64748B", "SansSerif", "#F1F5F9", "#1E293B", "#64748B", false, "Corporate", "Sleek metallic steel gray styling for executive leadership.", "⚙️"),
        TemplatePreset("prof_6", "Bordeaux Legacy", "Professional", "#881337", "Serif", "#FFF1F2", "#4C0519", "#881337", false, "Academic", "Elegant rich burgundy preset with scholarly credentials layout.", "🍷"),
        TemplatePreset("prof_7", "Bronze Wealth", "Professional", "#B45309", "SansSerif", "#FEF3C7", "#78350F", "#92400E", false, "Corporate", "Premium bronze orange accents with warm cream background.", "🪙"),
        TemplatePreset("prof_8", "Teal Enterprise", "Professional", "#0D9488", "SansSerif", "#F0FDFA", "#111827", "#4B5563", false, "Corporate", "Vibrant teal branding for fast-paced modern companies.", "🏢"),
        TemplatePreset("prof_9", "Charcoal Clean", "Professional", "#334155", "SansSerif", "#F8FAFC", "#0F172A", "#64748B", false, "Modern", "Muted charcoal and silver professional portfolio preset.", "⚖️"),
        TemplatePreset("prof_10", "Gold Standard", "Professional", "#D97706", "Serif", "#FFFFFF", "#1E293B", "#64748B", false, "Corporate", "Prestige golden accent borders for premium corporate CVs.", "🏆"),
        TemplatePreset("prof_11", "Oxford Finance", "Professional", "#0F172A", "Serif", "#FFFFFF", "#1E293B", "#475569", false, "Corporate", "Prestigious deep blue typography designed for financial experts.", "🏦"),
        TemplatePreset("prof_12", "Earthy Partner", "Professional", "#78350F", "SansSerif", "#FEFDFB", "#451A03", "#78350F", false, "Corporate", "Warm brown terracotta shades for landscape & organic architects.", "🪵"),
        TemplatePreset("prof_13", "Clinical Blue", "Professional", "#2563EB", "SansSerif", "#EFF6FF", "#1E3A8A", "#2563EB", false, "Light", "Clean bright blue accents designed for medical & scientific careers.", "🩺"),
        TemplatePreset("prof_14", "Plum Executive", "Professional", "#581C87", "SansSerif", "#FAF5FF", "#3B0764", "#581C87", false, "Corporate", "Rich plum imperial tone designed for strategic directors.", "👑"),
        TemplatePreset("prof_15", "Onyx Premium", "Professional", "#111827", "SansSerif", "#F9FAFB", "#111827", "#4B5563", false, "Modern", "Ultra-high contrast absolute black on white paper layout.", "🔲"),
        TemplatePreset("prof_16", "Burgundy Shield", "Professional", "#9D174D", "Serif", "#FDF2F8", "#500724", "#9D174D", false, "Academic", "Deep burgundy shield template for legal advisors & risk analysts.", "🛡️"),
        TemplatePreset("prof_17", "Urban Concrete", "Professional", "#4B5563", "SansSerif", "#F3F4F6", "#1F2937", "#4B5563", false, "Corporate", "Concrete matte gray detailing for structural engineers & project managers.", "🏗️"),
        TemplatePreset("prof_18", "Olive Authority", "Professional", "#3F6212", "SansSerif", "#F7FEE7", "#1A2E05", "#3F6212", false, "Corporate", "Deep olive and moss greens for agricultural & ecological directors.", "🌿"),
        TemplatePreset("prof_19", "Global Trade Navy", "Professional", "#172554", "SansSerif", "#EFF6FF", "#172554", "#1E40AF", false, "Corporate", "Dynamic trading navy blue for logistics and global commerce experts.", "🚢"),
        TemplatePreset("prof_20", "Crimson Counsel", "Professional", "#BE123C", "Serif", "#FFF1F2", "#4C0519", "#9F1239", false, "Academic", "Bold crimson tones for legal advocates, advisors and public relations specialists.", "📢"),

        // ====================================================================
        // CATEGORY: MINIMALIST (20 Templates)
        // ====================================================================
        TemplatePreset("min_1", "Snow White", "Minimalist", "#111827", "SansSerif", "#FFFFFF", "#111827", "#6B7280", false, "Minimal", "Zero fluff, ultra-white clean minimalist layout.", "❄️"),
        TemplatePreset("min_2", "Nordic Ash", "Minimalist", "#4B5563", "SansSerif", "#F9FAFB", "#1F2937", "#6B7280", false, "Minimal", "Scandinavian ash gray with muted details.", "🪵"),
        TemplatePreset("min_3", "Sand Cozy", "Minimalist", "#5C4033", "SansSerif", "#FDFBF7", "#2D241E", "#7A685D", false, "Minimal", "Cozy warm desert sand palette for minimalist designers.", "🏜️"),
        TemplatePreset("min_4", "Ivory Soft", "Minimalist", "#374151", "Serif", "#FAF9F6", "#111827", "#4B5563", false, "Minimal", "Ivory paper texture feel with traditional text rendering.", "🍦"),
        TemplatePreset("min_5", "Charcoal Draft", "Minimalist", "#1E293B", "Monospace", "#F8FAFC", "#0F172A", "#64748B", false, "Minimal", "Raw typewriter monospace draft styling with light gray background.", "⌨️"),
        TemplatePreset("min_6", "Linen Clean", "Minimalist", "#4A4A4A", "SansSerif", "#FDFDFD", "#1A1A1A", "#707070", false, "Minimal", "Soft organic linen gray with subtle visual accents.", "🪡"),
        TemplatePreset("min_7", "Pebble Grey", "Minimalist", "#4B5563", "SansSerif", "#F3F4F6", "#1F2937", "#6B7280", false, "Minimal", "River pebble soft slate accents and spacing.", "🪨"),
        TemplatePreset("min_8", "Warm Wheat", "Minimalist", "#78350F", "Serif", "#FEFDFB", "#451A03", "#78350F", false, "Minimal", "Wheat field organic text presentation for authors.", "🌾"),
        TemplatePreset("min_9", "Muted Olive", "Minimalist", "#3F4E3E", "SansSerif", "#F4F7F4", "#242B24", "#5F6E5E", false, "Minimal", "Dull serene olive greens for quiet botanical professionals.", "🫒"),
        TemplatePreset("min_10", "Foggy Morning", "Minimalist", "#475569", "SansSerif", "#F1F5F9", "#1E293B", "#64748B", false, "Minimal", "Soft early morning mist tones with high readability.", "🌫️"),
        TemplatePreset("min_11", "Warm Clay", "Minimalist", "#9A3412", "SansSerif", "#FFF7ED", "#431407", "#9A3412", false, "Minimal", "Earthy clay colors for visual sculptors and crafters.", "🏺"),
        TemplatePreset("min_12", "Pure Alabaster", "Minimalist", "#000000", "SansSerif", "#FAFAFA", "#171717", "#737373", false, "Minimal", "High contrast black ink on pure alabaster backing.", "🥛"),
        TemplatePreset("min_13", "Muted Lavender", "Minimalist", "#6D28D9", "SansSerif", "#FAF5FF", "#1E1B4B", "#6B7280", false, "Minimal", "A subtle lavender touch for soft minimalist digital products.", "🪻"),
        TemplatePreset("min_14", "Silver Ore", "Minimalist", "#374151", "SansSerif", "#F9FAFB", "#111827", "#6B7280", false, "Minimal", "Sleek silver margins and light structural rules.", "🪙"),
        TemplatePreset("min_15", "Matcha Latte", "Minimalist", "#166534", "SansSerif", "#F0FDF4", "#14532D", "#166534", false, "Minimal", "Clean matcha green accents over cream white layout.", "🍵"),
        TemplatePreset("min_16", "Dune Whisper", "Minimalist", "#7C2D12", "Serif", "#FFFDFA", "#431407", "#7C2D12", false, "Minimal", "Dry desert sand whispers with literary serif detailing.", "🏜️"),
        TemplatePreset("min_17", "Silent Slate", "Minimalist", "#1E293B", "SansSerif", "#F1F5F9", "#0F172A", "#475569", false, "Minimal", "Perfect light slate aesthetic for modern visual artists.", "🛸"),
        TemplatePreset("min_18", "Tuscan Sun", "Minimalist", "#B45309", "SansSerif", "#FFFBEB", "#78350F", "#B45309", false, "Minimal", "Sunny warmth minimalist yellow hues.", "☀️"),
        TemplatePreset("min_19", "Polar Ice", "Minimalist", "#0891B2", "SansSerif", "#F0FDFA", "#155E75", "#0891B2", false, "Minimal", "Fresh light cyan accent lines mimicking ice crystals.", "🧊"),
        TemplatePreset("min_20", "Chalkboard Draft", "Minimalist", "#E2E8F0", "Monospace", "#0F172A", "#F1F5F9", "#94A3B8", true, "Minimal", "Clean chalk monospace writing on dark slate backing.", "🖍️"),

        // ====================================================================
        // CATEGORY: TECH & DEVELOPER (20 Templates)
        // ====================================================================
        TemplatePreset("dev_1", "Kotlin Jet", "Tech & Developer", "#7F52FF", "SansSerif", "#09090B", "#F4F4F5", "#A1A1AA", true, "Developer", "Vibrant JetBrains purple/orange developer aesthetic.", "🚀"),
        TemplatePreset("dev_2", "Matrix Code", "Tech & Developer", "#22C55E", "Monospace", "#022C22", "#F0FDF4", "#4ADE80", true, "Developer", "Classic green hacker matrix typewriter console style.", "📟"),
        TemplatePreset("dev_3", "Cyberpunk Neon", "Tech & Developer", "#F43F5E", "Monospace", "#0F051D", "#FFFFFF", "#F43F5E", true, "Developer", "Bold neon pink with cyber midnight grid layout.", "🤖"),
        TemplatePreset("dev_4", "Cobalt IDE", "Tech & Developer", "#38BDF8", "SansSerif", "#0B1528", "#E2E8F0", "#38BDF8", true, "Developer", "Deep indigo/blue IDE theme for modern cloud engineers.", "💻"),
        TemplatePreset("dev_5", "GitHub Dark", "Tech & Developer", "#F3F4F6", "SansSerif", "#0D1117", "#C9D1D9", "#8B949E", true, "Developer", "Official open source style layout with charcoal panels.", "🐙"),
        TemplatePreset("dev_6", "Rust Oxide", "Tech & Developer", "#EA580C", "Monospace", "#1A1513", "#F5F5F4", "#EA580C", true, "Developer", "Rusty copper orange styled preset for systems engineers.", "🦀"),
        TemplatePreset("dev_7", "Terminal Console", "Tech & Developer", "#E2E8F0", "Monospace", "#000000", "#00FF00", "#A3E635", true, "Developer", "Old school monochrome green phosphor terminal screen.", "🖥️"),
        TemplatePreset("dev_8", "Android Bot", "Tech & Developer", "#3DDC84", "SansSerif", "#070F0E", "#F1FAEE", "#3DDC84", true, "Developer", "Official modern android green and black branding theme.", "🤖"),
        TemplatePreset("dev_9", "Vaporwave Neon", "Tech & Developer", "#E879F9", "Monospace", "#180028", "#F5F3FF", "#A78BFA", true, "Developer", "Retro-future synthwave dream violet gradient preset.", "🌆"),
        TemplatePreset("dev_10", "Cloud DevOps", "Tech & Developer", "#0284C7", "SansSerif", "#0C4A6E", "#F0F9FF", "#38BDF8", true, "Developer", "Azure-blue styled devops & cloud native portfolio.", "☁️"),
        TemplatePreset("dev_11", "Dracula Theme", "Tech & Developer", "#BD93F9", "Monospace", "#282A36", "#F8F8F2", "#8BE9FD", true, "Developer", "Famous programmer theme with purple and cyan accents.", "🧛"),
        TemplatePreset("dev_12", "Material Ocean", "Tech & Developer", "#00E5FF", "SansSerif", "#263238", "#ECEFF1", "#80DEEA", true, "Developer", "Ocean deep blue teal styling for front-end developers.", "🌊"),
        TemplatePreset("dev_13", "Python Snake", "Tech & Developer", "#FFE082", "SansSerif", "#1E293B", "#ECEFF1", "#4B8BBE", true, "Developer", "Yellow and blue python programming language branding layout.", "🐍"),
        TemplatePreset("dev_14", "Git Branch", "Tech & Developer", "#F1502F", "Monospace", "#18181B", "#FAFAFA", "#A1A1AA", true, "Developer", "Branching red colorway inspired by git workflows.", "🌿"),
        TemplatePreset("dev_15", "Cyber Orange", "Tech & Developer", "#F97316", "SansSerif", "#18181B", "#F5F5F5", "#F97316", true, "Developer", "Hi-vis neon orange warning style on charcoal base.", "☣️"),
        TemplatePreset("dev_16", "Deep Space", "Tech & Developer", "#EC4899", "SansSerif", "#03001C", "#ECEFF1", "#D63484", true, "Developer", "Nebula space cosmic dust magenta and deep dark purple.", "🌌"),
        TemplatePreset("dev_17", "Monokai Retro", "Tech & Developer", "#F92672", "Monospace", "#272822", "#F8F8F2", "#A6E22E", true, "Developer", "Legendary text-editor color scheme with lime & pink.", "📟"),
        TemplatePreset("dev_18", "One Dark Pro", "Tech & Developer", "#61AFEF", "SansSerif", "#282C34", "#ABB2BF", "#5C6370", true, "Developer", "Clean classic Atom-inspired VSCode styling.", "⚙️"),
        TemplatePreset("dev_19", "Solarized Dark", "Tech & Developer", "#2AA198", "Monospace", "#002B36", "#839496", "#93A1A1", true, "Developer", "Low contrast solarized teal preset, gentle on eyes.", "🕶️"),
        TemplatePreset("dev_20", "AI Prompt Core", "Tech & Developer", "#F472B6", "SansSerif", "#111827", "#F3F4F6", "#F472B6", true, "Developer", "Prompt engineer layout with AI magenta circuitry details.", "🧠"),

        // ====================================================================
        // CATEGORY: CREATIVE & ARTS (20 Templates)
        // ====================================================================
        TemplatePreset("cr_1", "Sunset Clay", "Creative", "#E11D48", "SansSerif", "#FFF1F2", "#4C0519", "#E11D48", false, "Creative", "Vibrant summer sunset clay palette for visual arts.", "🎨"),
        TemplatePreset("cr_2", "Lavender Dream", "Creative", "#7C3AED", "Serif", "#F5F3FF", "#2E1065", "#7C3AED", false, "Creative", "Dreamy pastel lavender styling for content writers.", "🪻"),
        TemplatePreset("cr_3", "Lemon Zest", "Creative", "#CA8A04", "SansSerif", "#FEF08A", "#422006", "#854D0E", false, "Creative", "Playful hyper-yellow theme for copywriters & creators.", "🍋"),
        TemplatePreset("cr_4", "Graffiti Pop", "Creative", "#EC4899", "SansSerif", "#FCE7F3", "#500724", "#DB2777", false, "Creative", "Street-art graffiti vibes with high-contrast magenta.", "🎨"),
        TemplatePreset("cr_5", "Coral Splash", "Creative", "#F97316", "SansSerif", "#FFEDD5", "#7C2D12", "#EA580C", false, "Creative", "Bright coral red details, warm beach portfolio vibe.", "🐠"),
        TemplatePreset("cr_6", "Forest Moss", "Creative", "#166534", "Serif", "#F0FDF4", "#14532D", "#166534", false, "Creative", "Serene moss green editorial presentation for florists.", "🌲"),
        TemplatePreset("cr_7", "Wild Orchid", "Creative", "#C084FC", "SansSerif", "#FAF5FF", "#3B0764", "#A855F7", false, "Creative", "Exotic purple orchid layout for visual models & actors.", "🌸"),
        TemplatePreset("cr_8", "Berry Sorbet", "Creative", "#DB2777", "SansSerif", "#FDF2F8", "#4C0519", "#C2185B", false, "Creative", "Sweet pink berry theme for fashion and styling designers.", "🍓"),
        TemplatePreset("cr_9", "Mango Tango", "Creative", "#F59E0B", "SansSerif", "#FFFBEB", "#78350F", "#D97706", false, "Creative", "Spicy mango orange layout for chefs and travel bloggers.", "🥭"),
        TemplatePreset("cr_10", "Plum Royal", "Creative", "#7E22CE", "Serif", "#FAF5FF", "#3B0764", "#7E22CE", false, "Creative", "Royal violet typography designed for jewelry & luxury designers.", "💎"),
        TemplatePreset("cr_11", "Neo Mint", "Creative", "#10B981", "SansSerif", "#ECFDF5", "#064E3B", "#059669", false, "Creative", "Hyper-modern fresh mint look for UX researchers.", "🍃"),
        TemplatePreset("cr_12", "Apricot Warm", "Creative", "#FB923C", "SansSerif", "#FFF7ED", "#7C2D12", "#EA580C", false, "Creative", "Juicy light apricot colors for food photographers.", "🍑"),
        TemplatePreset("cr_13", "Cherry Blossom", "Creative", "#F472B6", "Serif", "#FFF5F5", "#6B21A8", "#DB2777", false, "Creative", "Flowery sakura pastel tones for lifestyle planners.", "🌸"),
        TemplatePreset("cr_14", "Deep Mahogany", "Creative", "#451A03", "Serif", "#FFFDF9", "#270E02", "#78350F", false, "Creative", "Rich mahogany wood-tones layout for luxury carpenters.", "🪵"),
        TemplatePreset("cr_15", "Oasis Teal", "Creative", "#0F766E", "SansSerif", "#F0FDFA", "#115E59", "#0F766E", false, "Creative", "Serene teal waters styling for travel guides & experts.", "🌴"),
        TemplatePreset("cr_16", "Peacock Blue", "Creative", "#0369A1", "SansSerif", "#F0F9FF", "#075985", "#0369A1", false, "Creative", "Brilliant deep teal-blue styled for interior designers.", "🦚"),
        TemplatePreset("cr_17", "Midnight Rose", "Creative", "#FDA4AF", "SansSerif", "#1E1B4B", "#FFF1F2", "#FB7185", true, "Creative", "Deep nocturnal violet with soft rose highlight borders.", "🌹"),
        TemplatePreset("cr_18", "Spiced Chai", "Creative", "#B45309", "Serif", "#FFFBEB", "#451A03", "#92400E", false, "Creative", "Chai cafe warm color palette for local baristas.", "☕"),
        TemplatePreset("cr_19", "Grape Soda", "Creative", "#6D28D9", "SansSerif", "#F5F3FF", "#4C1D95", "#8B5CF6", false, "Creative", "Youthful grape purple preset for stream creators.", "🥤"),
        TemplatePreset("cr_20", "Cyberpunk Punk", "Creative", "#F43F5E", "Monospace", "#1A001A", "#FFECFE", "#F43F5E", true, "Creative", "Hot pink punk visual styling for music bands.", "🎸"),

        // ====================================================================
        // CATEGORY: ACADEMIC (25 Templates)
        // ====================================================================
        TemplatePreset("acad_1", "Crimson Scholar", "Academic", "#991B1B", "Serif", "#FFF8F8", "#450A0A", "#991B1B", false, "Academic", "Crimson academic colors for professors & researchers.", "🎓"),
        TemplatePreset("acad_2", "Ivy League", "Academic", "#14532D", "Serif", "#F4FDF4", "#052E16", "#166534", false, "Academic", "Classic ivy green borders for institutional researchers.", "🌿"),
        TemplatePreset("acad_3", "Oxford Blue", "Academic", "#1E3A8A", "Serif", "#FFFFFF", "#111827", "#1E3A8A", false, "Academic", "Traditional prestigious Oxford navy style layout.", "🏛️"),
        TemplatePreset("acad_4", "Cambridge crest", "Academic", "#0F766E", "Serif", "#F0FDFA", "#115E59", "#0F766E", false, "Academic", "Elegant teal institutional preset for PhD graduates.", "📜"),
        TemplatePreset("acad_5", "Textbook Editorial", "Academic", "#111827", "Serif", "#FCFBF9", "#111827", "#4B5563", false, "Academic", "Classic typewriter style black ink layout.", "📖"),
        TemplatePreset("acad_6", "Heritage Gold", "Academic", "#854D0E", "Serif", "#FEFCE8", "#422006", "#854D0E", false, "Academic", "Golden wheat borders for history and archaeology scholars.", "🏺"),
        TemplatePreset("acad_7", "Bordeaux Professor", "Academic", "#881337", "Serif", "#FFF1F2", "#4C0519", "#881337", false, "Academic", "Deep burgundy scholar aesthetic for medical journals.", "🧪"),
        TemplatePreset("acad_8", "Science Lab", "Academic", "#0284C7", "SansSerif", "#F0F9FF", "#0C4A6E", "#0284C7", false, "Academic", "Vibrant science lab blue for biology and chemistry researchers.", "🧪"),
        TemplatePreset("acad_9", "Old Parchment", "Academic", "#78350F", "Serif", "#FDFBF7", "#451A03", "#78350F", false, "Academic", "Aesthetic yellowish ancient parchment for classic philosophers.", "📜"),
        TemplatePreset("acad_10", "Mathematic Slate", "Academic", "#374151", "Monospace", "#F9FAFB", "#111827", "#374151", false, "Academic", "Clean slate layout for physicists & mathematicians.", "📐"),
        TemplatePreset("acad_11", "Yale Classic", "Academic", "#0A2342", "Serif", "#FFFFFF", "#0A2342", "#4A607A", false, "Academic", "Distinguished dark blue theme for legal and business schools.", "🏛️"),
        TemplatePreset("acad_12", "Ecology Green", "Academic", "#064E3B", "Serif", "#ECFDF5", "#064E3B", "#047857", false, "Academic", "Prestigious dark green for botany & forestry scholars.", "🍃"),
        TemplatePreset("acad_13", "Astronomy Night", "Academic", "#1E1B4B", "SansSerif", "#FAF5FF", "#111827", "#312E81", false, "Academic", "Deep star-blue tones for astrophysics investigators.", "🔭"),
        TemplatePreset("acad_14", "Vintage Sepia", "Academic", "#5C4033", "Serif", "#FAF0E6", "#2D1E18", "#5C4033", false, "Academic", "Historical sepia tones for document archival curators.", "🎞️"),
        TemplatePreset("acad_15", "Engineering Navy", "Academic", "#1D4ED8", "SansSerif", "#EFF6FF", "#1E3A8A", "#1D4ED8", false, "Academic", "High contrast engineering blueprints theme.", "📐"),
        TemplatePreset("acad_16", "Stanford Red", "Academic", "#8C1515", "Serif", "#FFFFFF", "#2E2D29", "#8C1515", false, "Academic", "Bright classic red tones inspired by prestigious tech centers.", "🌲"),
        TemplatePreset("acad_17", "MIT Grey", "Academic", "#8A1B2F", "SansSerif", "#F5F5F5", "#2D2D2D", "#8A1B2F", false, "Academic", "High-contrast technical cardinal red on steel gray background.", "🔬"),
        TemplatePreset("acad_18", "UCLA Blue", "Academic", "#2774AE", "SansSerif", "#F2FAFF", "#003B5C", "#2774AE", false, "Academic", "Bright blue and golden yellow layout for athletic coaches.", "🐻"),
        TemplatePreset("acad_19", "Sorbonne Burgundy", "Academic", "#581C87", "Serif", "#FDF4FF", "#2E0854", "#581C87", false, "Academic", "Elegant French literary purple and burgundy details.", "📚"),
        TemplatePreset("acad_20", "Chicago Maroon", "Academic", "#76232F", "Serif", "#FFF1F3", "#3C1014", "#76232F", false, "Academic", "Elite maroon theme for economic policy analysts & professors.", "📈"),
        TemplatePreset("acad_21", "Princeton Orange", "Academic", "#E7720D", "Serif", "#FFFBF7", "#222222", "#E7720D", false, "Academic", "Iconic bright orange details over traditional text formatting.", "🐯"),
        TemplatePreset("acad_22", "Columbia Cyan", "Academic", "#9BCBEB", "SansSerif", "#F6FAFD", "#002D62", "#4A82B8", false, "Academic", "Crisp light blue details for journalism and media graduates.", "🦁"),
        TemplatePreset("acad_23", "Berkeley Gold", "Academic", "#003262", "Serif", "#F8FAFC", "#003262", "#C28800", false, "Academic", "Deep blue with golden highlight borders for Nobel scholars.", "🐻"),
        TemplatePreset("acad_24", "Cornell Red", "Academic", "#B31B1B", "Serif", "#FFFFFF", "#111111", "#B31B1B", false, "Academic", "Warm carnation red branding for hospitality & agricultural leaders.", "🐻"),
        TemplatePreset("acad_25", "Dartmouth Green", "Academic", "#00693E", "Serif", "#F2F9F6", "#001E11", "#00693E", false, "Academic", "Dark green design styled for ecological, earth and outdoor fields.", "🌲")
    )
}
