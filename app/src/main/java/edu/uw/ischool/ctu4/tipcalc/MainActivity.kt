package edu.uw.ischool.ctu4.tipcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import java.lang.NumberFormatException
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    lateinit var numberInput: EditText
    lateinit var button: Button
    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // All elements
        numberInput = findViewById<EditText>(R.id.amount)
        button = findViewById<Button>(R.id.tipButton)

        // Creating spinner
        spinner = findViewById(R.id.tipSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.tipSpinner,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        numberInput.addTextChangedListener(inputWatcher)
        button.setOnClickListener({
            // Formate spinner item into tip value
            val tip = spinner.getSelectedItem().toString()
            val cleanTip = tip.replace("%", "").toDouble() / 100

            // Calculating and displaying tip
            val amount: Double = (numberInput.getText().toString().replace("$", "")).toDouble()
            val plusTips: Double = amount + (amount * cleanTip)
            Toast.makeText(this, "$${String.format("%.2f", plusTips)}", Toast.LENGTH_SHORT).show()
        })
    }

    // A listener for when input changes
    private val inputWatcher = object : TextWatcher {
        // Changes input to $ format
        override fun afterTextChanged(s: Editable?) {
            val stringText = s.toString()
            if (stringText != "") {
                numberInput.removeTextChangedListener(this)

                val cleanText = stringText.replace("[$,.]".toRegex(), "")

                val parsed: Double = cleanText.toDouble()
                val format: String = NumberFormat.getCurrencyInstance().format(parsed / 100)
                numberInput.setText(format)
                numberInput.setSelection(format.length)
                numberInput.addTextChangedListener(this)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        // Checks if input exists before making button active
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!TextUtils.isEmpty(numberInput.getText().toString())) {
                button.setEnabled(true)
            } else {
                button.setEnabled(false)
            }
        }
    }
}