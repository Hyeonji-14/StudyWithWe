package com.example.studyw.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.studyw.R
import com.example.studyw.databinding.ActivityCorrectionBinding
import com.example.studyw.utils.FBAuth
import com.example.studyw.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CorrectionActivity : AppCompatActivity() {

    private lateinit var key: String

    private val TAG = CorrectionActivity::class.java.simpleName

    private lateinit var binding: ActivityCorrectionBinding

    private lateinit var writerUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_correction)

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.correctionWrite.setOnClickListener {
            correctionData(key)
        }
    }

    private fun correctionData(key: String) {

        FBRef.boardRef
            .child(key)
            .setValue(
                BoardModel(
                    binding.titleEdit.text.toString(),
                    binding.contentsEdit.text.toString(),
                    writerUid,
                    FBAuth.getTime()
                )
            )
        Toast.makeText(this, "게시물 수정이 완료되었습니다.", Toast.LENGTH_LONG).show()

        finish()
    }

    private fun getImageData(key: String) {

        val storageReference = Firebase.storage.reference.child(key)
        val imageV = binding.imageWrite2

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageV)
            }
        })
    }

    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.titleEdit.setText(dataModel?.title)
                binding.contentsEdit.setText(dataModel?.content)
                writerUid = dataModel!!.uid

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
}