package ru.ok.itmo.example

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class ArrayViewAdapter(private val data: ArrayList<View>)
    : BaseAdapter() {
    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup): View = data[position]

    fun add(view: View) = data.add(view)
}
