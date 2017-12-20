package voen.experiment.firebasesimplechat.section.singup

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast
import voen.experiment.firebasesimplechat.R
import voen.experiment.firebasesimplechat.helper.SharedPreferenceHelper
import voen.experiment.firebasesimplechat.helper.getFirebaseAuth
import voen.experiment.firebasesimplechat.section.login.LoginActivity
import voen.experiment.firebasesimplechat.section.main.MainActivity
import voen.experiment.firebasesimplechat.utils.*

/**
 * Created by voen on 12/7/17.
 */
class SignUpActivity : AppCompatActivity() {
    private val firebaseAuth by lazy {
        this.getFirebaseAuth()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initializeListener()
    }

    private fun initializeListener() {
        bt_signup.setOnClickListener {
            pb_loading.showVisibility()
            bt_signup.disable()
            firebaseAuth
                    .createUserWithEmailAndPassword(et_email.text.toString(), et_password.text.toString())
                    .addOnCompleteListener {
                        pb_loading.hideVisibility()
                        if (it.isSuccessful) {
                            saveUid()
                            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                            finish()
                        } else {
                            it.exception?.message?.let { toast(it) }
                            it.exception?.printStackTrace()
                            bt_signup.enable()
                        }
                    }
        }
        tv_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun saveUid() {
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        firebaseUser?.let {
            SharedPreferenceHelper.setString(SharedPreferenceHelper.FIREBASE_UID, it.uid)
            SharedPreferenceHelper.setString(SharedPreferenceHelper.FIREBASE_USER_NAME, it.email)
        }
    }
}