package voen.experiment.firebasesimplechat.section.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import voen.experiment.firebasesimplechat.R
import voen.experiment.firebasesimplechat.helper.SharedPreferenceHelper
import voen.experiment.firebasesimplechat.helper.getFirebaseAuth
import voen.experiment.firebasesimplechat.section.main.MainActivity
import voen.experiment.firebasesimplechat.section.singup.SignUpActivity
import voen.experiment.firebasesimplechat.utils.*

/**
 * Created by voen on 12/6/17.
 */
class LoginActivity : AppCompatActivity() {
    private val firebaseAuth by lazy {
        this.getFirebaseAuth()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initializeListener()
        this.checkGoogleAPI()
    }

    private fun initializeListener() {
        bt_login.setOnClickListener {
            if(et_email.text.isNotEmpty() && et_password.text.isNotEmpty()) {
                pb_loading.showVisibility()
                bt_login.disable()
                firebaseAuth
                        .signInWithEmailAndPassword(et_email.text.toString(), et_password.text.toString())
                        .addOnCompleteListener {
                            pb_loading.hideVisibility()
                            if (it.isSuccessful) {
                                firebaseAuth.currentUser?.let {
                                    SharedPreferenceHelper.setString(SharedPreferenceHelper.FIREBASE_USER_NAME, it.email)
                                    SharedPreferenceHelper.setString(SharedPreferenceHelper.FIREBASE_UID, it.uid)
                                }
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                it.exception?.message?.let { toast(it) }
                                it.exception?.printStackTrace()
                                bt_login.enable()
                            }
                        }
            } else {
                toast(getString(R.string.empty_username))
            }
        }
        tv_signup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        this.checkGoogleAPI()
    }
}