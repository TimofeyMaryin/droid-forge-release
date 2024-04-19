package custom.lib.droid.droid_craft

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import custom.lib.droid.flyer_extension.FlyerExtension
import custom.web.view.compose.UserWebView
import java.time.LocalDate
import java.time.format.DateTimeFormatter


private val primary = Color(0xffFFDF00)
private val secondary = Color(0xff0096FF)
private val container = Color(0xff161616)
private val background = Color(0xff222428)

private val onContainer = Color(0xffe9e9e9)
private val onBackground = Color(0xffdddbd7)

private val white = Color(0xffFFFFFF)
private val black = Color(0xff000000)

class DroidForgeMain(
    private val activity: ComponentActivity,
    private val context: Context,
) {


    var appsCode = ""
    var currentData: String = ""
    var url: String = ""
    var applicationShortInformation: ShortAppInfo? = null

    private val sPref = activity.getSharedPreferences("user", Context.MODE_PRIVATE)
    private var statusApp by mutableStateOf(StatusApp.LOAD)


    @RequiresApi(Build.VERSION_CODES.O)
    private val activityResultLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                setFlyer()
            }
        }

    private fun setFlyer() {
        Log.e("TAG", "setFlyer: call", )
        FlyerExtension.setExtension(
            value = appsCode,
            context = context,
            onError = { statusApp = StatusApp.FAIL },
            onSuccess = { statusApp = StatusApp.SUCCESS }
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun start(){
        Log.e("TAG", "checkPush: call ${currentData.length}", )

        if (currentData.isEmpty()) {
            if (sPref.getString("key", "")?.isNotEmpty() == true) {
                Log.e("TAG", "checkPush: link is save", )
                statusApp = StatusApp.SUCCESS_DOWNLOAD
            }

            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    setFlyer()
                }
                activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {

                }
                else -> {
                    activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)

                }
            }
        } else {
            if (checkDateCondition(currentData)) {
                Log.e("TAG", "checkPush: открывать заглушку", )
                statusApp = StatusApp.FAIL
            } else {
                Log.e("TAG", "checkPush: открывать вьюху", )
                setFlyer()
            }
        }



    }
    @Composable
    fun ShowContent(
        userContent: @Composable () -> Unit
    ) {
        when (statusApp) {
            StatusApp.LOAD -> Loader(applicationShortInformation)
            StatusApp.SUCCESS -> UserWebView(data = url)
            StatusApp.FAIL -> userContent()
            StatusApp.SUCCESS_DOWNLOAD -> UserWebView(data = sPref.getString("key", "")!!)
        }
    }

    companion object {
        var signalCode = ""
    }

}

enum class StatusApp {
    LOAD, SUCCESS, FAIL, SUCCESS_DOWNLOAD
}

data class ShortAppInfo(
    val name: String,
    @DrawableRes val ic: Int,
)

@Composable
private fun Loader(info: ShortAppInfo?) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    window.statusBarColor = background.toArgb()

    if (info == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(background),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    text = "Подождите несколько секунд. Идет загрузка материала.",
                    color = onBackground,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )

            }
        }
    } else {
        val infiniteAnimation = rememberInfiniteTransition(label = "")
        val loadShape = infiniteAnimation.animateFloat(
            initialValue = 0.0f,
            targetValue = 0.2f,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(1000)
            ), label = ""
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Container(weight = 1f) {}
            Container(weight = 5f) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(70.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = info.ic),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier
                            .background(Color.White.copy(alpha = loadShape.value))
                            .fillMaxSize(),)
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = info.name,
                        color = onBackground,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Container(weight = 1f) {
                LinearProgressIndicator()
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun checkDateCondition(dateString: String): Boolean {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDate = LocalDate.now()
    val providedDate = LocalDate.parse(dateString, dateFormatter)

    return currentDate.minusDays(4) <= providedDate
}


@Composable
fun ColumnScope.Container(
    weight: Float,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().weight(weight), contentAlignment = Alignment.Center) {
        content()
    }
}

@Composable
fun RowScope.Container(
    weight: Float,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().weight(weight), contentAlignment = Alignment.Center) {
        content()
    }
}