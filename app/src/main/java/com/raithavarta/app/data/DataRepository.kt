package com.raithavarta.app.data

import android.graphics.Color
import com.raithavarta.app.R

object DataRepository {

    val tips: List<Tip> = listOf(
        Tip(
            id = 0,
            cropEn = "PADDY",
            cropKn = "ಭತ್ತ",
            emoji = "🌾",
            bgColor = Color.parseColor("#E8F5E9"),
            actionEn = "Water Management for Paddy",
            actionKn = "ಭತ್ತಕ್ಕೆ ನೀರು ನಿರ್ವಹಣೆ",
            detailEn = "Maintain 5cm water level in paddy fields during tillering stage. Drain fields for 2 days before harvest to ease cutting.",
            detailKn = "ಕುಳ್ಳು ಹಂತದಲ್ಲಿ ಭತ್ತದ ಗದ್ದೆಯಲ್ಲಿ 5cm ನೀರಿನ ಮಟ್ಟ ಕಾಯ್ದುಕೊಳ್ಳಿ. ಕೊಯ್ಲಿಗೆ 2 ದಿನ ಮೊದಲು ಗದ್ದೆ ಬರಿದು ಮಾಡಿ.",
            tagEn = "💧 IRRIGATION",
            tagKn = "💧 ನೀರಾವರಿ",
            timeAgoEn = "1 hour ago",
            timeAgoKn = "1 ಗಂಟೆ ಹಿಂದೆ"
        ),
        Tip(
            id = 1,
            cropEn = "TOMATO",
            cropKn = "ಟೊಮೇಟೊ",
            emoji = "🍅",
            bgColor = Color.parseColor("#FFE4E1"),
            actionEn = "Spray Neem Oil This Week",
            actionKn = "ಈ ವಾರ ಬೇವಿನ ಎಣ್ಣೆ ಸಿಂಪಡಿಸಿ",
            detailEn = "High humidity expected. Mix 5ml neem oil per litre of water and spray in evening hours to prevent fungal disease.",
            detailKn = "ಹೆಚ್ಚಿನ ತೇವಾಂಶ ನಿರೀಕ್ಷಿಸಲಾಗಿದೆ. ಪ್ರತಿ ಲೀಟರ್ ನೀರಿಗೆ 5ml ಬೇವಿನ ಎಣ್ಣೆ ಬೆರೆಸಿ ಸಂಜೆಯ ಸಮಯದಲ್ಲಿ ಸಿಂಪಡಿಸಿ.",
            tagEn = "🌿 ORGANIC",
            tagKn = "🌿 ಸಾವಯವ",
            timeAgoEn = "2 hours ago",
            timeAgoKn = "2 ಗಂಟೆಗಳ ಹಿಂದೆ"
        ),
        Tip(
            id = 2,
            cropEn = "RAGI",
            cropKn = "ರಾಗಿ",
            emoji = "🌾",
            bgColor = Color.parseColor("#FFF4E1"),
            actionEn = "Best Time to Sow Ragi",
            actionKn = "ರಾಗಿ ಬಿತ್ತನೆಗೆ ಸೂಕ್ತ ಸಮಯ",
            detailEn = "Pre-monsoon showers ideal for ragi sowing. Use ML-365 variety for higher yield in your district.",
            detailKn = "ಮಾನ್ಸೂನ್ ಮುನ್ನ ಮಳೆ ರಾಗಿ ಬಿತ್ತನೆಗೆ ಸೂಕ್ತ. ನಿಮ್ಮ ಜಿಲ್ಲೆಯಲ್ಲಿ ಹೆಚ್ಚಿನ ಇಳುವರಿಗಾಗಿ ML-365 ತಳಿಯನ್ನು ಬಳಸಿ.",
            tagEn = "🌱 SEEDS",
            tagKn = "🌱 ಬೀಜ",
            timeAgoEn = "5 hours ago",
            timeAgoKn = "5 ಗಂಟೆಗಳ ಹಿಂದೆ"
        ),
        Tip(
            id = 3,
            cropEn = "COCONUT",
            cropKn = "ತೆಂಗಿನಕಾಯಿ",
            emoji = "🥥",
            bgColor = Color.parseColor("#E1F5E4"),
            actionEn = "Apply Potash Fertiliser",
            actionKn = "ಪೊಟ್ಯಾಷ್ ಗೊಬ್ಬರ ಹಾಕಿ",
            detailEn = "Add 1.5kg muriate of potash per palm to improve nut size. Best applied before next rainfall.",
            detailKn = "ಕಾಯಿ ಗಾತ್ರ ಸುಧಾರಿಸಲು ಪ್ರತಿ ಮರಕ್ಕೆ 1.5kg ಮ್ಯೂರಿಯೇಟ್ ಆಫ್ ಪೊಟ್ಯಾಷ್ ಸೇರಿಸಿ. ಮುಂದಿನ ಮಳೆಯ ಮೊದಲು ಹಾಕುವುದು ಉತ್ತಮ.",
            tagEn = "🌴 FERTILISER",
            tagKn = "🌴 ಗೊಬ್ಬರ",
            timeAgoEn = "1 day ago",
            timeAgoKn = "1 ದಿನದ ಹಿಂದೆ"
        ),
        Tip(
            id = 4,
            cropEn = "SUGARCANE",
            cropKn = "ಕಬ್ಬು",
            emoji = "🎋",
            bgColor = Color.parseColor("#E1F0FF"),
            actionEn = "Drip Irrigation Schedule",
            actionKn = "ಹನಿ ನೀರಾವರಿ ವೇಳಾಪಟ್ಟಿ",
            detailEn = "Run drip system 2 hours daily for next 10 days. Save 40% water vs flooding method.",
            detailKn = "ಮುಂದಿನ 10 ದಿನಗಳ ಕಾಲ ಪ್ರತಿದಿನ 2 ಗಂಟೆ ಹನಿ ವ್ಯವಸ್ಥೆ ಚಲಾಯಿಸಿ. ಪ್ರವಾಹ ವಿಧಾನಕ್ಕಿಂತ 40% ನೀರು ಉಳಿತಾಯ.",
            tagEn = "💧 WATER",
            tagKn = "💧 ನೀರು",
            timeAgoEn = "1 day ago",
            timeAgoKn = "1 ದಿನದ ಹಿಂದೆ"
        ),
        Tip(
            id = 5,
            cropEn = "MARKET PRICE",
            cropKn = "ಮಾರುಕಟ್ಟೆ ಬೆಲೆ",
            emoji = "💰",
            bgColor = Color.parseColor("#FFF8E1"),
            actionEn = "Onion Prices Up 15%",
            actionKn = "ಈರುಳ್ಳಿ ಬೆಲೆ 15% ಏರಿಕೆ",
            detailEn = "Hubli APMC: ₹38/kg today. Good time to sell stored stock. Prices may stabilise next week.",
            detailKn = "ಹುಬ್ಬಳ್ಳಿ APMC: ಇಂದು ₹38/kg. ಸಂಗ್ರಹಿಸಿದ ಸ್ಟಾಕ್ ಮಾರಲು ಉತ್ತಮ ಸಮಯ. ಮುಂದಿನ ವಾರ ಬೆಲೆ ಸ್ಥಿರವಾಗಬಹುದು.",
            tagEn = "📈 MARKET",
            tagKn = "📈 ಮಾರುಕಟ್ಟೆ",
            timeAgoEn = "3 hours ago",
            timeAgoKn = "3 ಗಂಟೆಗಳ ಹಿಂದೆ"
        ),
        Tip(
            id = 6,
            cropEn = "BANANA",
            cropKn = "ಬಾಳೆಹಣ್ಣು",
            emoji = "🍌",
            bgColor = Color.parseColor("#FFF9C4"),
            actionEn = "Watch for Panama Wilt",
            actionKn = "ಪನಾಮ ವಿಲ್ಟ್ ಬಗ್ಗೆ ಎಚ್ಚರ",
            detailEn = "Yellow leaves on lower part = early sign. Apply Trichoderma viride 50g per plant immediately.",
            detailKn = "ಕೆಳಭಾಗದಲ್ಲಿ ಹಳದಿ ಎಲೆಗಳು = ಆರಂಭಿಕ ಚಿಹ್ನೆ. ಪ್ರತಿ ಸಸ್ಯಕ್ಕೆ ತಕ್ಷಣ 50g ಟ್ರೈಕೋಡರ್ಮಾ ವಿರಿಡೆ ಹಾಕಿ.",
            tagEn = "⚠️ DISEASE",
            tagKn = "⚠️ ರೋಗ",
            timeAgoEn = "6 hours ago",
            timeAgoKn = "6 ಗಂಟೆಗಳ ಹಿಂದೆ"
        )
    )

