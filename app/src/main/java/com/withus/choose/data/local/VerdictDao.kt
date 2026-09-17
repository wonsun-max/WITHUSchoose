package com.withus.choose.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.withus.choose.data.model.VerdictEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VerdictDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerdict(verdict: VerdictEntity): Long

    @Update
    suspend fun updateVerdict(verdict: VerdictEntity)

    @Query("SELECT * FROM verdicts ORDER BY timestamp DESC")
    fun getAllVerdicts(): Flow<List<VerdictEntity>>

    @Query("SELECT * FROM verdicts WHERE id = :id")
    suspend fun getVerdictById(id: Long): VerdictEntity?

    @Delete
    suspend fun deleteVerdict(verdict: VerdictEntity)

    @Query("DELETE FROM verdicts")
    suspend fun clearAll()
}
