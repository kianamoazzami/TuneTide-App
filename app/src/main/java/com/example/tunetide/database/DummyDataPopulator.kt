package com.example.tunetide.database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
object DummyDataPopulator {





        fun populate(database: TuneTideDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                val timerDao = database.timerDao()

                val dummyTimers = listOf(
                    Timer(0,
                        "Timer 1",
                        false,
                        5,
                        600,
                        1,
                        2, 300,
                        3, 4,
                        true),
                    Timer(0, "Timer 2", true, 3, 450, 5, 6, 150, 7, 8, false),
                    Timer(0, "Timer 3", false, 8, 800, 9, 10, 400, 11, 12, true),
                    Timer(0, "Timer 4", true, 4, 500, 13, 14, 250, 15, 16, false),
                    Timer(0, "Timer 5", false, 6, 700, 17, 18, 350, 19, 20, true)
                )
                for (timer in dummyTimers) {
                    timerDao.insertTimer(timer)
                }
            }
        }
    }

