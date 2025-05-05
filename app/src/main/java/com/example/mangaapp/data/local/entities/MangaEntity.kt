package com.example.mangaapp.data.local.entities


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.mangaapp.data.local.converters.StringListConverter

@Entity(tableName = "manga")
data class MangaEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val subTitle: String,
    val status: String,
    val thumb: String,
    val summary: String,
    @TypeConverters(StringListConverter::class)
    val authors: List<String>,
    @TypeConverters(StringListConverter::class)
    val genres: List<String>,
    val nsfw: Boolean,
    val type: String,
    val totalChapter: Int,
    val createAt: Long,
    val updateAt: Long
)