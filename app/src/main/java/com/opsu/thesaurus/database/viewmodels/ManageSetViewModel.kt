package com.opsu.thesaurus.database.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.opsu.thesaurus.database.AppDatabase
import com.opsu.thesaurus.database.daos.SetDao
import com.opsu.thesaurus.database.daos.TermDao
import com.opsu.thesaurus.database.entities.Entities
import kotlinx.coroutines.*

class ManageSetViewModel(application: Application, private val setId: Long? = null): AndroidViewModel(application)
{
    private val termDao: TermDao = AppDatabase.getDatabase(application).termDao()
    private val setDao: SetDao = AppDatabase.getDatabase(application).setDao()


    // SELECT
    suspend fun checkTitleIsTaken(title: String): Int =
        withContext(Dispatchers.IO) {
            setDao.checkTitleIsTaken(title)
        }

    fun getSetById(): LiveData<Entities.Set>?
    {
        if (setId != null) {
            return setDao.getSetById(setId)
        }
        return null
    }

    fun getTermsBySet(): LiveData<List<Entities.Term>>?
    {
        if (setId != null) {
            return termDao.getTermsBySet(setId)
        }
        return null
    }


    // INSERT
    fun addSetWithTerms(set: Entities.Set, terms: List<Entities.Term>): Job
    {
        return viewModelScope.launch(Dispatchers.IO)
        {
            setDao.addSetWithTerms(set, terms)
        }
    }
    fun addTermsToSet(terms: List<Entities.Term>, set: Entities.Set): Job
    {
        terms.forEach {
            it.setIdRef = set.setId
        }
        return viewModelScope.launch(Dispatchers.IO)
        {
            setDao.updateSetTermsNum(set.setId, set.numOfTerms)
            termDao.addTerms(terms)
        }
    }


    // DELETE
    fun deleteTermsFromSet(terms: List<Entities.Term>, set: Entities.Set): Job
    {
        return viewModelScope.launch(Dispatchers.IO)
        {
            setDao.updateSetTermsNum(set.setId, set.numOfTerms)
            termDao.deleteTerms(terms)
        }
    }


    // UPDATE
    fun updateTerms(terms: List<Entities.Term>): Job
    {
        return viewModelScope.launch(Dispatchers.IO)
        {
            termDao.updateTerms(terms)
        }
    }
    fun updateSetTitle(setId: Long, title: String)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            setDao.updateSetTitle(setId, title)
        }
    }
}

class ManageSetViewModelFactory(
    private val application: Application,
    private val setId: Long?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManageSetViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return ManageSetViewModel(application, setId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}