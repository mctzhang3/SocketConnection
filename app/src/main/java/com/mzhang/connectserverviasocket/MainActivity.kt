package com.mzhang.connectserverviasocket

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mzhang.connectserverviasocket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var viewModel: SocketConnection? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: HostRecyclerViewAdapter
    private var hosts: MutableList<HostNameStatus> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SocketConnection::class.java)

        supportActionBar?.hide()
//        if(getSupportedActionbar()!=null)
//            this.getSupportedActionBar().hide();

        hosts = arrayListOf()
//        initRecycler()
        initObserver()
    }

//    private fun initRecycler() {
//        adapter = HostRecyclerViewAdapter(hosts)
//        binding.rvHostList.apply {
//            this.adapter = adapter
//            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
//        }
//        adapter.updateHostList(null)
//    }

    private fun initObserver() {
        viewModel?.deviceList?.observe(this) {
            hosts.clear()
            for (s in it) {
                val parts: List<String> = s.split("|")
                var name = if (parts.isNotEmpty()) {
                    parts[0]
                } else {
                    ""
                }
                var status = if (parts.size > 1) {
                    parts[1]
                } else {
                    ""
                }
//                if (parts.size > 1) {
                    val host: HostNameStatus = HostNameStatus(name, status)
                    hosts.add(host)
//                }
//                Log.d("amz095", host.toString())
            }
            Log.d("amz095", hosts.toString())

            adapter = HostRecyclerViewAdapter(hosts)
            binding.rvHostList.adapter = adapter
            binding.rvHostList.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }
    private var ipaddress = ""
    private var port = 0

    fun getDeviceList(v: View) {
        ipaddress = binding.etIpAddress.text.toString()
        val portStr = binding.etIpPort.text.toString()
        try {
            port = portStr.toInt()
        } catch (e: Exception) {
            //
        }
//        val port = binding.etIpPort.text.toString().toInt()
        val query = binding.etQuery.text.toString()
        viewModel?.getDeviceList(ipaddress, port, query)
    }

    fun updateDeviceList(view: View) {
        Log.d("amz095", hosts.toString())

        var outList: MutableList<String> = arrayListOf("update")
        for (out in hosts) {
            val str = out.hostName + "|" + out.status
            outList.add(str)
        }

        // Empty line indicates end of file
        outList.add("\n")
        viewModel?.updateDeviceList(ipaddress, port,outList)
    }
}