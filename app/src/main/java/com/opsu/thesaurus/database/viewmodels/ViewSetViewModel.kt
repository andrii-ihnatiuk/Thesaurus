package com.opsu.thesaurus.database.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.opsu.thesaurus.database.AppDatabase
import com.opsu.thesaurus.database.daos.SetDao
import com.opsu.thesaurus.database.daos.TermDao
import com.opsu.thesaurus.database.entities.Entities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewSetViewModel(application: Application, setId: Long) : AndroidViewModel(application)
{
    var readSetData: LiveData<Entities.Set>
    var readAllTerms: LiveData<List<Entities.Term>>

    private val setDao: SetDao = AppDatabase.getDatabase(application).setDao()
    private val termDao: TermDao = AppDatabase.getDatabase(application).termDao()

    init {
        readSetData = setDao.getSetById(setId)
        readAllTerms = termDao.getTermsBySet(setId)
    }

    fun getAllTerms() : LiveData<List<Entities.Term>>
    {
        val result = MutableLiveData<List<Entities.Term>>()
        viewModelScope.launch(Dispatchers.IO)
        {
            result.postValue(termDao.getAllTerms())
        }
        return result
    }

}

class ViewSetViewModelFactory(
    private val application: Application,
    private val setId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewSetViewModel::class.java))
        {
            @Suppress("UNCHECKED_CAST")
            return ViewSetViewModel(application, setId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
