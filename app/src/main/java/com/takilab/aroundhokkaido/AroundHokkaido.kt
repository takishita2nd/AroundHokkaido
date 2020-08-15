package com.takilab.aroundhokkaido

import java.io.File

class AroundHokkaido {
    private val filename: String = "distance.txt"
    private val citylist: CityList = CityList()
    private var totalDistance: Double = 0.0
    private val activity: MainActivity = SingletonActivity.GetActivity()

    fun getDistance(): Double{
        val file = File(activity.filesDir, filename)
        if(file.exists()){
            totalDistance = file.readText().toDouble()
        }
        return totalDistance
    }

    fun updateDistance(distance: Double): Double {
        val file = File(activity.filesDir, filename)
        if(file.exists()){
            totalDistance = file.readText().toDouble()
        }
        totalDistance += distance
        file.writeText("%.3f".format(totalDistance))
        return totalDistance
    }
}