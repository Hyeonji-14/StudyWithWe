package com.example.studyw.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object {

        private val database = Firebase.database

        val boardRef = database.getReference("board")

        val commentRef = database.getReference("comment")

        val homeBoardRef = database.getReference("homeBoard")

        val homeCommentRef = database.getReference("homeComment")


    }
}