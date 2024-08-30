package com.example.CALCULATOR_VILLAVILLA

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private lateinit var displayResult: TextView
    private val input = StringBuilder()
    private var lastCalculation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        display = findViewById(R.id.display)
        displayResult = findViewById(R.id.displayResult)

        val btnDot: Button = findViewById(R.id.buttonDot)
        val btn0: Button = findViewById(R.id.button0)
        val btn1: Button = findViewById(R.id.button1)
        val btn2: Button = findViewById(R.id.button2)
        val btn3: Button = findViewById(R.id.button3)
        val btn4: Button = findViewById(R.id.button4)
        val btn5: Button = findViewById(R.id.button5)
        val btn6: Button = findViewById(R.id.button6)
        val btn7: Button = findViewById(R.id.button7)
        val btn8: Button = findViewById(R.id.button8)
        val btn9: Button = findViewById(R.id.button9)

        val btnAc: Button = findViewById(R.id.buttonAc)
        val btnDel: Button = findViewById(R.id.buttonDel)
        val btnPercent: Button = findViewById(R.id.buttonPercent)

        val btnAdd: Button = findViewById(R.id.buttonAdd)
        val btnSub: Button = findViewById(R.id.buttonSub)
        val btnMul: Button = findViewById(R.id.buttonMul)
        val btnDiv: Button = findViewById(R.id.buttonDiv)

        val btnEqual: Button = findViewById(R.id.buttonEqual)
        // button listener for numbers and period this is for displaying the typed numbers by user
        btnDot.setOnClickListener { appendToDisplay(".") }
        btn0.setOnClickListener { appendToDisplay("0") }
        btn1.setOnClickListener { appendToDisplay("1") }
        btn2.setOnClickListener { appendToDisplay("2") }
        btn3.setOnClickListener { appendToDisplay("3") }
        btn4.setOnClickListener { appendToDisplay("4") }
        btn5.setOnClickListener { appendToDisplay("5") }
        btn6.setOnClickListener { appendToDisplay("6") }
        btn7.setOnClickListener { appendToDisplay("7") }
        btn8.setOnClickListener { appendToDisplay("8") }
        btn9.setOnClickListener { appendToDisplay("9") }

        // button listener for Operators
        btnAdd.setOnClickListener { performOperation("+") }
        btnSub.setOnClickListener { performOperation("-") }
        btnMul.setOnClickListener { performOperation("*") }
        btnDiv.setOnClickListener { performOperation("/") }
        btnPercent.setOnClickListener { performOperation("%") }
        // result
        btnEqual.setOnClickListener { calculateResult() }
        // will clear all input and result
        btnAc.setOnClickListener {
            clearAll()
        }
        // last input delete
        btnDel.setOnClickListener {
            backspace()
        }
    }

    private fun clearAll() {
        input.clear()
        display.text = ""
        displayResult.text = ""
        lastCalculation = false
    }


    private fun backspace() {
        if (input.isNotEmpty()) {
            input.deleteCharAt(input.length - 1)
            display.text = input.toString()

            if (input.isEmpty()) {
                displayResult.text = ""
            }
        }
    }

    private fun appendToDisplay(value: String) {
        if (lastCalculation) {
            clearAll() // this function will clear input if the last calculation was completed
        }
        input.append(value)
        display.text = input.toString()
        lastCalculation = false
    }

    private fun performOperation(operator: String) {
        if (input.isNotEmpty() && input.last().isDigit()) {
            input.append(" $operator ")
            display.text = input.toString()
            lastCalculation = false
        }
    }

    private fun calculateResult() {
        val tokens = input.toString().trim().split(" ")

        if (tokens.size < 3) {
            return
        }

        var intermediateTokens = evaluateOperations(tokens, listOf("*", "/")) //for PEMDAS

        val finalResult = evaluateOperations(intermediateTokens, listOf("+", "-"))
    // check if the result is valid and display it
        if (finalResult.size == 1) {
            displayResult.text = finalResult[0] // final result
        } else {
            displayResult.text = "Invalid"
        }
        lastCalculation = true
        input.clear()
    }
    // this function performs a PEMDAS method in calculator. meaning, the 2*2+3 will be 7 instead of 5
    // evaluate operations based on the specified operators (PEMDAS)
    private fun evaluateOperations(tokens: List<String>, operators: List<String>): List<String> {
        val output = mutableListOf<String>() // this store results
        var index = 0

        while (index < tokens.size) {
            val token = tokens[index]

            if (operators.contains(token)) {
                // perform the operation on the last number and the next number
                val left = output.removeAt(output.size - 1)
                val right = tokens[index + 1]

                val result = when (token) {
                    "*" -> left.toDouble() * right.toDouble()
                    "/" -> {
                        if (right.toDouble() == 0.0) {
                            displayResult.text = "Error"
                            return listOf("0")
                        } else {
                            left.toDouble() / right.toDouble()
                        }
                    }
                    "+" -> left.toDouble() + right.toDouble()
                    "-" -> left.toDouble() - right.toDouble()
                    else -> null
                }
                output.add(
                    when (token) {
                        "*", "+", "-" -> result!!.toInt().toString()
                        "/" -> result.toString()
                        else -> result!!.toString()
                    }
                )
                index += 2
            } else {
                output.add(token)
                index++
            }
        }
        return output
    }
}
