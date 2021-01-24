package com.artsman.progressive_calc

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@ExperimentalCoroutinesApi
class CalculatorViewModel(private val repo: ICalculatorRepository) : ViewModel() {

    //val mState = MutableLiveData<State>()

    val mChannel=ConflatedBroadcastChannel<State>()

    fun subscribe(): Flow<State>{
        return mChannel.asFlow()
    }/*
    fun subscribe(): MutableLiveData<State> {
        return mState
    }*/

    fun setAction(action: Actions) {
        when (action) {
            Actions.Reset -> {
                postResetDisplay()
                repo.setValue(0)
            }
            is Actions.Add -> {
                val result = adder(action.add)
                repo.setValue(result)
                mChannel.offer(State.Display(result.toString()))
            }
            is Actions.IntentOf -> {
                mChannel.offer(State.HideOperators)
                repo.cache(operator = action.operator)
                postResetDisplay()
            }
            Actions.Commit -> {
                mChannel.offer(State.ShowOperators)
                val result=calculate(repo.getCache(), repo.getOperator(), repo.getValue())
                mChannel.offer(State.Display(result.toString()))
            }
        }
}

    private fun calculate(cache: Int?, operator: Operator?, value: Int): Int {
        return cache?.let {
            operator?.let {
                when(operator){
                   Operator.product->{
                       return cache * value
                   }
                   Operator.division->{
                       if(value<=0){
                           return 0
                       }
                       return cache / value
                   }
                }
            }?: value
        }?: value
    }

    private fun postResetDisplay() {
        mChannel.offer(State.Display("0"))
    }

    private fun adder(add: Int): Int = repo.getValue() + add


sealed class State {
    object HideOperators : State()
    object ShowOperators : State()

    data class Display(val text: String) : State()
}

sealed class Actions {
    object Reset : Actions()
    object Commit : Actions()

    data class Add(val add: Int) : Actions()
    class IntentOf(val operator: Operator) : Actions()
}
}