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

class SetViewModel(application: Application) : AndroidViewModel(application)
{
    var readAllData: LiveData<List<Entities.Set>>
    private val dao: SetDao

    init {
        dao = AppDatabase.getDatabase(application).setDao()
        readAllData = dao.getAll()
    }

    // adding to db in a background thread
    fun addSet(set: Entities.Set)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            dao.addSet(set)
        }
    }

}