package com.example.testgos.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.navigation.findNavController
import com.example.testgos.R
import com.example.testgos.data.MyDatabase
import com.example.testgos.model.Car
import com.example.testgos.model.CarState

class EditFragment : Fragment() {

    private var viewModel = EditViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateSpinner()

        val db = MyDatabase.getDatabase(requireContext())
        val id = requireArguments().getInt("carId")

        if (id > 0){
            val item = db.carDao().getCar(id)
            setItem(item)
        }

        view.apply {
            findViewById<Button>(R.id.buttonDelete).setOnClickListener {
                if (id > 0){
                    db.carDao().deleteItem(db.carDao().getCar(id))
                    findNavController().popBackStack()
                }
            }

            findViewById<Button>(R.id.buttonSave).setOnClickListener {

                val client = if (CarState.valueOf(viewModel.state) == CarState.InUse) viewModel.client else ""

                if (id > 0){
                    val newItem = Car(viewModel.name, viewModel.state, client, id)
                    db.carDao().updateItem(newItem)
                }
                else{
                    val newItem = Car(viewModel.name, viewModel.state, client)
                    db.carDao().insertItem(newItem)
                }

                findNavController().popBackStack()
            }

            findViewById<EditText>(R.id.editTextName).doOnTextChanged {text, start, before, count ->
                viewModel.name = text.toString()
            }

            findViewById<EditText>(R.id.editTextClient).doOnTextChanged {text, start, before, count ->
                viewModel.client = text.toString()
            }

            findViewById<Spinner>(R.id.spinnerCarState).onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val value = CarState.entries[position]
                    viewModel.state = value.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun updateSpinner(){
        val items = CarState.entries.map { it.name }
        val adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items)
        view?.findViewById<Spinner>(R.id.spinnerCarState)?.adapter = adapter
    }

    private fun setItem(item: Car){
        view?.apply {
            findViewById<TextView>(R.id.editTextName).text = item.carName
            findViewById<TextView>(R.id.editTextClient).text = item.client
            findViewById<Spinner>(R.id.spinnerCarState).setSelection(CarState.valueOf(item.carState).ordinal)
        }

        viewModel.client = item.client
        viewModel.name = item.carName
        viewModel.state = item.carState
    }
}