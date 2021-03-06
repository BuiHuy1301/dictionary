package com.example.mydictionary.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mydictionary.android.R
import com.example.mydictionary.android.listener.ItemFragmentClickListener

class RecentSearchAdapter(var list: ArrayList<String>) :
    RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>() {

    var mItemClickListener: ItemFragmentClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recent_search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = list[position]
        holder.textView.text = text
        holder.itemView.setOnClickListener {
            mItemClickListener?.onClickItem(text)
        }
        holder.fillButton.setOnClickListener {
            mItemClickListener?.onClickFillButton(text)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setItemClickListener(itemClickListener: ItemFragmentClickListener) {
        mItemClickListener = itemClickListener
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val textView: TextView = item.findViewById(R.id.text)
        val fillButton: Button = item.findViewById(R.id.fill_button)
    }
}