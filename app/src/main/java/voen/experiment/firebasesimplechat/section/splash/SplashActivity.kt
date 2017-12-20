package voen.experiment.firebasesimplechat.section.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import voen.experiment.firebasesimplechat.R
import voen.experiment.firebasesimplechat.helper.SharedPreferenceHelper
import voen.experiment.firebasesimplechat.section.login.LoginActivity
import voen.experiment.firebasesimplechat.section.main.MainActivity

/**
 * Created by voen on 12/6/17.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val background = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(3*1000)

                    startActivity(Intent(baseContext, if (SharedPreferenceHelper.getString(SharedPreferenceHelper.FIREBASE_UID).isEmpty()) LoginActivity::class.java else MainActivity::class.java))
                    finish()
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        background.start()
    }
}