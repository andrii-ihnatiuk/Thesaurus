package com.opsu.thesaurus.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.opsu.thesaurus.database.entities.Entities

@Dao
interface TermDao
{
    // INSERT
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTerm(term: Entities.Term): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTerms(terms: List<Entities.Term>): List<Long>


    // GET
    @Query("SELECT * FROM Term")
    suspend fun getAllTerms(): List<Entities.Term>

    @Transaction
    @Query("SELECT * FROM Term WHERE setIdRef = :setId ORDER BY termId ASC")
    fun getTermsBySet(setId: Long): LiveData<List<Entities.Term>>


    // DELETE
    @Delete
    suspend fun deleteTerms(ids: List<Entities.Term>)


    // UPDATE
    @Update
    suspend fun updateTerms(terms: List<Entities.Term>)



}