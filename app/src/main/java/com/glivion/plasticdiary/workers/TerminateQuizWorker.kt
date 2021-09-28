package com.glivion.plasticdiary.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import androidx.work.rxjava3.RxWorker
import com.glivion.plasticdiary.data.repository.QuizRepository
import com.glivion.plasticdiary.util.QUIZ_ID
import com.glivion.plasticdiary.util.QUIZ_SCORE
import com.glivion.plasticdiary.util.makeStatusNotification
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.core.Single
import org.json.JSONObject

@HiltWorker
class TerminateQuizWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: QuizRepository,
) : RxWorker(context, params){
    override fun createWork(): Single<Result> {
        val score = inputData.getInt(QUIZ_SCORE, 0)
        val categoryID = inputData.getInt(QUIZ_ID, 0)

        makeStatusNotification(
            "Recording your score",
            "Your score will be recorded in the background",
            applicationContext
        )

        return repository.submitScore(score, categoryID)
            .flatMap {response ->
                if (response.isSuccessful) {
                    val message = response.body()?.message.toString()
                    makeStatusNotification(
                        "Score recorded",
                        message,
                        applicationContext
                    )
                    Single.just(Result.success())
                } else {
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody!!)
                    val message = jsonObject.getString("error")
                    makeStatusNotification(
                        "Something went wrong 😕",
                        message,
                        applicationContext
                    )
                    Single.just(Result.failure())
                }
            }
    }
}