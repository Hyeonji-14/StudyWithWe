package com.example.studyw.homeBoard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.studyw.R

class HomeBoardAdapter(val homeList: MutableList<HomeModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return homeList.size
    }

    override fun getItem(position: Int): Any {
        return homeList[position]
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
        title!!.text = homeList[position].title

        val content = view?.findViewById<TextView>(R.id.content_text)
        content!!.text = homeList[position].content

        val time = view?.findViewById<TextView>(R.id.time_text)
        time!!.text = homeList[position].time

        return view!!
    }
}