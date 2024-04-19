package custom.lib.droid.droidforgerelease

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import custom.lib.droid.droid_craft.DroidForgeMain
import custom.lib.droid.droidforgerelease.ui.theme.DroidForgeReleaseTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = DroidForgeMain(context = applicationContext, activity = this)
        content.apply {
            this.applicationShortInformation = null
            this.appsCode = "u7pZpr4bJ7kxaAujH5ok3Z"
            this.url = "https://github.com"
            this.start()
        }

        setContent {
            DroidForgeReleaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    content.ShowContent {
                        Text("White Part")
                    }
                }
            }
        }
    }
}
