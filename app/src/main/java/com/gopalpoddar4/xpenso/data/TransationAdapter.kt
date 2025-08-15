package com.gopalpoddar4.xpenso.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.gopalpoddar4.xpenso.R

class TransationAdapter (private val transactionList: List<ExpenceModel>,private val onClick:(String)-> Unit) : RecyclerView.Adapter<TransationAdapter.Viewholder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Viewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expence_sample_layout,parent,false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(
        holder: Viewholder,
        position: Int
    ) {
        val transation = transactionList[position]
        holder.bind(transation)
        holder.itemView.setOnClickListener {
            onClick(transation.id)
        }
    }

    override fun getItemCount(): Int {
       return transactionList.size
    }


    class Viewholder(itemView: View): RecyclerView.ViewHolder(itemView){
        val expenceName = itemView.findViewById<TextView>(R.id.expenceName)
        val date = itemView.findViewById<TextView>(R.id.expenceDate)
        val amount = itemView.findViewById<TextView>(R.id.expenceAmount)

        fun bind(transaction: ExpenceModel){
            expenceName.text = transaction.name
            date.text = transaction.date
            amount.text = "$ ${transaction.amount}"

        }

    }
}