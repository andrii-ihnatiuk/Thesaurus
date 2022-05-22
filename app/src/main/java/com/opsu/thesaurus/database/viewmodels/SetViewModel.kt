package com.opsu.thesaurus.database.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.opsu.thesaurus.database.AppDatabase
import com.opsu.thesaurus.database.daos.SetDao
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.database.entities.Relations
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

    fun addTerm(term: Entities.Term): LiveData<Long>
    {
        val result = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO)
        {
            result.postValue(dao.addTerm(term))
        }
        return result
    }

    fun addTerms(terms: List<Entities.Term>): LiveData<List<Long>>
    {
        val result = MutableLiveData<List<Long>>()
        viewModelScope.launch(Dispatchers.IO)
        {
            result.postValue(dao.addTerms(terms))
        }
        return result
    }

    fun addSetTermCrossRef(row: Relations.SetTermCrossRef)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            dao.addSetTermCrossRef(row)
        }
    }

    fun getTermsBySet(setTitle: String) : LiveData<List<Relations.SetWithTerms>>
    {
        val result = MutableLiveData<List<Relations.SetWithTerms>>()
        viewModelScope.launch(Dispatchers.IO)
        {
            result.postValue(dao.getTermsBySet(setTitle))
        }
        return result
    }

}