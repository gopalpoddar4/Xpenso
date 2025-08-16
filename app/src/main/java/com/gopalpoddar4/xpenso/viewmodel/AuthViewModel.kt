package com.gopalpoddar4.xpenso.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.gopalpoddar4.xpenso.data.UserModel
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //Create account using email and password
    fun createAccountUsingEmailPass(name:String,email: String,password: String,onSuccessCreation:()->Unit,onFailedCreation:(String)->Unit){
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener {result->
                    val uid = result.user?.uid
                    if (uid!=null){
                        createUserInFirestore(name,email,uid,onSuccessCreation, onFailedCreation)
                        createUserInDatabase(name,email,uid)
                    }
                }
                .addOnFailureListener { result->
                    onFailedCreation(result.message.toString())
                }
        }
    }

    //Login account using email and password
    fun loginAccountUsingEmailPass(email: String,password: String,onSuccessLogin:()->Unit,onFailedLogin:(String)->Unit){
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        onSuccessLogin()
                    }else{
                        onFailedLogin(task.exception?.message.toString())
                    }
                }
                .addOnFailureListener { task->
                    onFailedLogin(task.message.toString())
                }
        }
    }

    fun createUserInDatabase(name: String,email: String,uid: String){
        val user = UserModel(name,email)

        val dbRef = FirebaseDatabase.getInstance().reference

        dbRef.child("xpenso").child("users").child(uid).setValue(user)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }

    }

    fun createUserInFirestore(name: String,email: String,uid: String,onSuccessTask:()->Unit,onFailedTask:(String)->Unit){
        val user = UserModel(name,email)
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                onSuccessTask()
            }
            .addOnFailureListener {
                onFailedTask(it.message.toString())
            }
    }


}