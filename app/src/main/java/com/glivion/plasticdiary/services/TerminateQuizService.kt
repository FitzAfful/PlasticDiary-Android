package com.glivion.plasticdiary.services

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import com.glivion.plasticdiary.util.QUIZ_ID
import com.glivion.plasticdiary.util.QUIZ_SCORE
import timber.log.Timber

class TerminateQuizService: IntentService("TERMINATE_QUIZ_SERVICE") {
    var categoryID = 0
    var score = 0
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.e("app started")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Timber.e("app destroyed $categoryID, score $score")
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Timber.e("app cleared $categoryID, score $score")
       stopSelf()
    }

    override fun onHandleIntent(intent: Intent?) {
        intent?.extras?.let {
            categoryID = it.getInt(QUIZ_ID)
            score = it.getInt(QUIZ_SCORE)
        }
    }
}