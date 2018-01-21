package voen.experiment.firebasesimplechat.section.main

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by voen on 12/8/17.
 */
class GeneralRecyclerViewAdapter<T>(@LayoutRes private val resId: Int, private val rooms: MutableList<T>, private val listener: (T) -> Unit, private val viewHolderBindAction: (T, View) -> Unit) : RecyclerView.Adapter<GeneralRecyclerViewAdapter.RoomHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = RoomHolder<T>(LayoutInflater.from(parent?.context).inflate(resId, parent, false))

    override fun onBindViewHolder(holder: RoomHolder<T>?, position: Int) {
        holder?.bind(rooms[position], listener, viewHolderBindAction)
    }

    override fun getItemCount() = rooms.size

    class RoomHolder<T>(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(any: T, listener: (T) -> Unit, viewHolderBindAction: (T, View) -> Unit) = with(itemView) {
            viewHolderBindAction(any, itemView)
            setOnClickListener { listener(any) }
        }
    }
}