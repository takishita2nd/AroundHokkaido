package com.takilab.aroundhokkaido

import java.io.File

class AroundHokkaido {
    private val filename: String = "distance.txt"
    private val citylist: CityList = CityList()
    private var resultDistance: Double = 0.0
    private val activity: MainActivity = SingletonActivity.GetActivity()
    init{
        getResultDistance()
    }

    fun getResultDistance(): Double{
        val file = File(activity.filesDir, filename)
        if(file.exists()){
            resultDistance = file.readText().toDouble()
        }
        return resultDistance
    }

    fun getTotalDistance(): Double {
        return citylist.totalDistance
    }

    fun updateDistance(distance: Double): Double {
        val file = File(activity.filesDir, filename)
        if(file.exists()){
            resultDistance = file.readText().toDouble()
        }
        resultDistance += distance
        file.writeText("%.3f".format(resultDistance))
        return resultDistance
    }

    fun getPosition() : StartEndPosition {
        var tempDistance = 0.0
        var start : String = ""
        var end : String = ""
        var loop : Boolean = false
        var segment: Double = 0.0
        var aaa: Double = 0.0
        run {
            citylist.cityList.forEach{
                if(loop){
                    end = it.city
                    return@run
                }else{
                    tempDistance += it.distance
                    if(resultDistance < tempDistance){
                        start = it.city
                        aaa = resultDistance - (tempDistance - it.distance)
                        segment = it.distance
                        loop = true
                    }
                }
            }
        }
        return StartEndPosition(start, end, aaa, segment)
    }
}