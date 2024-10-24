package com.example.celestialconnect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(private val context: Context, private val helperList: ArrayList<HelperClass>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val helper = helperList[position]
        Glide.with(context).load(helper.imageUrl).into(holder.recyclerImage)
        holder.recyclerCaption.text = helper.caption
    }

    override fun getItemCount(): Int {
        return helperList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerImage: ImageView = itemView.findViewById(R.id.recyclerImage)
        val recyclerCaption: TextView = itemView.findViewById(R.id.recyclerCaption)
    }
}
