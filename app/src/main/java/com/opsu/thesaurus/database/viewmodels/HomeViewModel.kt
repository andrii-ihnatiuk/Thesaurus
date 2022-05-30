package com.opsu.thesaurus.database.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.opsu.thesaurus.database.AppDatabase
import com.opsu.thesaurus.database.daos.SetDao
import com.opsu.thesaurus.database.entities.Entities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application)
{
    var readAllSets: LiveData<List<Entities.Set>>
    private val dao: SetDao = AppDatabase.getDatabase(application).setDao()

    init {
        readAllSets = dao.getAllSets()
    }

    // INSERT
    fun addSet(set: Entities.Set)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            dao.addSet(set)
        }
    }


    // DELETE
    fun deleteSet(setId: Long)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            dao.deleteSet(setId)
        }
    }

}