package com.artsman.progressive_calc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel(private val repo: ICalculatorRepository) : ViewModel() {

    val mState= MutableLiveData<State>()
    fun subscribe(): MutableLiveData<State> {
        return mState
    }

    fun setAction(action: Actions) {
        when(action){
            Actions.Reset -> {
                mState.postValue(State.Display("0"))
                repo.setValue(0)
            }
            is Actions.Add ->{
               val result= adder(action.add)
               repo.setValue(result)
               mState.postValue(State.Display(result.toString()))
            }
        }
    }

    private fun adder(add: Int): Int = repo.getValue()+add


    sealed class State{
        data class Display(val text: String): State()
    }
    sealed class Actions{
        object Reset: Actions()
        data class Add(val add: Int) : Actions()
    }
}