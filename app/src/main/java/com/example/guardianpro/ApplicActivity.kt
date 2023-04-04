package com.example.guardianpro

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guardianpro.databinding.ApplicActivityBinding
import com.example.guardianpro.databinding.ApplicItemBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_applic.*
import kotlinx.android.synthetic.main.applic_activity.*
import kotlinx.android.synthetic.main.custom_toast.*
import kotlinx.android.synthetic.main.custom_toast.view.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class ApplicActivity : AppCompatActivity(), ApplicAdapter.Listener {
    lateinit var binding: ApplicActivityBinding
    private var adapter = ApplicAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApplicActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTabLayout()
        initSearch()
        initApplic()
        initButtons()
    }

    private fun initTabLayout() = with(binding){
        tabLayout.addOnTabSelectedListener(object: OnTabSelectedListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position){
                    0 -> {
                        val newAdapter = ApplicAdapter(this@ApplicActivity)

                        val currentDate = getCurrentDateTime().toString("yyyy-MM-dd")

                        val gson = Gson()

                        val stringJson = runBlocking {
                            Great().greater()
                        }

                        val listApplic = gson.fromJson(stringJson, ExampleJson2KtKotlin::class.java)

                        for (i in listApplic.applications.indices){
                            if(listApplic.applications[i].timeOut == null && LocalDate.parse(listApplic.applications[i].date) >= LocalDate.parse(currentDate)){
                                newAdapter.addApplic(listApplic.applications[i])
                            }
                        }

                        adapter = newAdapter
                        rcView2.adapter = adapter
                    }
                    1 -> {
                        val newAdapter = ApplicAdapter(this@ApplicActivity)

                        val currentDate = getCurrentDateTime().toString("yyyy-MM-dd")

                        val gson = Gson()

                        val stringJson = runBlocking {
                            Great().greater()
                        }

                        val listApplic = gson.fromJson(stringJson, ExampleJson2KtKotlin::class.java)

                        for (i in listApplic.applications.indices){
                            if(listApplic.applications[i].timeOut != null){
                                newAdapter.addApplic(listApplic.applications[i])
                            }
                        }

                        adapter = newAdapter
                        rcView2.adapter = adapter
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                return
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                return
            }

        })
    }

    private fun initSearch() = with(binding){
        searchLine.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
    }

    @SuppressLint("NewApi")
    private fun initApplic() = with(binding) {
        rcView2.layoutManager = LinearLayoutManager(this@ApplicActivity)
        rcView2.adapter = adapter

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
        imBtFilterDate.setOnClickListener {
            val list = adapter.getList()

            list.sortBy { LocalDate.parse(it.date) }

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

        imBtFilterDivision.setOnClickListener {
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

    override fun onClick(applic: Applic, binding: ApplicItemBinding) = with(binding){
        if (tabLayout.selectedTabPosition == 0){
            if(applic.access == false){
                val dialog = InfoDialogFragment(applic, binding, this@ApplicActivity, false)

                dialog.show(supportFragmentManager, "InfoDialog")
            }
            else{
                val dialog = TimeDialogFragment(this@ApplicActivity, adapter, applic)

                dialog.show(supportFragmentManager, "TimeDialog")
            }

            Toast.makeText(this@ApplicActivity, "${applic.access}", Toast.LENGTH_SHORT).show()
        }
        else{
            val dialog = InfoDialogFragment(applic, binding, this@ApplicActivity, true)

            dialog.show(supportFragmentManager, "InfoDialog")
        }
    }
}
