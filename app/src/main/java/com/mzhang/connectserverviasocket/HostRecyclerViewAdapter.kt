package com.mzhang.connectserverviasocket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class HostRecyclerViewAdapter(private var mHostList: MutableList<HostNameStatus>, var listener: ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    private var mHostList: List<HostNameStatus>? = null

//    @SuppressLint("NotifyDataSetChanged")
//    fun updateHostList(hosts: List<HostNameStatus>?) {
//        this.mHostList = hosts
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.host_list_item, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mHostList.let { hostList ->
            (holder as ViewHolder).bind(hostList[position], listener)
        }

    }

    override fun getItemCount(): Int {
        return mHostList.size ?: 0

    }

    data class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
//        private val tvStatus: SwitchCompat = view.findViewById(R.id.status)
        private val tvHostName: TextView = view.findViewById(R.id.textViewHostName)
        private val buttonBlock = view.findViewById<Button>(R.id.buttonBlock)
        private val buttonEnable = view.findViewById<Button>(R.id.buttonEnable)

        fun bind(host: HostNameStatus, listener: ActionListener) {
            tvHostName.text = host.hostName

            buttonBlock.setOnClickListener {
                listener.onBlockButtonClicked(host.hostName)
            }

            buttonEnable.setOnClickListener {
                listener.onEnablButtonClicked(host.hostName)
            }
        }
    }

    interface ActionListener {
        fun onBlockButtonClicked(host: String)
        fun onEnablButtonClicked(host: String)
    }

}