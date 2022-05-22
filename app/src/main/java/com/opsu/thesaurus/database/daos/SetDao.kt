package com.opsu.thesaurus.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.opsu.thesaurus.database.entities.Entities

@Dao
interface SetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSet(set: Entities.Set)

    @Query("SELECT * FROM `Set`")
    fun getAll(): LiveData<List<Entities.Set>>

}