package com.opsu.thesaurus.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.opsu.thesaurus.database.entities.Entities

@Dao
interface SetDao
{
    // INSERT
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSet(set: Entities.Set): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTerms(terms: List<Entities.Term>): List<Long>

    @Transaction
    suspend fun addSetWithTerms(set: Entities.Set, terms: List<Entities.Term>)
    {
        val id = addSet(set)
        for (term in terms)
            term.setIdRef = id
        addTerms(terms)
    }


    // GET
    @Query("SELECT * FROM 'Set' ORDER BY setId DESC")
    fun getAllSets(): LiveData<List<Entities.Set>>

    @Query("SELECT * FROM 'Set' WHERE setId = :setId")
    fun getSetById(setId: Long): LiveData<Entities.Set>

    @Query("SELECT 1 FROM 'Set' WHERE setTitle = :title")
    fun checkTitleIsTaken(title: String): Int


    // UPDATE
    @Query("UPDATE 'Set' SET numOfTerms = :newVal WHERE setId = :setId")
    fun updateSetTermsNum(setId: Long, newVal: Int)

    @Query("UPDATE 'Set' SET setTitle = :title WHERE setId = :setId")
    fun updateSetTitle(setId: Long, title: String)


    // DELETE
    @Query("DELETE FROM 'Set' WHERE setId = :setId")
    suspend fun deleteSet(setId: Long)

}