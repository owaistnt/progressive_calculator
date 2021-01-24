package com.artsman.progressive_calc

import android.database.DatabaseUtils
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.artsman.progressive_calc.databinding.CalculatorFragmentBinding

public class CalculatorFragment : Fragment() {

    companion object {
        fun newInstance() = CalculatorFragment()
    }

    private lateinit var viewModel: CalculatorViewModel
    private lateinit var binding: CalculatorFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getContext()?.getTheme()?.applyStyle(R.style.Theme_Library, true);
        binding=CalculatorFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory= CalculatorViewModelFactory(CalculatorRepo())
        viewModel = ViewModelProvider(this, viewModelFactory).get(CalculatorViewModel::class.java)
        viewModel.subscribe().observe(viewLifecycleOwner, {
            when(it){
                is CalculatorViewModel.State.Display -> {
                    binding.textView.text=it.text
                }
            }
        })

        binding.btn1.bindWithNumber(1)
        binding.btnNeg1.bindWithNumber(-1)
        binding.btn5.bindWithNumber(5)
        binding.btn10.bindWithNumber(10)
        binding.btn100.bindWithNumber(100)
        binding.btn1k.bindWithNumber(1000)
        binding.btnNeg25.bindWithNumber(-25)
        binding.btnNeg50.bindWithNumber(-50)

        binding.btnReset.setOnClickListener {
            viewModel.setAction(CalculatorViewModel.Actions.Reset)
        }
    }

    fun Button.bindWithNumber(num: Int){
        this.setOnClickListener {
            viewModel.setAction(CalculatorViewModel.Actions.Add(num))
        }
    }
}