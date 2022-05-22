package com.opsu.thesaurus.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

class Entities
{
    @Entity(tableName = "Set")
    data class Set(
        @PrimaryKey(autoGenerate = false)
        val setTitle: String,
        val numOfTerms: Int,
        val createdBy: String,
    ) : Serializable

    @Entity(tableName = "Term")
    data class Term(
        @PrimaryKey(autoGenerate = true)
        val termId: Int,
        var term: String,
        var definition: String
    ) : Serializable

}