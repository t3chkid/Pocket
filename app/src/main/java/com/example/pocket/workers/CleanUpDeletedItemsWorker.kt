package com.example.pocket.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.pocket.data.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException

/**
 * This worker is responsible for deleting all the items marked
 * as 'deleted' in the database.
 */
@HiltWorker
class CleanUpDeletedItemsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: Repository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = runCatching {
        val itemsMarkedAsDeleted = repository.getUrlItemsMarkedAsDeleted()
        itemsMarkedAsDeleted.forEach { repository.permanentlyDeleteSavedUrlItem(it) }
        Result.success()
    }.getOrElse {
        if (it is CancellationException) throw it
        Result.failure()
    }
}