package assignment.home.taglocation.view.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import assignment.home.taglocation.databinding.ActivitySplashScreenBinding
import assignment.home.taglocation.view.MapsActivity

class SplashScreenActivity : AppCompatActivity() {


    private lateinit var activtySplashScreenBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        activtySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(activtySplashScreenBinding.root)
    }

    override fun onResume() {
        super.onResume()
        displaySplashScreen()
    }

    /**
     * Function to display the Splash Screen
     */
    private fun displaySplashScreen() {
        Handler().postDelayed({
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
            this.finish()
        }, 2000)
    }
}