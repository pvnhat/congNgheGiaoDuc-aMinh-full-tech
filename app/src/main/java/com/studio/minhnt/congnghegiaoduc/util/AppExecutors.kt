package com.studio.minhnt.congnghegiaoduc.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AppExecutors(private val diskIO: Executor, private val networkIO: Executor, private val mainThread: Executor) {
    @Inject
    constructor() : this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), MainThreadExecutor())


    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    class MainThreadExecutor : Executor {
        val mainThreadHandler: Handler by lazy {
            Handler(Looper.getMainLooper())
        }

        override fun execute(command: Runnable?) {
            mainThreadHandler.post(command)
        }
    }
}
