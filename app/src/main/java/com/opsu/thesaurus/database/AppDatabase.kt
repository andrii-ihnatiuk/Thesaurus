package com.opsu.thesaurus.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.opsu.thesaurus.database.daos.SetDao
import com.opsu.thesaurus.database.daos.TermDao
import com.opsu.thesaurus.database.entities.Entities
import com.opsu.thesaurus.database.entities.Relations

@Database(entities = [Entities.Set::class, Entities.Term::class], version = 1)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun setDao(): SetDao
    abstract fun termDao(): TermDao

    companion object
    {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase
        {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}