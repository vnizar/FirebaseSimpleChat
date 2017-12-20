package voen.experiment.firebasesimplechat.section.main

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by voen on 12/8/17.
 */
class GeneralRecyclerViewAdapter(@LayoutRes private val resId: Int, private val rooms: MutableList<Any>, private val listener: (Any) -> Unit, private val viewHolderBindAction: (Any, View) -> Unit) : RecyclerView.Adapter<GeneralRecyclerViewAdapter.RoomHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = RoomHolder(LayoutInflater.from(parent?.context).inflate(resId, parent, false))

    override fun onBindViewHolder(holder: RoomHolder?, position: Int) {
        holder?.bind(rooms[position], listener, viewHolderBindAction)
    }

    override fun getItemCount() = rooms.size

    class RoomHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(any: Any, listener: (Any) -> Unit, viewHolderBindAction: (Any, View) -> Unit) = with(itemView) {
            viewHolderBindAction(any, itemView)
            setOnClickListener { listener(any) }
        }
    }
}