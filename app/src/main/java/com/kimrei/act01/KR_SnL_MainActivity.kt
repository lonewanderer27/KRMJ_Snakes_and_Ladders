package com.kimrei.act01

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView

class KR_SnL_MainActivity : AppCompatActivity() {
    private val maxValue = 60
    private val numCols = 5
    private var snakesAndLaddersNumPattern = IntArray(maxValue)
    private var diceImg: ImageView? = null
    private var rollBtn: Button? = null
    private var resetBtn: Button? = null
    private var coorentPoosition: Int = 0
    private var BGMusic: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // generate an array of numbers from 60 to 1
        // in a snake and ladders pattern
        snakesAndLaddersNumPattern = combineArrays(generateSnLPatternArrays(maxValue, numCols))

        // assign the grid view it's text view adapter
        val gridView = findViewById<GridView>(R.id.table)
        val adapter = TextViewAdapter(this)
        gridView.adapter = adapter

        // assign the dice view
        diceImg = findViewById(R.id.dice)

        // assign the roll dice button
        rollBtn = findViewById(R.id.rollBtn)

        // assign the reset button
        resetBtn = findViewById(R.id.resetBtn)

        // set the on click listener for the roll dice btn
        rollBtn!!.setOnClickListener {
            rollDice()
        }

        // set the on click listener for the reset btn
        resetBtn!!.setOnClickListener {
            reset()
        }

        // create a background music player
        BGMusic = MediaPlayer.create(this, R.raw.bgmusic);

        // set the bg music to loop
        BGMusic!!.setOnCompletionListener {
            MediaPlayer.OnCompletionListener { mp ->
                // When the media completes, restart it from the beginning
                mp.seekTo(0)
                mp.start()
            }
        }

        // start the bg music
        BGMusic!!.start()
    }

    fun reset() {
        // reset position
        coorentPoosition = 0;

        // reset boxes
        updateBoxes()

        // reset dice
        updateDice(6);
    }

    fun updateBoxes() {
        for (i in 0 until maxValue) {
            // progress = 60
            // find the box - id = 60
            val box = findViewById<TextView>(snakesAndLaddersNumPattern[i])

            // change the background resource to selected
            // if the box's id is less than or equal to the progress
            if (box.id <= coorentPoosition) {
                box.setBackgroundResource(R.drawable.selected)
            } else {
                box.setBackgroundResource(R.drawable.unselected)
            }
        }
    }

    fun updateDice(value: Int) {
        when (value) {
            1 -> diceImg!!.setImageResource(R.drawable.one)
            2 -> diceImg!!.setImageResource(R.drawable.two)
            3 -> diceImg!!.setImageResource(R.drawable.three)
            4 -> diceImg!!.setImageResource(R.drawable.four)
            5 -> diceImg!!.setImageResource(R.drawable.five)
            6 -> diceImg!!.setImageResource(R.drawable.six)
        }
    }

    fun endMusic() {
        val endMusic = MediaPlayer.create(this, R.raw.finish)

        // start the end music
        endMusic.start()
    }

    fun transitionMusic() {
        val diceRollMusic = MediaPlayer.create(this, R.raw.transition)

        // start the dice roll music
        diceRollMusic.start()
    }

    fun rollDice () {
        // generate a random number from 1 to 5
        val diceValue = (1..6).random()
        Log.i("Dice Value", diceValue.toString())

        // update the dice view
        updateDice(diceValue)

        // update the progress
        coorentPoosition += diceValue

        // if coorentPosition is higher than the max value
        // subtract the excess from the coorentPosition
        if (coorentPoosition > maxValue) {
            val excess = coorentPoosition - maxValue
            coorentPoosition -= excess
        }
        Log.i("Coorent Position", coorentPoosition.toString())

        // update the boxes
        updateBoxes()

        // if the coorentPoosition equals to 60, then play the finish music
        if (coorentPoosition == maxValue) {
            endMusic()
        } else {
            // else play the music for the dice roll
            transitionMusic()
        }
    }

    fun generateSnLPatternArrays(maxValue: Int, numCols: Int): Array<IntArray> {
        val numRows = maxValue / numCols //12
        val arrays = Array(numRows) { IntArray(numCols) }
        var startValue = maxValue

        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                arrays[row][col] = startValue
                startValue--
            }
            if (row % 2 != 0) {
                arrays[row].reverse()
            }
        }

        return arrays
    }

    fun combineArrays(arrays: Array<IntArray>): IntArray {
        return arrays.flatMap { it.asIterable() }.toIntArray()
    }

    // [ 60, 59, 58, 57, 56 ]
    // class is the blueprint
    // object is made from the class
    inner class TextViewAdapter(private val context: Context): BaseAdapter() {
        override fun getCount(): Int {
            return maxValue // 60
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        // 60 -> 0
        // TextView -> View
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            // 60
            val textView = TextView(context)
            val actualPosition = snakesAndLaddersNumPattern[position] // 60

            // set the text to START at 1
            // set the text to END at 60
            // otherwise set the text to the reversed position
            if (actualPosition == 1) {
                textView.text = "START"
            } else if (actualPosition == maxValue) {
                textView.text = "END" //
            } else {
                textView.text = actualPosition.toString()
            }

            // set the font size to 20
            textView.textSize = 20f

            // set the text alignment to center
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView.gravity = View.TEXT_ALIGNMENT_CENTER

            // set the background resource to unselected with border on the text view
            textView.setBackgroundResource(R.drawable.unselected)

            // set the padding of the text view
            textView.setPadding(10, 10, 10, 10)

            // set the height of the text view
            textView.height = 100

            // set the id of the text view
            textView.id = actualPosition

            return textView
        }
    }
}