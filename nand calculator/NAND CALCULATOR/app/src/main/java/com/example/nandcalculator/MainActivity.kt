package com.example.nandcalculator

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val selectedIngredients = mutableListOf<String>()
    private val selectedButtons = mutableListOf<Button>()
    private val selectedColor = Color.parseColor("#44c74c")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectedColor = Color.parseColor("#44c74c")
        val defaultColor = Color.parseColor("#FFFFFF")

        val buttons = mapOf(
            R.id.milkbutton to "Milk",
            R.id.chocobutton to "Chocolate",
            R.id.hotwaterbutton to "Hot Water",
            R.id.foambutton to "Foam",
            R.id.coffeebutton to "Coffee",
            R.id.vanillabutton to "Vanilla"
        )

        for ((id, ingredient) in buttons) {
            val btn = findViewById<Button>(id)
            btn.setOnClickListener {
                if (selectedIngredients.size < 2 && !selectedIngredients.contains(ingredient)) {
                    selectedIngredients.add(ingredient)
                    selectedButtons.add(btn)
                    btn.setBackgroundColor(selectedColor)
                } else {
                    Toast.makeText(this, "You can only select two different ingredients!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.calculatebutton).setOnClickListener {
            showPopup(getDrinkName())

            // Reset selections and button colors
            selectedIngredients.clear()
            for (btn in selectedButtons) {
                btn.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            selectedButtons.clear()
        }

        findViewById<Button>(R.id.nextpage).setOnClickListener {
            startActivity(Intent(this, secondpage::class.java))

            selectedIngredients.clear()
            for (btn in selectedButtons) {
                btn.setBackgroundColor(defaultColor)
            }
            selectedButtons.clear()

        }
    }

    private fun getDrinkName(): String {
        val combo = selectedIngredients.sorted().joinToString(" + ")

        val drinkMap = mapOf(
            "Chocolate + Coffee"       to "Mocha",
            "Chocolate + Foam"        to "Foamy Hot Chocolate",
            "Chocolate + Hot Water"   to "Standard Hot Chocolate",
            "Chocolate + Milk"        to "Hot Chocolate with Milk",
            "Chocolate + Vanilla"     to "Vanilla Hot Chocolate",
            "Coffee + Foam"           to "Espresso Macchiato",
            "Coffee + Hot Water"      to "Americano",
            "Coffee + Milk"           to "Latte",
            "Coffee + Vanilla"        to "Vanilla Coffee",
            "Foam + Hot Water"        to "Foamed Water",
            "Foam + Milk"             to "Steamed Milk with Foam",
            "Foam + Vanilla"          to "Vanilla Steamer Foam",
            "Hot Water + Milk"        to "Diluted Milk",
            "Hot Water + Vanilla"     to "Vanilla Flavored Water",
            "Milk + Vanilla"          to "Vanilla Milk"
        )
        return drinkMap[combo] ?: "Woops! Machine broke..."
    }

    private fun showPopup(drinkName: String) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.activity_popup, null)

        val instructWindow = PopupWindow(
            popupView,
            1000,
            1100,
            true
        )
        instructWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        instructWindow.showAtLocation(popupView, Gravity.BOTTOM, 10, 800)

        val answerText: TextView = popupView.findViewById(R.id.answerview)
        answerText.text = drinkName

        val closeButton: Button = popupView.findViewById(R.id.closebutton)
        closeButton.setOnClickListener {
            instructWindow.dismiss()
        }
    }
}








