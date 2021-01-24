package com.artsman.progressive_calc

interface ICalculatorRepository {
    fun setValue(i: Int)
    fun getValue(): Int
}
