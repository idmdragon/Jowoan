package com.example.jowoan.models.lesson

import com.google.gson.annotations.SerializedName

data class Lesson(
    val ID: Int,
    @SerializedName("content_id") val contentID: Int,
    val penjelasan: Penjelasan?,
    val tips: Tips?,
    val hafalan: Hafalan?,
    @SerializedName("pilih_kata") val pilihKata: PilihKata?,
    val berbicara: Berbicara?,
    @SerializedName("benar_salah") val benarSalah: BenarSalah?,
    val result: LessonResult?,
    @SerializedName("subpractice_id") val subpracticeID: Int,
    val type: Int
)