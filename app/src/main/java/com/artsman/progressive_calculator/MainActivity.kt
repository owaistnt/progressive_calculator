package com.artsman.progressive_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.artsman.progressive_calc.CalculatorFragment
import com.artsman.progressive_calculator.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.placeholder, CalculatorFragment.newInstance())
                    .commitNow()
        }
    }
}