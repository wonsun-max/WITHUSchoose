package com.withus.choose.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.withus.choose.data.model.VerdictEntity

@Database(entities = [VerdictEntity::class], version = 1, exportSchema = false)
abstract class SomaticDatabase : RoomDatabase() {

    abstract fun verdictDao(): VerdictDao

    companion object {
        @Volatile
        private var INSTANCE: SomaticDatabase? = null

        fun getDatabase(context: Context): SomaticDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SomaticDatabase::class.java,
                    "somatic_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
