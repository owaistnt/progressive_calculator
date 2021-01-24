package com.artsman.progressive_calc

import androidx.lifecycle.Observer
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

    @Test
    fun `given multiplication action it must clear the display and on action equals must calculate result`(){
        Mockito.`when`(repo.getValue()).thenReturn(100)
        viewModel.subscribe().observeForever(observer)
        viewModel.setAction(CalculatorViewModel.Actions.IntentOf(Operator.product))
        Mockito.verify(observer, Mockito.atLeast(1)).onChanged(CalculatorViewModel.State.Display("0"))
        Mockito.verify(repo).cache(Operator.product)

        Mockito.`when`(repo.getValue()).thenReturn(0)
        viewModel.setAction(CalculatorViewModel.Actions.Add(5))
        Mockito.verify(observer, Mockito.atLeast(1)).onChanged(CalculatorViewModel.State.Display("5"))

    }

    @Test
    fun `given operation is multiplication and commit action is perform then it must return values`(){
        Mockito.`when`(repo.getValue()).thenReturn(5)
        Mockito.`when`(repo.getCache()).thenReturn(100)
        Mockito.`when`(repo.getOperator()).thenReturn(Operator.product)
        viewModel.subscribe().observeForever(observer)
        viewModel.setAction(CalculatorViewModel.Actions.Commit)
        Mockito.verify(repo).getValue()
        Mockito.verify(repo).getOperator()
        Mockito.verify(repo).getCache()

        Mockito.verify(observer).onChanged(CalculatorViewModel.State.Display("500"))
    }
}