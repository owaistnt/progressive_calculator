package com.artsman.progressive_calc

class CalculatorRepo: ICalculatorRepository {
    private var data=0
    private var dataCache: Int?=null
    private var operator: Operator?=null
    override fun setValue(i: Int) {
         data=i
    }

    override fun getValue(): Int {
        return data
    }

    override fun cache(operator: Operator) {
        dataCache=data
        this.operator=operator
        data=0
    }

    override fun getOperator(): Operator? =operator

    override fun getCache(): Int?= dataCache
}