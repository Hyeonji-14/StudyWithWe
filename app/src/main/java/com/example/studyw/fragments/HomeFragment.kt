package com.example.studyw.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.studyw.R
import com.example.studyw.board.BoardListVAdapter
import com.example.studyw.board.BoardModel
import com.example.studyw.board.BoardReadActivity
import com.example.studyw.board.BoardWriteActivity
import com.example.studyw.databinding.ActivityLoginBinding
import com.example.studyw.databinding.FragmentHomeBinding
import com.example.studyw.homeBoard.HomeBoardAdapter
import com.example.studyw.homeBoard.HomeModel
import com.example.studyw.homeBoard.HomeReadActivity
import com.example.studyw.homeBoard.HomeWriteActivity
import com.example.studyw.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeList = mutableListOf<HomeModel>()
    private val boardKeyList = mutableListOf<String>()

    private val TAG = TalkFragment::class.java.simpleName

    private lateinit var homeRVAdapter: HomeBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        homeRVAdapter = HomeBoardAdapter(homeList)
        binding.homeListview.adapter = homeRVAdapter

        binding.homeListview.setOnItemClickListener { parent, view, position, id ->

            val intent = Intent(context, HomeReadActivity::class.java)
            intent.putExtra("key", boardKeyList[position])
            startActivity(intent)
        }

        binding.writebtn.setOnClickListener {
            val intent = Intent(context, HomeWriteActivity::class.java)
            startActivity(intent)
        }


        binding.talkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_talkFragment2)
        }

        getBoardData()

        return binding.root
    }

    private fun getBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                 homeList.clear()

                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(HomeModel::class.java)
                    homeList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }

                boardKeyList.reverse()
                homeList.reverse()
                homeRVAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.homeBoardRef.addValueEventListener(postListener)
    }
}