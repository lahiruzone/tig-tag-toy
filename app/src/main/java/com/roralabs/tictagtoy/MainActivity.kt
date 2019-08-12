package com.roralabs.tictagtoy

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    //database instance
    var database = FirebaseDatabase.getInstance()
    var myRef = database.reference

    var myEmail:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        var b: Bundle? =intent.extras
        myEmail = b!!.getString("name")

        incomingRequest()
    }

    protected fun buClick(view:View){
        val buSelected = view as Button
        var cellID = 0
        when(buSelected.id){
            R.id.but1 -> cellID = 1
            R.id.but2 -> cellID = 2
            R.id.but3 -> cellID = 3
            R.id.but4 -> cellID = 4
            R.id.but5 -> cellID = 5
            R.id.but6 -> cellID = 6
            R.id.but7 -> cellID = 7
            R.id.but8 -> cellID = 8
            R.id.but9 -> cellID = 9
        }

//        Toast.makeText(this, "ID"+cellID, Toast.LENGTH_SHORT).show()
        //Play game offline
//        gamePlay(cellID, buSelected)

        //For playing with phone
        myRef.child("PlayerOnline").child(sessionID!!).child(cellID.toString()).setValue(myEmail)
    }

    var playerOne = ArrayList<Int>()
    var playerTwo = ArrayList<Int>()
    var activePlayer = 1
    var gameOnRun = 1 //1-10 Run the game, 10=< stop running the game

    fun gamePlay(cellID:Int, buSelected:Button){

        buSelected.isEnabled = false


        if(gameOnRun<10) {
            if (activePlayer == 1) {
                buSelected.text = "X"
                buSelected.setBackgroundResource(R.color.colorblue)
                playerOne.add(cellID)
                activePlayer = 2

//                autoPlay() //For playing with phone
            } else {
                buSelected.text = "O"
                buSelected.setBackgroundResource(R.color.colorpink)
                playerTwo.add(cellID)
                activePlayer = 1
            }
            gameOnRun++

            findWinner()
        }
    }

    //play with phone
    fun autoPlay(){

        if(gameOnRun!=9){
            var emptyCells = ArrayList<Int>()

            for(cellID in 1..9){
                if(!(playerOne.contains(cellID) || playerTwo.contains(cellID)))
                    emptyCells.add(cellID)
            }

            val r = Random

            val randomIndex = r.nextInt(emptyCells.size-0)+0
            val cellID = emptyCells[randomIndex]

            var buttonSelected:Button?
            when(cellID){
                1->buttonSelected=but1
                2->buttonSelected=but2
                3->buttonSelected=but3
                4->buttonSelected=but4
                5->buttonSelected=but5
                6->buttonSelected=but6
                7->buttonSelected=but7
                8->buttonSelected=but8
                9->buttonSelected=but9
                else->{buttonSelected=but1}
            }

            gamePlay(cellID, buttonSelected)
        }
    }

    fun autoPlayOnline(cellID: Int){

        if(gameOnRun!=9){


            var buttonSelected:Button?
            when(cellID){
                1->buttonSelected=but1
                2->buttonSelected=but2
                3->buttonSelected=but3
                4->buttonSelected=but4
                5->buttonSelected=but5
                6->buttonSelected=but6
                7->buttonSelected=but7
                8->buttonSelected=but8
                9->buttonSelected=but9
                else->{buttonSelected=but1}
            }

            gamePlay(cellID, buttonSelected)
        }
    }

    fun findWinner(){
        var winner = -1

        //row1
        if(playerOne.contains(1) && playerOne.contains(2) && playerOne.contains(3))
            winner = 1
        if(playerTwo.contains(1) && playerTwo.contains(2) && playerTwo.contains(3))
            winner = 2

        //row2
        if(playerOne.contains(4) && playerOne.contains(5) && playerOne.contains(6))
            winner = 1
        if(playerTwo.contains(4) && playerTwo.contains(5) && playerTwo.contains(6))
            winner = 2

        //row3
        if(playerOne.contains(7) && playerOne.contains(8) && playerOne.contains(9))
            winner = 1
        if(playerTwo.contains(7) && playerTwo.contains(8) && playerTwo.contains(9))
            winner = 2

        //col1
        if(playerOne.contains(1) && playerOne.contains(4) && playerOne.contains(7))
            winner = 1
        if(playerTwo.contains(1) && playerTwo.contains(4) && playerTwo.contains(7))
            winner = 2

        //col2
        if(playerOne.contains(2) && playerOne.contains(5) && playerOne.contains(8))
            winner = 1
        if(playerTwo.contains(2) && playerTwo.contains(5) && playerTwo.contains(8))
            winner = 2

        //col3
        if(playerOne.contains(3) && playerOne.contains(6) && playerOne.contains(9))
            winner = 1
        if(playerTwo.contains(3) && playerTwo.contains(6) && playerTwo.contains(9))
            winner = 2

        if(winner==1){
            Toast.makeText(this, "Player One Wins!!", Toast.LENGTH_SHORT).show()
            gameOnRun = 11
        }
        else if (winner==2) {
            Toast.makeText(this, "Phone Wins!!", Toast.LENGTH_SHORT).show()
            gameOnRun = 11
        }

        if(gameOnRun==10)
            Toast.makeText(this, "Game Over!!", Toast.LENGTH_LONG).show()
    }

    //When I request other to play with me
    fun buttonRequestEvent(view:android.view.View){
        var userEmail = editText.text.toString()
        myRef.child("Users").child(splitSring(userEmail)).child("Request").push().setValue(myEmail)
        playerOnline(splitSring(myEmail!!) + splitSring(userEmail))
        playerSimbol="X"
    }

    //When I accept to play request
    fun buttonAcceptEvent(view:android.view.View){
        var userEmail = editText.text.toString()
        myRef.child("Users").child(splitSring(userEmail)).child("Request").push().setValue(myEmail)
        playerOnline(splitSring(userEmail) + splitSring(myEmail!!))
        playerSimbol="O"
    }

    fun splitSring(str:String) : String{
        var split = str.split("@")
        return split[0]
    }

    fun incomingRequest(){
        myRef.child("Users").child(splitSring(myEmail.toString())).child("Request")
            .addValueEventListener(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val td = dataSnapshot.value as HashMap<String, Any>
                        if(td!=null){

                            var value:String
                            for(key in td.keys){
                                value = td[key] as String
                                editText.setText(value)
                                myRef.child("Users").child(splitSring(myEmail!!)).child("Request").setValue(true)
                                break
                            }
                        }

                    }catch (ex:Exception){}
                }
            })
    }


    var sessionID:String?=null
    var playerSimbol:String?=null

    //Genarate session for playing two playes online
    fun playerOnline(sessionID:String){

        this.sessionID = sessionID
        myRef.child("PlayerOnline").child(sessionID)
            .addValueEventListener(object:ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try{
                        playerOne.clear()
                        playerTwo.clear()
                        val td = dataSnapshot.value as HashMap<String,Any>
                        if(td!=null){
                            var value:String
                            for(key in td.keys){
                                value = td[key] as String

                                if(value!=myEmail){
                                    activePlayer = if (playerSimbol==="X") 1 else 2
                                }else{
                                    activePlayer = if (playerSimbol==="X") 2 else 1
                                }

                                autoPlayOnline(key.toInt()) //key=butnID
                            }
                        }
                    }catch (ex:Exception){}
                }
            })



    }

}
