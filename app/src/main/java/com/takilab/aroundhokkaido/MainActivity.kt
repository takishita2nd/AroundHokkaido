package com.takilab.aroundhokkaido

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 1234
    }

    private val appid = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var updatedCount = 0
        var prevLocation : Location? = null

        val client : OkHttpClient = OkHttpClient()
        val url : String = "https://map.yahooapis.jp/dist/V1/distance?output=json&coordinates=%f,%f %f,%f&appid=%s"
        // 位置情報取得時のコールバック定義オブジェクト
        locationCallback = object : LocationCallback() {
            /**
             * 位置情報取得時の処理
             */
            @SuppressLint("SetTextI18n")
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    updatedCount++
                    if (prevLocation == null) {
                        locationText.text = "[${updatedCount}] ${location.latitude} , ${location.longitude}"
                    } else {
                        locationText.text = "[${updatedCount}] ${location.latitude} , ${location.longitude}" +
                                " - ${prevLocation!!.latitude} , ${prevLocation!!.longitude}"
                        // HTTPリクエストを作成
                        val request = Request.Builder()
                            .url(url.format(location.longitude, location.latitude, prevLocation!!.longitude, prevLocation!!.latitude, appid))
                            .build()
                        // HTTPリクエストを送信
                        client.newCall(request).enqueue(object : Callback {
                            /**
                             * HTTPリクエスト失敗時の処理
                             */
                            override fun onFailure(call: Call, e: IOException) {
                                println("fail : $e")
                            }

                            /**
                             * HTTP李lクエスト成功時の処理
                             */
                            override fun onResponse(call: Call, response: Response) {
                                var str = response!!.body!!.string()
                                val jsonObject = JSONObject(str)
                                val jsonArray = jsonObject.getJSONArray("Feature")
                                for (i in 0 until jsonArray.length()) {
                                    val jsonData = jsonArray.getJSONObject(i)
                                    val geometry = jsonData.getJSONObject("Geometry")
                                    val distance = geometry.getDouble("Distance")
                                    val mainHandler : Handler = Handler(Looper.getMainLooper())
                                    mainHandler.post(Runnable {
                                        distanceText.text = "$distance"
                                    })
                                }
                            }
                        })
                    }
                    prevLocation = location
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    /**
     * 権限の問い合わせを行う。
     */
    private fun requestPermission() {
        // 位置情報にアクセスする権限があるかどうかを確認する。
        if (!checkPermission()) {
            // 権限が無いため、許可を求める
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                PERMISSION_REQUEST_CODE)
        }
    }

    /**
     * 権限の確認を行う
     */
    private fun checkPermission() : Boolean {
        return ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 位置情報の取得開始
     */
    private fun startLocationUpdates() {
        // 権限の再確認（無いとIDE（Android Studio）に怒られる）
        if (!checkPermission()) {
            requestPermission()
            return
        }
        // 位置情報の取得開始
        val locationRequest = createLocationRequest() ?: return
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null)
    }

    /**
     * 位置情報の取得を停止する
     */
    private fun stopLocationUpdates() {
        // 位置情報の取得停止
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * 位置情報を行うインスタンスの作成
     */
    private fun createLocationRequest(): LocationRequest? {
        return LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}