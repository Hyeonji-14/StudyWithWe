package com.example.studyw.homeBoard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.studyw.R
import com.example.studyw.comment.CommentLVAdapter
import com.example.studyw.comment.CommentModel
import com.example.studyw.databinding.ActivityHomeReadBinding
import com.example.studyw.utils.FBAuth
import com.example.studyw.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class HomeReadActivity : AppCompatActivity() {

    private val TAG = HomeReadActivity::class.java.simpleName

    private lateinit var binding: ActivityHomeReadBinding

    private lateinit var key: String

    private val commentDataList = mutableListOf<CommentModel>()

    private lateinit var commentLVdapter: CommentLVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_read)

        binding.moreBtn.setOnClickListener {
            showDialog()
        }

        key = intent.getStringExtra("key").toString()
        getBoardData(key)
        getImageData(key)

        binding.commentBtn.setOnClickListener {
            insertComment(key)
        }
        commentLVdapter = CommentLVAdapter(commentDataList)
        binding.commentLv.adapter = commentLVdapter

        getCommentData(key)
    }

    fun getCommentData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentDataList.clear()

                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                }

                commentLVdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.homeCommentRef.child(key).addValueEventListener(postListener)
    }

    fun insertComment(key: String) {
        FBRef.homeCommentRef
            .child(key)
            .push()
            .setValue(
                CommentModel(
                    binding.comment.text.toString(),
                    FBAuth.getTime()
                )
            )
        Toast.makeText(this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show()
        binding.comment.setText("")
    }

    private fun showDialog() {
        val dialogV = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogV)
            .setTitle("????????? ??????/??????")

        val alertDialog = builder.show()

        //??????
        alertDialog.findViewById<Button>(R.id.correction_btn)?.setOnClickListener {
            val intent = Intent(this, HomeCrrectionActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
            alertDialog.dismiss()
        }
        //??????
        alertDialog.findViewById<Button>(R.id.delete_btn)?.setOnClickListener {
            FBRef.homeBoardRef.child(key).removeValue()
            Toast.makeText(this, "???????????? ?????????????????????.", Toast.LENGTH_LONG).show()
            finish()
            alertDialog.dismiss()
        }
    }

    private fun getImageData(key: String) {

        val storageReference = Firebase.storage.reference.child(key)
        val imageV = binding.imageRead

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageV)
            } else {
                binding.imageRead.isVisible = false
            }
        })
    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try {
                    val dataModel = dataSnapshot.getValue(HomeModel::class.java)

                    binding.titleRead.text = dataModel!!.title
                    binding.contentRead.text = dataModel!!.content
                    binding.timeRead.text = dataModel!!.time
                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid

                    if (myUid.equals(writerUid)) {
                        binding.moreBtn.isVisible = true
                    } else {
                        Log.d(TAG, "??? ???????????? ????????????.")
                    }

                } catch (e: java.lang.Exception) {
                    Log.d(TAG, "????????????")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.homeBoardRef.child(key).addValueEventListener(postListener)

    }
}
