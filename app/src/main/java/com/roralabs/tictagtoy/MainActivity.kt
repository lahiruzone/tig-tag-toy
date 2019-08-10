package com.roralabs.tictagtoy

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        gamePlay(cellID, buSelected)
    }

    var playerOne = ArrayList<Int>()
    var playerTwo = ArrayList<Int>()
    var activePlayer = 1
    var gameOnRun = 1 //1-10 Run the game, 10=< stop running the game

    fun gamePlay(cellID:Int, buSelected:Button){

        buSelected.isEnabled = false


        if(gameOnRun<10) {
            if (activePlayer == 1) {
                buSelected.text = "x"
                buSelected.setBackgroundResource(R.color.colorblue)
                playerOne.add(cellID)
                activePlayer = 2
                autoPlay()
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

}
