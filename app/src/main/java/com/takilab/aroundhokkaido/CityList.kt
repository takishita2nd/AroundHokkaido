package com.takilab.aroundhokkaido

import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

class CityList {
    val cityList: ArrayList<City> = ArrayList()

    init{
        val activity: MainActivity = SingletonActivity.GetActivity() as MainActivity
        val assetManager = activity.resources.assets //アセット呼び出し
        val inputStream = assetManager.open("AroundHokkaido.json") //Jsonファイル
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val str: String = bufferedReader.readText() //データ
        try {
            val jsonObject = JSONObject(str)
            val jsonArray = jsonObject.getJSONArray("list")
            for (i in 0 until jsonArray.length()) {
                val jsonData = jsonArray.getJSONObject(i)
                val city: City = City(jsonData.getString("city"), jsonData.getDouble("distance"))
                cityList.add(city)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}