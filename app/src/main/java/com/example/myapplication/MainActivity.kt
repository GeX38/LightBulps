package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var gridLayout: GridLayout
    private var cellSize: Int = 0
    private var borderSize: Int = 0
    private var gridSize: Int = 4
    private var score: Int = 0
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)
        scoreTextView = findViewById(R.id.scoreTextView)
        updateScore()
        val btnShuffle: Button = findViewById(R.id.btnShuffle)
        btnShuffle.setOnClickListener {
            resetGame()
        }

        setupGridLayout()
    }

    private fun setupGridLayout() {
        gridLayout.removeAllViews()
        gridLayout.columnCount = gridSize
        gridLayout.rowCount = gridSize

        cellSize = resources.getDimensionPixelSize(R.dimen.cell_size)
        borderSize = resources.getDimensionPixelSize(R.dimen.border_size)

        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val cell = View(this)
                val params = GridLayout.LayoutParams()
                params.width = cellSize
                params.height = cellSize
                params.rowSpec = GridLayout.spec(row)
                params.columnSpec = GridLayout.spec(col)
                cell.layoutParams = params

                val shape = GradientDrawable()
                shape.shape = GradientDrawable.OVAL
                shape.setColor(if (Random.nextBoolean()) Color.WHITE else Color.BLACK)
                shape.setStroke(borderSize, Color.GRAY)
                cell.background = shape

                cell.setOnClickListener {
                    onCellClicked(row, col)
                }

                gridLayout.addView(cell)
            }
        }
    }

    private fun onCellClicked(row: Int, col: Int) {
        toggleCellState(row, col)
        toggleRowAndColumn(row, col)
        checkForVictory()
        score++
        updateScore()
    }

    private fun toggleCellState(row: Int, col: Int) {
        val index = row * gridSize + col
        val cell = gridLayout.getChildAt(index) as View
        val shape = cell.background as GradientDrawable
        val currentColor = shape.color?.defaultColor ?: Color.TRANSPARENT
        val newColor = if (currentColor == Color.WHITE) Color.BLACK else Color.WHITE
        shape.setColor(newColor)
    }

    private fun toggleRowAndColumn(row: Int, col: Int) {
        for (c in 0 until gridSize) {
            toggleCellState(row, c)
            toggleCellState(c, col)
        }
    }

    private fun checkForVictory() {
        var allCellsSameColor = true
        val firstCell = gridLayout.getChildAt(0) as View

        for (i in 1 until gridLayout.childCount) {
            val currentCell = gridLayout.getChildAt(i) as View
            val currentColor = (currentCell.background as GradientDrawable).color?.defaultColor ?: Color.TRANSPARENT
            val firstColor = (firstCell.background as GradientDrawable).color?.defaultColor ?: Color.TRANSPARENT
            if (currentColor != firstColor) {
                allCellsSameColor = false
                break
            }
        }

        if (allCellsSameColor) {
            gridSize++
            setupGridLayout()
            score = 0
        }
    }

    private fun resetGame() {
        setupGridLayout()
        score = 0
        updateScore()
    }

    private fun updateScore() {
        scoreTextView.text = "Score: $score" // Обновляем отображение счета
    }

}
