package com.example.guardianpro

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.example.guardianpro.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btEnter.setOnClickListener {
            val connectionManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activityNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
            val isConnected = activityNetwork?.isConnectedOrConnecting

            if(isConnected == true){
                val code: Int = runBlocking {
                    Great().great(binding.edCode.text.toString(), binding.edPassword.text.toString())
                }

                if(code == 200){
                    val intent = Intent(this, ApplicActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast(this).apply {
                        val layout = layoutInflater.inflate(R.layout.custom_toast, parentLayout)
                        layout.tvTextToast.text = "Ошибка $code"
                        layout.imageToast.setImageResource(R.drawable.ic_error)

                        duration = Toast.LENGTH_SHORT
                        setGravity(Gravity.BOTTOM, 0, 70)
                        view = layout
                    }.show()
                }
            }
            else{
                Toast(this).apply {
                    val layout = layoutInflater.inflate(R.layout.custom_toast, parentLayout)
                    layout.tvTextToast.text = "Отсутствие сети"
                    layout.imageToast.setImageResource(R.drawable.ic_no_wifi)

                    duration = Toast.LENGTH_SHORT
                    setGravity(Gravity.BOTTOM, 0, 70)
                    view = layout
                }.show()
            }
        }
    }
}