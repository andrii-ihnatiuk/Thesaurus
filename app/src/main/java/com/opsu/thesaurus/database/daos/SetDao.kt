package com.opsu.thesaurus.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.database.entities.Relations

@Dao
interface SetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSet(set: Entities.Set): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTerm(term: Entities.Term): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTerms(terms: List<Entities.Term>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSetTermCrossRef(join: Relations.SetTermCrossRef): Long

    @Transaction
    @Query("SELECT * FROM `Set` WHERE setTitle = :setTitle")
    suspend fun getTermsBySet(setTitle: String): List<Relations.SetWithTerms>

//    @Query("SELECT Term.termId, Term.term, Term.definition FROM 'Term' INNER JOIN 'SetTermCrossRef' ON ('Term.termId' = 'SetTermCrossRef.termId' AND 'SetTermCrossRef.setTitle' = :setTitle)")
//    suspend fun getTermsOnSet(setTitle: String): List<Entities.Term>
//
//    @Query("SELECT * FROM Term")
//    suspend fun getAllTerms(): List<Entities.Term>
//
//    @Query("SELECT * FROM SetTermCrossRef")
//    suspend fun getAllSetTermCross(): List<Relations.SetTermCrossRef>

    @Query("SELECT * FROM `Set`")
    fun getAll(): LiveData<List<Entities.Set>>

}