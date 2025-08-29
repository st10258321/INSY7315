package vcmsa.projects.wil_hustlehub


import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import java.util.concurrent.TimeUnit
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeoutException


fun <T> getOrAwaitValue(
    liveData: LiveData<T>,
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {


        var data : T? = null
        val latch = CountDownLatch(1)

        val observer = object: Observer<T>{
            override fun onChanged(t: T){
                data = t
                latch.countDown()
                liveData.removeObserver(this)
            }

        }
    InstrumentationRegistry.getInstrumentation().runOnMainSync {
        liveData.removeObserver(observer)
    }
        if(!latch.await(time,timeUnit)){
            throw TimeoutException("Live data was not loaded")

        }
        @Suppress("UNCHECKED_CAST")
        return data as T
    }
