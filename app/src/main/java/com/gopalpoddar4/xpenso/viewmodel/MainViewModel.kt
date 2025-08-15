package com.gopalpoddar4.xpenso.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.gopalpoddar4.xpenso.data.ExpenceModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.exp

class MainViewModel : ViewModel(){

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    fun addExpense(name: String,date: String,amount: Double,desc: String,onTaskSuccess:()->Unit,onTaskFailed:(String)->Unit){

        val uid = auth.currentUser?.uid
        database = Firebase.database.reference

        val month = LocalDate.now()
        val formmatter = DateTimeFormatter.ofPattern("MMMM")
        val formateMonth = month.format(formmatter)

        val expenseKey: String =
            database.child("xpenso").child("users").child(uid.toString()).child("expense").push().key.toString()

        val expenseModel = ExpenceModel(name=name, date = date, amount = amount, id = expenseKey, note = desc, month = formateMonth)

        database.child("xpenso").child("users").child(uid.toString()).child("expense").child(expenseKey).setValue(expenseModel)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    onTaskSuccess()
                }else{
                    onTaskFailed(it.exception?.message.toString())
                }
            }
            .addOnFailureListener {
                onTaskFailed(it.message.toString())
            }
    }

    fun getAllExpense(expenseList:(MutableList<ExpenceModel>)-> Unit, onTaskFailed:(String)->Unit){
        val expenseList = mutableListOf<ExpenceModel>()
        val uid = auth.currentUser?.uid

        database = Firebase.database.getReference("xpenso/users/$uid/expense")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                expenseList.clear()

                for (expenseSnap in snapshot.children){
                    val expense = expenseSnap.getValue(ExpenceModel::class.java)
                    expense?.let {
                        expenseList.add(it)
                    }
                }

                val reversList= expenseList.reversed()
                expenseList(reversList as MutableList<ExpenceModel>)

            }

            override fun onCancelled(error: DatabaseError) {
                onTaskFailed(error.message.toString())
            }

        })


    }

    fun updateExpense(name: String,date: String,amount: Double,desc: String,id:String,onTaskSuccess:()->Unit,onTaskFailed:(String)->Unit){
        val uid = auth.currentUser?.uid
        val expenseModel = ExpenceModel(name = name, id = id, date = date, amount = amount, note = desc)
        database = Firebase.database.getReference("xpenso/users/$uid/expense/$id")

        database.setValue(expenseModel)
            .addOnCompleteListener {
                onTaskSuccess()
            }
            .addOnFailureListener {
                onTaskFailed(it.message.toString())

            }
    }

    fun getExpenseById(id: String, onTaskSuccess: (ExpenceModel) -> Unit, onTaskFailed: (String) -> Unit){
        val uid = auth.currentUser?.uid
        database = Firebase.database.getReference("xpenso/users/$uid/expense/$id")
        database.get()
            .addOnSuccessListener {snapshot->
                if (snapshot.exists()){
                    val expense = snapshot.getValue(ExpenceModel::class.java)
                    expense?.let {
                        onTaskSuccess(it)
                    }
                }
            }
            .addOnFailureListener {
                onTaskFailed(it.message.toString())
            }
    }

    fun deleteExpense(id:String,onTaskSuccess:()-> Unit){
        val uid = auth.currentUser?.uid
        database = Firebase.database.getReference("xpenso/users/$uid/expense/$id")
        database.removeValue()
            .addOnCompleteListener {
                onTaskSuccess()
            }
    }
}