package com.glivion.plasticdiary.services

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.*
import com.glivion.plasticdiary.util.QUIZ_ID
import com.glivion.plasticdiary.util.QUIZ_SCORE
import com.glivion.plasticdiary.util.TERMINATE_QUIZ_TAG_OUTPUT
import com.glivion.plasticdiary.util.TERMINATE_QUIZ_WORK_NAME
import com.glivion.plasticdiary.workers.TerminateQuizWorker
import timber.log.Timber

class TerminateQuizService: Service() {
    private lateinit var workManager: WorkManager
    var categoryID = 0
    var score = 0
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        workManager = WorkManager.getInstance(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.e("app started")
        intent?.extras?.let {
            categoryID = it.getInt(QUIZ_ID)
            score = it.getInt(QUIZ_SCORE)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Timber.e("app destroyed $categoryID, score $score")
        //startWorkerForTerminateQuiz(categoryID, score)
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Timber.e("app cleared $categoryID, score $score")
        startWorkerForTerminateQuiz(categoryID, score)
       stopSelf()
    }

    private fun startWorkerForTerminateQuiz(categoryID: Int, score: Int) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val data = Data.Builder()
            .putInt(QUIZ_ID, categoryID)
            .putInt(QUIZ_SCORE, score)
            .build()
        val terminateQuizRequest = OneTimeWorkRequestBuilder<TerminateQuizWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .addTag(TERMINATE_QUIZ_TAG_OUTPUT)
            .build()

        val continuation = workManager.beginUniqueWork(
            TERMINATE_QUIZ_WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            terminateQuizRequest
        )
        Timber.e("I'm here")
        continuation.enqueue()
    }


   /* override fun onHandleIntent(intent: Intent?) {
        intent?.extras?.let {
            categoryID = it.getInt(QUIZ_ID)
            score = it.getInt(QUIZ_SCORE)
        }
    }*/
}