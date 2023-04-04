package com.example.guardianpro

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.guardianpro.databinding.ApplicItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ApplicAdapter(val listener: Listener) : RecyclerView.Adapter<ApplicAdapter.ApplicHolder>(), Filterable {
    var applicList = ArrayList<Applic>()
    var secondApplicList = ArrayList<Applic>()

    class ApplicHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ApplicItemBinding.bind(item)
        var id: Int? = null
        var applicIn: Applic? = null
        var name: String? = null
        var thirdName: String? = null

        @SuppressLint("SetTextI18n")
        fun bind(applic: Applic, listener: Listener) = with(binding){
            name = applic.name
            thirdName = applic.secondName

            tvFio.text = "ФИО: ${applic.lastName} ${name?.first()}.${thirdName?.first()}."

            if(getCurrentDateTime().toString("yyyy-MM-dd") == applic.date){
                tvDate.text = "Дата: Сегодня"
            }
            else{
                tvDate.text = "Дата: ${applic.date}"
            }

            tvDivision.text = "Подр-ние: ${applic.division}"

            if(applic.access == true){
                cardViewItem.strokeColor = Color.parseColor("#66D371")
            }
            else{
                cardViewItem.strokeColor = Color.parseColor("#C4C4C4")
            }

            btNext.setOnClickListener {
                listener.onClick(applic, binding)
            }

            id = applic.id
            applicIn = applic
        }

        private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(this)
        }

        private fun getCurrentDateTime(): Date {
            return Calendar.getInstance().time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.applic_item, parent, false)
        return ApplicHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicHolder, position: Int) {
        holder.bind(applicList[position], listener)
    }

    override fun getItemCount(): Int {
        return applicList.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                applicList = if (charSearch.isNotEmpty()){
                    val resultList = ArrayList<Applic>()

                    for (row in applicList){
                        if (row.lastName.toString().lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                } else {
                    secondApplicList
                }

                val filterResults = FilterResults()
                filterResults.values = applicList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                applicList = results?.values as ArrayList<Applic>
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addApplic(applic: Applic){
        applicList.add(applic)
        secondApplicList = applicList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(id: Int?){
        for(i in applicList.indices){
            if(applicList[i].id == id){
                applicList.removeAt(i)
                break
            }
        }
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<Applic>{
        return applicList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSortList(list: ArrayList<Applic>){
        applicList = list
        notifyDataSetChanged()
    }

    interface Listener{
        fun onClick(applic: Applic, binding: ApplicItemBinding)
    }
}