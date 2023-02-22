package com.example.studyw.homeBoard

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.databinding.DataBindingUtil
import com.example.studyw.R
import com.example.studyw.board.BoardModel
import com.example.studyw.board.BoardWriteActivity
import com.example.studyw.databinding.ActivityBoardWriteBinding
import com.example.studyw.databinding.ActivityHomeWriteBinding
import com.example.studyw.utils.FBAuth
import com.example.studyw.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class HomeWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeWriteBinding

    private val TAG = HomeWriteActivity::class.java.simpleName

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_write)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_write)

        binding.btnWrite.setOnClickListener {

            val title = binding.titleEdit.text.toString()
            val content = binding.contentsEdit.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            // 파이어베이스 store에 이미지 저장
            // 만약 게시글을 클릭했을 때, 게시글에 대한 정보를 받아와야 함
            // 이미지 이름에 대한 정보를 모르기 때문에 key(문서의 key) 값으로 처리
            val key = FBRef.homeBoardRef.push().key.toString()

            FBRef.homeBoardRef
                .child(key) //push 대신 child
                .setValue(HomeModel(title, content, uid, time))

            if (isImageUpload == true) {
                imageUpload(key)
            }
            finish()
        }

        binding.imageWrite.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }
    private fun imageUpload(key: String) {

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key)
        val imageView = binding.imageWrite

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            binding.imageWrite.setImageURI(data?.data)
        }
    }
}