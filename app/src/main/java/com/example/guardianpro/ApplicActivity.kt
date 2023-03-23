package com.example.guardianpro

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guardianpro.databinding.ActivityApplicBinding
import com.example.guardianpro.databinding.ApplicItemBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_applic.*
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ApplicActivity : AppCompatActivity(), ApplicAdapter.Listener {
    lateinit var binding: ActivityApplicBinding
    private var adapter = ApplicAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initApplic()
        initButtons()
    }

    @SuppressLint("NewApi")
    private fun initApplic() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(this@ApplicActivity)
        rcView.adapter = adapter

        val currentDate = getCurrentDateTime().toString("yyyy-MM-dd")

        val gson = Gson()

        val stringJson = runBlocking {
            Great().greater()
        }

        val listApplic = gson.fromJson(stringJson, ExampleJson2KtKotlin::class.java)

        for (i in listApplic.applications.indices){
            if(listApplic.applications[i].timeOut == null && LocalDate.parse(listApplic.applications[i].date) >= LocalDate.parse(currentDate)){
                adapter.addApplic(listApplic.applications[i])
            }
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    private  fun initButtons() = with(binding){
        btFilterDate.setOnClickListener {
            val list = adapter.getList()

            list.sortBy { LocalDate.parse(it.date) }

            adapter.setSortList(list)

            rcView.adapter = adapter

            Toast(this@ApplicActivity).apply {
                val layout = layoutInflater.inflate(R.layout.custom_toast, parentLayout)
                layout.tvTextToast.text = "Фильтрация произведена"
                layout.imageToast.setImageResource(R.drawable.ic_filter)

                duration = Toast.LENGTH_SHORT
                setGravity(Gravity.BOTTOM, 0, 70)
                view = layout
            }.show()
        }

        btFilterDivision.setOnClickListener {
            val list = adapter.getList()

            list.sortBy { it.division }

            adapter.setSortList(list)

            Toast(this@ApplicActivity).apply {
                val layout = layoutInflater.inflate(R.layout.custom_toast, parentLayout)
                layout.tvTextToast.text = "Фильтрация произведена"
                layout.imageToast.setImageResource(R.drawable.ic_filter)

                duration = Toast.LENGTH_SHORT
                setGravity(Gravity.BOTTOM, 0, 70)
                view = layout
            }.show()
        }
    }

    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    override fun onClick(applic: Applic, binding: ApplicItemBinding) = with(binding) {
        if(applic.access == false){
            val dialog = InfoDialogFragment(applic, binding, this@ApplicActivity)

            dialog.show(supportFragmentManager, "InfoDialog")
        }
        else{
            val dialog = TimeDialogFragment(this@ApplicActivity, adapter, applic)

            dialog.show(supportFragmentManager, "TimeDialog")
        }
    }
}
