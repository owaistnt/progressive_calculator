package com.artsman.progressive_calc

import androidx.lifecycle.Observer
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito

import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
class CalculatorViewModelTest{


    @Mock
    lateinit var observer: Observer<CalculatorViewModel.State>

    @Mock
    lateinit var repo: ICalculatorRepository

    lateinit var viewModel: CalculatorViewModel
    @BeforeEach
    fun setup(){
        MockitoAnnotations.initMocks(this)
        viewModel=CalculatorViewModel(repo)
    }

    @Test
    fun `given reset it must return Display (0)`(){

        viewModel.subscribe().observeForever(observer)
        viewModel.setAction(CalculatorViewModel.Actions.Reset)
        Mockito.verify(observer).onChanged(CalculatorViewModel.State.Display("0"))
        Mockito.verify(repo).setValue(0)
    }

    @Test
    fun `given action addition it must add to value from repository`(){
        Mockito.`when`(repo.getValue()).thenReturn(100)
        viewModel.subscribe().observeForever(observer)
        viewModel.setAction(CalculatorViewModel.Actions.Add(10))
        Mockito.verify(repo).getValue()
        Mockito.verify(observer).onChanged(CalculatorViewModel.State.Display("110"))
        Mockito.verify(repo).setValue(110)
    }
}