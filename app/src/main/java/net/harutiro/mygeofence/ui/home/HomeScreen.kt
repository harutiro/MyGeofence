package net.harutiro.mygeofence.ui.home

import android.location.Location
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Home(
    innerPadding: PaddingValues,
    viewModel: HomeViewModel = viewModel(),
) {

    // 位置情報
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var result by remember { mutableStateOf(false) }

    // コンテキスト
    val context = LocalContext.current
    SideEffect {
        // コンテキストの受け渡し
        viewModel.initContent(context)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(innerPadding)
    ) {
        Button(
            onClick = {
                // 位置情報を取得
                viewModel.startLocationUpdates(object : HomeViewModel.MyLocationCallback {
                    override fun onLocationResult(location: Location?, checkResult: Boolean) {
                        if (location != null) {
                            latitude = location.latitude
                            longitude = location.longitude
                            result = checkResult

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
                viewModel.stopLocationUpdates()
            }
        ) {
            Text(text = "位置情報を取得を止める")
        }

        Text(text = "latitude: $latitude")
        Text(text = "longitude: $longitude")
        Text(text = "checkResult: ${if(result) "円の中に入っています" else "円の外です"}")

    }
}