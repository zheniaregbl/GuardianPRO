package com.example.guardianpro

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.guardianpro.databinding.ApplicItemBinding
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.android.synthetic.main.info_access_fragment.view.*
import kotlinx.coroutines.runBlocking

class InfoDialogFragment(applic: Applic, binding: ApplicItemBinding, context: Context) : DialogFragment() {
    private val form = applic
    private val item = binding
    private val activity = context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme_transparent)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView : View = inflater.inflate(R.layout.info_access_fragment, container, false)

        rootView.tvInfo2.text = "Фамилия: ${form.lastName}"
        rootView.tvInfo3.text = "Имя: ${form.name}"
        rootView.tvInfo4.text = "Отчество: ${form.secondName}"
        rootView.tvInfo5.text = "Подр-ние: ${form.division}"
        rootView.tvInfo6.text = "Дата: ${form.date}"
        rootView.tvInfo7.text = "Время: ${form.timeIn}"

        rootView.btClose.setOnClickListener {
            dismiss()
        }

        rootView.btAccess.setOnClickListener {
            val connectionManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activityNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
            val isConnected = activityNetwork?.isConnectedOrConnecting

            if(isConnected == true){
                val code: Int = runBlocking {
                    Great().access(form.id, true)
                }

                if(code == 200){
                    Toast(activity).apply {
                        val layout = layoutInflater.inflate(R.layout.custom_toast, parentLayout)
                        layout.tvTextToast.text = "Доступ предоставлен"
                        layout.imageToast.setImageResource(R.drawable.ic_access_toast)

                        duration = Toast.LENGTH_SHORT
                        setGravity(Gravity.BOTTOM, 0, 70)
                        view = layout
                    }.show()

                    item.cardViewItem.strokeColor = Color.parseColor("#66D371")

                    form.access = true
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