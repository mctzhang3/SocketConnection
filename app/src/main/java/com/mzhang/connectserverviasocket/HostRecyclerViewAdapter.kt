package com.mzhang.connectserverviasocket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView


class HostRecyclerViewAdapter(private var mHostList: MutableList<HostNameStatus>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    private var mHostList: List<HostNameStatus>? = null

//    @SuppressLint("NotifyDataSetChanged")
//    fun updateHostList(hosts: List<HostNameStatus>?) {
//        this.mHostList = hosts
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.host_list_item, viewGroup, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mHostList.let { hostList ->
            (holder as ViewHolder).bind(hostList[position])
        }

    }

    override fun getItemCount(): Int {
        return mHostList.size ?: 0

    }

    data class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvStatus: SwitchCompat = view.findViewById(R.id.status)
        private val tvHostName: TextView = view.findViewById(R.id.textViewHostName)

        fun bind(host: HostNameStatus) {
            tvHostName.text = host.hostName
            tvStatus.isChecked = host.status == "on"
            tvStatus.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    host.status = "on"
                } else {
                    host.status = "off"
                }
            }

        }
    }


}