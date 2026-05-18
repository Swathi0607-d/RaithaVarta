package com.raithavarta.app.data

data class Tip(
    val id: Int,
    val cropEn: String,
    val cropKn: String,
    val emoji: String,
    val bgColor: Int,
    val actionEn: String,
    val actionKn: String,
    val detailEn: String,
    val detailKn: String,
    val tagEn: String,
    val tagKn: String,
    val timeAgoEn: String,
    val timeAgoKn: String
)

data class StoryItem(
    val id: Int,
    val avatar: String,
    val nameEn: String,
    val nameKn: String,
    val locEn: String,
    val locKn: String,
    val textEn: String,
    val textKn: String,
    val resultEn: String,
    val resultKn: String
)

data class CropOption(
    val id: String,
    val emoji: String,
    val nameEn: String,
    val nameKn: String,
    val hintEn: String,
    val hintKn: String
)

data class SettingItem(
    val id: String,
    val icon: String,
    val labelRes: Int,
    val subRes: Int,
    val dialogTitleRes: Int,
    val dialogMsgRes: Int
)
