package com.roralabs.tictagtoy

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_login.*


class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    //database instance
    var database = FirebaseDatabase.getInstance()
    var myRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()





    }

    fun loginEvent(view:View){
        loginToFireBase(editTextEmail.text.toString(), editTextPassword.text.toString())
    }

    private fun loginToFireBase(email:String, password:String){
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->

                if(task.isSuccessful){
                    var currentUser = mAuth!!.currentUser
                    //Add to Database
                    if(currentUser!=null){
                        myRef.child("Users").child(splitSring(currentUser.email.toString())).setValue(currentUser.uid)
                    }
                    loadGame()
                }else{
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        loadGame()
    }

    fun loadGame(){
        var currentUser = mAuth!!.currentUser

        if(currentUser!=null){

            //save data to db


            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", currentUser.email)
            intent.putExtra("uid", currentUser.uid)

            startActivity(intent)
        }
    }

    fun splitSring(str:String) : String{
        var split = str.split("@")
        return split[0]
    }

}
