package com.opsu.thesaurus.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation


class Relations
{
    // M to N relation table
    @Entity(primaryKeys = ["setId", "termId"])
    data class SetTermCrossRef(
        val setTitle: String,
        val termId: Int
    )
    // Entity for getting set with all its terms
    data class SetWithTerms(
        @Embedded
        val set: Entities.Set,
        @Relation(
            parentColumn = "setTitle",
            entityColumn = "termId",
            associateBy = Junction(SetTermCrossRef::class)
        )
        val terms: List<Entities.Term>
    )

}