    val stories: List<StoryItem> = listOf(
        StoryItem(
            id = 1,
            avatar = "👨‍🌾",
            nameEn = "Ramesh K.",
            nameKn = "ರಮೇಶ್ ಕೆ.",
            locEn = "Mysuru District",
            locKn = "ಮೈಸೂರು ಜಿಲ್ಲೆ",
            textEn = "\"Used neem oil tip last month. My tomato crop saved from blight. ₹50,000 income safe!\"",
            textKn = "\"ಕಳೆದ ತಿಂಗಳು ಬೇವಿನ ಎಣ್ಣೆ ಸಲಹೆ ಬಳಸಿದೆ. ನನ್ನ ಟೊಮೇಟೊ ಬೆಳೆ ರೋಗದಿಂದ ಉಳಿಸಿತು. ₹50,000 ಆದಾಯ ಸುರಕ್ಷಿತ!\"",
            resultEn = "✓ ₹50,000 SAVED",
            resultKn = "✓ ₹50,000 ಉಳಿತಾಯ"
        ),
        StoryItem(
            id = 2,
            avatar = "👩‍🌾",
            nameEn = "Lakshmi B.",
            nameKn = "ಲಕ್ಷ್ಮಿ ಬಿ.",
            locEn = "Belagavi District",
            locKn = "ಬೆಳಗಾವಿ ಜಿಲ್ಲೆ",
            textEn = "\"Drip irrigation tip helped me save water. Sugarcane yield up 25% this season!\"",
            textKn = "\"ಹನಿ ನೀರಾವರಿ ಸಲಹೆ ನೀರು ಉಳಿಸಲು ಸಹಾಯ ಮಾಡಿತು. ಈ ಋತುವಿನಲ್ಲಿ ಕಬ್ಬಿನ ಇಳುವರಿ 25% ಹೆಚ್ಚಾಗಿದೆ!\"",
            resultEn = "✓ +25% YIELD",
            resultKn = "✓ +25% ಇಳುವರಿ"
        ),
        StoryItem(
            id = 3,
            avatar = "👨‍🌾",
            nameEn = "Suresh M.",
            nameKn = "ಸುರೇಶ್ ಎಂ.",
            locEn = "Tumakuru District",
            locKn = "ತುಮಕೂರು ಜಿಲ್ಲೆ",
            textEn = "\"AI leaf diagnosis caught early blight on my ragi crop. Treated in 2 days, saved 80% of harvest.\"",
            textKn = "\"AI ಎಲೆ ಪರೀಕ್ಷೆ ನನ್ನ ರಾಗಿ ಬೆಳೆಯಲ್ಲಿ ಆರಂಭಿಕ ರೋಗ ಪತ್ತೆ ಹಚ್ಚಿತು. 2 ದಿನಗಳಲ್ಲಿ ಚಿಕಿತ್ಸೆ ನೀಡಿ 80% ಬೆಳೆ ಉಳಿಸಿದೆ.\"",
            resultEn = "✓ 80% HARVEST SAVED",
            resultKn = "✓ 80% ಬೆಳೆ ಉಳಿತಾಯ"
        ),
        StoryItem(
            id = 4,
            avatar = "👩‍🌾",
            nameEn = "Anitha P.",
            nameKn = "ಅನಿತಾ ಪಿ.",
            locEn = "Hassan District",
            locKn = "ಹಾಸನ ಜಿಲ್ಲೆ",
            textEn = "\"Followed the coconut nutrition tip. Yield jumped from 60 to 95 nuts per tree this year.\"",
            textKn = "\"ತೆಂಗಿನ ಪೋಷಣೆ ಸಲಹೆ ಅನುಸರಿಸಿದೆ. ಪ್ರತಿ ಮರಕ್ಕೆ ಇಳುವರಿ 60 ರಿಂದ 95 ಕಾಯಿಗಳಿಗೆ ಏರಿತು.\"",
            resultEn = "✓ +58% YIELD",
            resultKn = "✓ +58% ಇಳುವರಿ"
        ),
        StoryItem(
            id = 5,
            avatar = "👨‍🌾",
            nameEn = "Mahesh G.",
            nameKn = "ಮಹೇಶ್ ಜಿ.",
            locEn = "Davangere District",
            locKn = "ದಾವಣಗೆರೆ ಜಿಲ್ಲೆ",
            textEn = "\"Market price alert told me to sell onions at peak. Made ₹35,000 extra this season.\"",
            textKn = "\"ಮಾರುಕಟ್ಟೆ ಬೆಲೆ ಎಚ್ಚರಿಕೆ ಗರಿಷ್ಠ ಸಮಯದಲ್ಲಿ ಈರುಳ್ಳಿ ಮಾರಲು ಹೇಳಿತು. ಈ ಋತುವಿನಲ್ಲಿ ₹35,000 ಹೆಚ್ಚುವರಿ ಗಳಿಸಿದೆ.\"",
            resultEn = "✓ +₹35,000 EXTRA",
            resultKn = "✓ +₹35,000 ಹೆಚ್ಚುವರಿ"
        ),
        StoryItem(
            id = 6,
            avatar = "👩‍🌾",
            nameEn = "Kavitha R.",
            nameKn = "ಕವಿತಾ ಆರ್.",
            locEn = "Chikkamagaluru District",
            locKn = "ಚಿಕ್ಕಮಗಳೂರು ಜಿಲ್ಲೆ",
            textEn = "\"Banana wilt warning came just in time. Used Trichoderma as suggested. Saved 200 plants!\"",
            textKn = "\"ಬಾಳೆ ವಿಲ್ಟ್ ಎಚ್ಚರಿಕೆ ಸಮಯಕ್ಕೆ ಸರಿಯಾಗಿ ಬಂತು. ಸೂಚಿಸಿದಂತೆ ಟ್ರೈಕೋಡರ್ಮಾ ಬಳಸಿದೆ. 200 ಸಸ್ಯಗಳು ಉಳಿದವು!\"",
            resultEn = "✓ 200 PLANTS SAVED",
            resultKn = "✓ 200 ಸಸ್ಯಗಳು ಉಳಿತಾಯ"
        )
    )

