package com.example.studyw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.studyw.auth.IntroActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.xml.datatype.DatatypeConstants.DURATION

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        if(auth.currentUser?.uid == null) {
            Log.d("Splash Activity", "null")

            Handler().postDelayed({
                val intent = Intent(baseContext, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, DURATION)

        } else {
            Log.d("Splash Activity", "not null")

            Handler().postDelayed({
                val intent = Intent(baseContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, DURATION)
        }

//        Handler().postDelayed({
//            val intent = Intent(baseContext, IntroActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//            startActivity(intent)
//            finish()
//        }, DURATION)
    }

    companion object {
        private const val DURATION: Long = 3000
    }
}