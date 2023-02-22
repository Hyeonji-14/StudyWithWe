package com.example.studyw.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.studyw.R

class BoardListVAdapter(val boardList : MutableList<BoardModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return boardList.size
    }

    override fun getItem(position: Int): Any {
        return boardList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {

            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)

        }
        val title = view?.findViewById<TextView>(R.id.title_text)
        title!!.text = boardList[position].title

        val content = view?.findViewById<TextView>(R.id.content_text)
        content!!.text = boardList[position].content

        val time = view?.findViewById<TextView>(R.id.time_text)
        time!!.text = boardList[position].time

        return view!!
    }


}