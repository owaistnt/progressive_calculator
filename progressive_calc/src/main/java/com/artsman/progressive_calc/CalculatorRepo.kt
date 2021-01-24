package com.artsman.progressive_calc

class CalculatorRepo: ICalculatorRepository {
    private var data=0
    override fun setValue(i: Int) {
         data=i
    }

    override fun getValue(): Int {
        return data
    }
}