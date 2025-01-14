package com.example.movies.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.movies.core.TAG
import java.util.concurrent.TimeUnit.SECONDS

class MyWorker(context: Context, private val workerParams: WorkerParameters): Worker(context, workerParams) {
    override fun doWork(): Result {
        Log.d(TAG, "Worker working")

        var s = 0
        for (i in 1..workerParams.inputData.getInt("to", 1)) {
            if (isStopped) {
                break
            }
            SECONDS.sleep(1)
            Log.d("MyWorker", "progress: $i")
            setProgressAsync(workDataOf("progress" to i))
            s += i
        }

        return Result.success(workDataOf("result" to s))
    }
}