package com.withus.choose.data.repository

import com.withus.choose.data.local.VerdictDao
import com.withus.choose.data.model.VerdictEntity
import kotlinx.coroutines.flow.Flow

class VerdictRepository(private val dao: VerdictDao) {

    val allVerdicts: Flow<List<VerdictEntity>> = dao.getAllVerdicts()

    suspend fun saveVerdict(verdict: VerdictEntity): Long {
        return dao.insertVerdict(verdict)
    }

    suspend fun updateVerdict(verdict: VerdictEntity) {
        dao.updateVerdict(verdict)
    }

    suspend fun deleteVerdict(verdict: VerdictEntity) {
        dao.deleteVerdict(verdict)
    }

    suspend fun clearHistory() {
        dao.clearAll()
    }
}
