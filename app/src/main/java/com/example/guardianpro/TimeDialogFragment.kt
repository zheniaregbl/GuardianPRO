package com.example.guardianpro

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.time_fragment.view.*
import kotlinx.coroutines.runBlocking

class TimeDialogFragment(context: Context, private val adapter: ApplicAdapter, private val applic: Applic) : DialogFragment() {
    private val activity = context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme_transparent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.time_fragment, container, false)

        rootView.timePicker.setIs24HourView(true)

        rootView.btClose2.setOnClickListener {
            dismiss()
        }

        rootView.btDone.setOnClickListener {
            val connectionManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activityNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
            val isConnected = activityNetwork?.isConnectedOrConnecting

            if(isConnected == true){
                val id = applic.id
                var time = ""

                time = if(rootView.timePicker.hour < 10 && rootView.timePicker.minute < 10){
                    "0${rootView.timePicker.hour}:0${rootView.timePicker.minute}"
                } else if(rootView.timePicker.hour < 10){
                    "0${rootView.timePicker.hour}:${rootView.timePicker.minute}"
                } else if(rootView.timePicker.minute < 10){
                    "${rootView.timePicker.hour}:0${rootView.timePicker.minute}"
                } else{
                    "${rootView.timePicker.hour}:${rootView.timePicker.minute}"
                }

                val code: Int = runBlocking {
                    Great().setTimeOut(id, time)
                }

                if(code == 200){
                    Toast(activity).apply {
                        val layout = layoutInflater.inflate(R.layout.custom_toast, parentLayout)
                        layout.tvTextToast.text = "Время установлено"
                        layout.imageToast.setImageResource(R.drawable.ic_access_toast)

                        duration = Toast.LENGTH_SHORT
                        setGravity(Gravity.BOTTOM, 0, 70)
                        view = layout
                    }.show()

                    adapter.removeItem(id)
                }
                else{
                    Toast(activity).apply {
                        val layout = layoutInflater.inflate(R.layout.custom_toast, parentLayout)
                        layout.tvTextToast.text = "Неизвестная ошибка"
                        layout.imageToast.setImageResource(R.drawable.ic_error)

                        duration = Toast.LENGTH_SHORT
                        setGravity(Gravity.BOTTOM, 0, 70)
                        view = layout
                    }.show()
                }
            }
            else{
                Toast(activity).apply {
                    val layout = layoutInflater.inflate(R.layout.custom_toast, parentLayout)
                    layout.tvTextToast.text = "Отсутствие сети"
                    layout.imageToast.setImageResource(R.drawable.ic_no_wifi)

                    duration = Toast.LENGTH_SHORT
                    setGravity(Gravity.BOTTOM, 0, 70)
                    view = layout
                }.show()
            }

            dismiss()
        }

        return rootView
    }
}