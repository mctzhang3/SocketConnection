package com.mzhang.connectserverviasocket

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mzhang.connectserverviasocket.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    var viewModel: SocketConnection? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: HostRecyclerViewAdapter
    private var hosts: MutableList<HostNameStatus> = arrayListOf()
    private var ipaddress: String? = null
    private val port by lazy { Utils.ROUTER_PORT }
    private var hostName = ""
    private var macAddress = ""
    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SocketConnection::class.java)

        sharedPref = getPreferences(Context.MODE_PRIVATE)

        hosts = arrayListOf()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        getDeviceList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.query_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> displaySettingsDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("InflateParams")
    private fun displaySettingsDialog() {
        val promptsView = layoutInflater.inflate(R.layout.ipaddress_prompts, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

        alertDialogBuilder.setView(promptsView)
        val userIpAddress = promptsView.findViewById<EditText>(R.id.etIpAddress1)
        alertDialogBuilder.setCancelable(false)
            .setPositiveButton("OK") { dialog, id -> // get user input and set it to result
                sharedPref?.edit()?.putString(Utils.PREFERRED_IP_ADDRESS, userIpAddress.text.toString())?.commit()
                getDeviceList()
            }.setNegativeButton("Cancel"
            ) { dialog, id -> dialog.cancel() }
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }

    private fun initObserver() {
        viewModel?.deviceList?.observe(this) {
            hosts.clear()
            var status = ""
            for (s in it) {
                hostName = ""
                macAddress = ""

                val parts: List<String> = s.split("|")

                for (part in parts) {
                    if (part.startsWith(Utils.HOST_NAME, ignoreCase = true)) {
                        hostName += part
                    }
                    if (part.startsWith(Utils.MAC_ADDRESS, ignoreCase = true)) {
                        hostName += part
                        val mac = part.split(Utils.MAC_ADDRESS)
                        if (mac.size > 1) macAddress = part
                        macAddress = part.substring(4)
                    }
                    if (part.startsWith(Utils.HOST_STATUS)) {
                        status = part.substring(6)
                    }
                }
                val host = HostNameStatus(hostName, macAddress, status)
                hosts.add(host)
            }

            adapter = HostRecyclerViewAdapter(hosts, object : HostRecyclerViewAdapter.ActionListener {
                override fun onBlockButtonClicked(host: String) {
                    val action = "block:$host"
                    viewModel?.updateDevice(ipaddress?:"", port, action)
                }

                override fun onEnablButtonClicked(host: String) {
                    val action = "enable:$host"
                    viewModel?.updateDevice(ipaddress?:"", port, action)
                }

            })
            binding.rvHostList.adapter = adapter
            binding.rvHostList.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun getDeviceList() {
        ipaddress = sharedPref?.getString(Utils.PREFERRED_IP_ADDRESS, "")

        if (ipaddress.isNullOrEmpty().not()) {
            viewModel?.getDeviceList(ipaddress?:"", port, Utils.GET_DEVICE_LIST_COMMAND)
        } else {
            displaySettingsDialog()
        }
    }

}