package custom.lib.droid.droid_craft

import android.app.Application
import custom.lib.droid.signal_extenstion.SignalExtension

class HelperApps: Application() {
    override fun onCreate() {
        super.onCreate()

        SignalExtension.setSignal(applicationContext, DroidForgeMain.signalCode)
    }
}