package com.example.testgos.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.testgos.R
import com.example.testgos.ReportActivity
import com.example.testgos.data.MyDatabase
import com.example.testgos.model.Car
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainFragment : Fragment() {

    private lateinit var arrayAdapter: ArrayAdapter<Car>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = MyDatabase.getDatabase(requireContext())
        val data = db.carDao().getAllCars()

        arrayAdapter = ArrayAdapter<Car>(requireContext(), android.R.layout.simple_list_item_1)

        view.apply {
            findViewById<Button>(R.id.buttonAdd).setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_editFragment, Bundle().apply {
                    putInt("carId", -1)
                })
            }

            findViewById<Button>(R.id.buttonReport).setOnClickListener {
                val neededActivity = Intent(activity, ReportActivity::class.java)
                startActivity(neededActivity)
            }

            findViewById<ListView>(R.id.listCarsMain).apply {
                adapter = arrayAdapter
                setOnItemClickListener { adapterView, view, position, index ->
                    val item = data[position]
                    findNavController().navigate(R.id.action_mainFragment_to_editFragment, Bundle().apply {
                        putInt("carId", item.id!!)
                    })
                }
            }

            findViewById<Button>(R.id.buttonExport).setOnClickListener {
                writeJson(data)
                Toast.makeText(requireContext(), "Pizda", Toast.LENGTH_LONG).show()
            }

            findViewById<Button>(R.id.buttonImport).setOnClickListener {
                arrayAdapter.clear()
                readJson(db)
                arrayAdapter.addAll(db.carDao().getAllCars())
                Toast.makeText(requireContext(), "zalupa", Toast.LENGTH_LONG).show()
            }
        }

        arrayAdapter.addAll(data)
    }

    private fun writeJson(items: List<Car>){
        val str = Json.encodeToString(value = items)

        requireContext().openFileOutput("data.json", Context.MODE_PRIVATE).use {
            it.write(str.toByteArray())
        }
    }

    private fun readJson(db: MyDatabase){
        val str = requireContext().openFileInput("data.json").bufferedReader().use {
            it.readText()
        }

        db.carDao().deleteAllItems()
        val norm = Json.decodeFromString<List<Car>>(str)
        db.carDao().insertAllItems(norm)
    }
}