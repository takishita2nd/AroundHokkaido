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

    fun getCity() : StartEnd {
        var tempDistance = 0.0
        var start : String = ""
        var end : String = ""
        var loop : Boolean = false
        run {
            citylist.cityList.forEach{
                if(loop){
                    end = it.city
                    return@run
                }else{
                    tempDistance += it.distance
                    if(totalDistance < tempDistance){
                        start = it.city
                        loop = true
                    }
                }
            }
        }
        return StartEnd(start, end)
    }
}