    val cropOptions: List<CropOption> = listOf(
        CropOption("paddy", "🌾", "Paddy", "ಭತ್ತ", "Plant/Grain", "ಸಸ್ಯ/ಧಾನ್ಯ"),
        CropOption("tomato", "🍅", "Tomato", "ಟೊಮೇಟೊ", "Leaf/Fruit", "ಎಲೆ/ಹಣ್ಣು"),
        CropOption("ragi", "🌾", "Ragi", "ರಾಗಿ", "Plant/Stem", "ಸಸ್ಯ/ಕಾಂಡ"),
        CropOption("coconut", "🥥", "Coconut", "ತೆಂಗಿನಕಾಯಿ", "Tree/Nut", "ಮರ/ಕಾಯಿ"),
        CropOption("banana", "🍌", "Banana", "ಬಾಳೆಹಣ್ಣು", "Leaf/Fruit", "ಎಲೆ/ಹಣ್ಣು"),
        CropOption("sugarcane", "🎋", "Sugarcane", "ಕಬ್ಬು", "Stem/Leaf", "ಕಾಂಡ/ಎಲೆ"),
        CropOption("onion", "🧅", "Onion", "ಈರುಳ್ಳಿ", "Bulb/Leaf", "ಗಡ್ಡೆ/ಎಲೆ")
    )

    val settings: List<SettingItem> = listOf(
        SettingItem("district", "📍", R.string.set_district, R.string.set_district_sub,
            R.string.set_district, R.string.dialog_district_msg),
        SettingItem("crops", "🌱", R.string.set_crops, R.string.set_crops_sub,
            R.string.set_crops, R.string.dialog_crops_msg),
        SettingItem("notif", "🔔", R.string.set_notif, R.string.set_notif_sub,
            R.string.set_notif, R.string.dialog_notif_msg),
        SettingItem("offline", "📥", R.string.set_offline, R.string.set_offline_sub,
            R.string.set_offline, R.string.dialog_offline_msg),
        SettingItem("kvk", "👨‍🏫", R.string.set_kvk, R.string.set_kvk_sub,
            R.string.set_kvk, R.string.dialog_kvk_msg),
        SettingItem("about", "ℹ️", R.string.set_about, R.string.set_about_sub,
            R.string.set_about, R.string.dialog_about_msg)
    )
}
