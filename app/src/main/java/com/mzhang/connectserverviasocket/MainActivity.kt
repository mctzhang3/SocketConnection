package com.mzhang.connectserverviasocket

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mzhang.connectserverviasocket.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket

class MainActivity : AppCompatActivity() {

    var viewModel: SocketConnection? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(SocketConnection::class.java)

        viewModel?.deviceList?.observe(this) {
            var dev = ""
            for (s in it) {
                dev = dev + s + "\n"
            }
            binding.textViewDeviceList.text = dev
        }
    }


    fun getDeviceList(v: View) {
        val ipaddress: String = binding.etIpAddress.text.toString()
        val portStr = binding.etIpPort.text.toString()
        var port = 0
        try {
            port = portStr.toInt()
        } catch (e: Exception) {
            //
        }
//        val port = binding.etIpPort.text.toString().toInt()
        val query = binding.etQuery.text.toString()
        viewModel?.getDeviceList(ipaddress, port, query)
    }
}