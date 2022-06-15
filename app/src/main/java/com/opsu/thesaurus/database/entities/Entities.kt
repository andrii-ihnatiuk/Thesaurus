package com.opsu.thesaurus.database.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

class Entities
{
    @Entity(tableName = "Set")
    data class Set(
        var setTitle: String,
        var numOfTerms: Int,
        var createdBy: String,
        @PrimaryKey(autoGenerate = true)
        val setId: Long = 0,
    ) : Serializable


    @Entity(
        tableName = "Term",
        foreignKeys = [
            ForeignKey(
                entity = Set::class,
                parentColumns = arrayOf("setId"),
                childColumns = arrayOf("setIdRef"),
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
            )
        ]
    )
    @Parcelize
    data class Term(
        var term: String,
        var definition: String,
        var setIdRef: Long? = null,
        @PrimaryKey(autoGenerate = true)
        val termId: Long? = null
    ) : Parcelable

}