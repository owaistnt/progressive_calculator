package com.artsman.progressive_calc

interface ICalculatorRepository {
    fun setValue(i: Int)
    fun getValue(): Int
    fun cache(operator: Operator)
    fun getOperator(): Operator?
    fun getCache(): Int?
}
