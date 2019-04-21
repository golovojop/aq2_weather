package k.s.yarlykov

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logI("onCreate")
    }

    override fun onPause() {
        super.onPause()
        logI("onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        logI("onDestroy")
    }

    override fun onStart() {
        super.onStart()
        logI("onStart")
    }

    override fun onResume() {
        super.onResume()
        logI("onResume")
    }

    override fun onStop() {
        super.onStop()
        logI("onStop")
    }

    fun logI(message: String) {
        val tag = "kWeather"
        Log.i(tag, MainActivity::class.java.simpleName + ": " + message)
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}
