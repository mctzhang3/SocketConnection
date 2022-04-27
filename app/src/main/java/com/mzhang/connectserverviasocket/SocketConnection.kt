package com.mzhang.connectserverviasocket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.Executors

class SocketConnection : ViewModel(){
    private val _deviceList = MutableLiveData<List<String>>()
    val deviceList : LiveData<List<String>> = _deviceList
//    var s: Socket? = null
//    var pw: PrintWriter? = null
//    var reader: BufferedReader? = null

    fun getDeviceList(ipAddress: String, port: Int, query: String) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val socket = Socket(ipAddress, port)
                val pw = PrintWriter(socket.getOutputStream(), true)
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                pw.write(query)
                pw.println()
                _deviceList.postValue(reader.readLines())

                reader.close()
                pw.close()
                socket.close()

            } catch (e: IOException) {
                //
                Log.d("amz095", "IOException ... $e")
            }
        }
    }

//    fun closeSocket() {
//        try {
//            reader?.close()
//            pw?.close()
//            s?.close()
//        } catch (e: IOException) {
//            //
//        }
//    }

}