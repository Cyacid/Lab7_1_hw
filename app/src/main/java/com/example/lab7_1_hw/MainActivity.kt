package com.example.lab7_1_hw

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import android.widget.Button
import android.widget.Toast
import androidx.annotation.NonNull
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(){
    private var rab = 0
    private var tur = 0
    private var btnStart : Button? = null
    private var seekBar2 : SeekBar? = null
    private var seekBar : SeekBar? =null
    private var job : Job? = null
    private var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar = findViewById(R.id.seekBar2)
        seekBar2 = findViewById(R.id.seekBar3)
        btnStart = findViewById(R.id.button)
        btnStart?.setOnClickListener{
            btnStart?.isEnabled = false
            rab = 0
            tur = 0
            seekBar?.progress = 0
            seekBar2?.progress = 0
            job = GlobalScope.launch(Dispatchers.Main){
                runCoroutine()
            }
            runThread()

        }
    }

    private suspend fun runCoroutine() = withContext(Dispatchers.Main){
        while(rab<=100 && tur<=100) {
            try {
                tur += (Math.random() * 3).toInt()
                seekBar?.progress = tur
                delay(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        Toast.makeText(this@MainActivity,
               "烏龜獲勝", Toast.LENGTH_SHORT).show()
        btnStart?.isEnabled = true
        job?.cancel()
    }
    private fun runThread(){
        Thread{
            while(rab<=100 && tur<=100){
                try{
                    Thread.sleep(100)
                    rab += (Math.random() * 3).toInt()
                    val msg = Message()
                    msg.what = 1
                    mHandler.sendMessage(msg)
                }catch (e : InterruptedException){
                    e.printStackTrace()
                }
            }
        }.start()
    }
    private val  mHandler = Handler(Handler.Callback { msg ->
            when (msg.what) {
                1 -> seekBar2?.progress = rab
            }
            if (rab >= 100 && tur < 100) {
                Toast.makeText(this@MainActivity,
                    "兔子獲勝", Toast.LENGTH_SHORT).show()
                btnStart?.isEnabled = true
                job?.cancel()
            }
        false
    })
}
