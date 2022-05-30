package com.opsu.thesaurus.database.entities

import androidx.room.Embedded
import androidx.room.Relation


class Relations
{
    data class SetWithTerms(
        @Embedded
        val set: Entities.Set,
        @Relation(
            parentColumn = "setId",
            entityColumn = "setIdRef"
        )
        val terms: List<Entities.Term>
    )
}

