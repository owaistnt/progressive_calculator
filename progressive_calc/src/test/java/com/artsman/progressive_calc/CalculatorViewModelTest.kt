package com.artsman.progressive_calc

import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.artsman.progressive_calc.CalculatorViewModel.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito

import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class CalculatorViewModelTest{


    @Mock
    lateinit var observer: Observer<State>

    @Mock
    lateinit var repo: ICalculatorRepository

    lateinit var viewModel: CalculatorViewModel

    private val coroutineDispatcher = TestCoroutineDispatcher()


    @BeforeEach
    fun setup(){
        MockitoAnnotations.initMocks(this)
        viewModel=CalculatorViewModel(repo)
        Dispatchers.setMain(coroutineDispatcher)
    }

    @Test
    fun `given reset it must return Display (0)`()=coroutineDispatcher.runBlockingTest{

        viewModel.subscribe().asLiveData().observeForever(observer)
        viewModel.setAction(CalculatorViewModel.Actions.Reset)
        Mockito.verify(observer).onChanged(State.Display("0"))
        Mockito.verify(repo).setValue(0)
    }

    @Test
    fun `given action addition it must add to value from repository`(){
        Mockito.`when`(repo.getValue()).thenReturn(100)
        viewModel.subscribe().asLiveData().observeForever(observer)
        viewModel.setAction(CalculatorViewModel.Actions.Add(10))
        Mockito.verify(repo).getValue()
        Mockito.verify(observer).onChanged(State.Display("110"))
        Mockito.verify(repo).setValue(110)
    }

    @Test
    fun `given multiplication action it must clear the display and on action equals must calculate result`()= coroutineDispatcher.runBlockingTest{
        Mockito.`when`(repo.getValue()).thenReturn(100)
        viewModel.subscribe().asLiveData().observeForever(observer)
        viewModel.setAction(CalculatorViewModel.Actions.IntentOf(Operator.product))
        Mockito.verify(observer, Mockito.atLeast(1)).onChanged(State.Display("0"))
        Mockito.verify(observer).onChanged(State.HideOperators)
        Mockito.verify(repo).cache(Operator.product)

        Mockito.`when`(repo.getValue()).thenReturn(0)
        viewModel.setAction(CalculatorViewModel.Actions.Add(5))
        Mockito.verify(observer, Mockito.atLeast(1)).onChanged(State.Display("5"))

    }

    @Test
    fun `given operation is multiplication and commit action is perform then it must return values`() = coroutineDispatcher.runBlockingTest{
        Mockito.`when`(repo.getValue()).thenReturn(5)
        Mockito.`when`(repo.getCache()).thenReturn(100)
        Mockito.`when`(repo.getOperator()).thenReturn(Operator.product)
        viewModel.subscribe().asLiveData().observeForever(observer)
        viewModel.setAction(CalculatorViewModel.Actions.Commit)
        Mockito.verify(repo).getValue()
        Mockito.verify(repo).getOperator()
        Mockito.verify(repo).getCache()

        Mockito.verify(observer).onChanged(State.Display("500"))
        Mockito.verify(observer).onChanged(State.ShowOperators)
    }
}