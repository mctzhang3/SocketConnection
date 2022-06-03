package com.mzhang.connectserverviasocket

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
            }
        }
    }

    fun updateDevice(ipAddress: String, port: Int, deviceAction: String) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val socket = Socket(ipAddress, port)
                val pw = PrintWriter(socket.getOutputStream(), true)
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                pw.write(deviceAction)
                pw.println()

//                _deviceList.postValue(reader.readLines())

                reader.close()
                pw.close()
                socket.close()

            } catch (e: IOException) {
                //
            }
        }
    }

    fun updateDeviceList(ipAddress: String, port: Int, outList: List<String>) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val socket = Socket(ipAddress, port)
                val pw = PrintWriter(socket.getOutputStream(), true)
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                for (out in outList) {
                    pw.write(out)
                    pw.println()
                }

                _deviceList.postValue(reader.readLines())

                reader.close()
                pw.close()
                socket.close()

            } catch (e: IOException) {
                //
            }
        }
    }


}