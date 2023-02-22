package com.example.studyw.auth

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.studyw.MainActivity
import com.example.studyw.R
import com.example.studyw.databinding.ActivityJoinBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        binding.joinBtn.setOnClickListener {
            var isGoToJoin = true

            val email = binding.emailText1.text.toString()
            val password1 = binding.pwdText1.text.toString()
            val password2 = binding.pwdcheckText.text.toString()

            //값이 비어있는지 확인
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일이 입력되지 않았습니다.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if (password1.isEmpty()) {
                Toast.makeText(this, "비밀번호가 입력되지 않았습니다.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if (password2.isEmpty()) {
                Toast.makeText(this, "비밀번호가 입력되지 않았습니다.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            // 비밀번호 일치 확인
            if (!password1.equals(password2)) {
                Toast.makeText(this, "비밀번호 값이 다릅니다.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            // 비밀번호 6자리 이상
            if (password1.length < 6) {
                Toast.makeText(this, "비밀번호를 6자리 이상으로 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            //모든 조건이 맞으면 실행
            if (isGoToJoin) {
                auth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "회원가입에 성공하였습니다!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_LONG).show()
                        }
                    }

            }
        }

    }
}