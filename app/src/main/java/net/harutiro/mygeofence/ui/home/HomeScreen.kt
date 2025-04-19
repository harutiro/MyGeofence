package net.harutiro.mygeofence.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.BatteryManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import net.harutiro.mygeofence.utils.DateUtils
import net.harutiro.mygeofence.utils.EnergyCallback
import net.harutiro.mygeofence.utils.EnergyChecker
import net.harutiro.mygeofence.utils.GeoNotification
import net.harutiro.mygeofence.utils.OtherFileStorage


@Composable
fun Home(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel = viewModel(),
) {
    // 位置情報
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var result by remember { mutableStateOf(false) }
    var energyValue by remember { mutableLongStateOf(0L) }
    var isSensing by remember { mutableStateOf(false) }
    var isEntry by remember { mutableStateOf(false) }

    // コンテキスト
    val context = LocalContext.current
    SideEffect {
        // コンテキストの受け渡し
        viewModel.initContent(context)
    }

    // csvファイル書き込み
    var otherFileStorage:OtherFileStorage? = null

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // 許可された
            val geoNotification = GeoNotification()
            geoNotification.showNotification(context,"許可されたよ")

        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(innerPadding)
    ) {
        Button(
            onClick = {
                // 位置情報を取得
                isSensing = true
                viewModel.startLocationUpdates(object : HomeViewModel.MyLocationCallback {
                    override fun onLocationResult(location: Location?, checkResult: Boolean) {
                        if (location != null) {
                            latitude = location.latitude
                            longitude = location.longitude
                            result = checkResult

                            if(checkResult && !isEntry){
                                isEntry = true
                                val geoNotification = GeoNotification()
                                geoNotification.showNotification(context,"中に入ったよ")
                            }else{
                                isEntry = false
                            }
                        }
                    }

                    override fun onLocationError(error: String) {
                        // エラー処理
                    }
                })
            }
        ) {
            Text(text = "位置情報を取得開始する")
        }

        Button(
            onClick = {
                isSensing = false
                isEntry = false
                viewModel.stopLocationUpdates()
            }
        ) {
            Text(text = "位置情報を取得を止める")
        }

        Text(text = "latitude: $latitude")
        Text(text = "longitude: $longitude")
        Text(text = "checkResult: ${if(result) "円の中に入っています" else "円の外です"}")

        Button(
            onClick = {
                otherFileStorage = OtherFileStorage(context)
                val energyChecker = EnergyChecker(context)
                // エネルギーチェックを開始
                energyChecker.start(object : EnergyCallback {
                    override fun onEnergyChecked(energy: Long) {
                        // ここでエネルギー値を処理する
                        energyValue = energy
                        otherFileStorage?.doLog("$energy,$latitude,$longitude,$result,$isSensing")
                    }
                })
            }
        ) {
            Text(text = "電量を確認する")
        }

        Button(
            onClick = {
                otherFileStorage = null
                val energyChecker = EnergyChecker(context)
                // エネルギーチェックを停止
                energyChecker.stop()
            }
        ) {
            Text(text = "電量チェックを止める")
        }

        Text(text = "電量: $energyValue μAh")

        Button(onClick = {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }){
            Text("通知発行のパ＝ミッション許可をもらう")
        }

    }
}
