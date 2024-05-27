package com.example.testgos.report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.testgos.R
import com.example.testgos.data.MyDatabase
import com.example.testgos.model.Car

class ReportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = MyDatabase.getDatabase(requireContext())

        val adapterIsNew = ArrayAdapter<List<Car>>(requireContext(), android.R.layout.simple_list_item_1)
        val adapterInUse = ArrayAdapter<List<Car>>(requireContext(), android.R.layout.simple_list_item_1)
        val adapterIsDeleted= ArrayAdapter<List<Car>>(requireContext(), android.R.layout.simple_list_item_1)

        adapterIsNew.addAll(db.carDao().loadIsNew())
        adapterInUse.addAll(db.carDao().loadInUse())
        adapterIsDeleted.addAll(db.carDao().loadIsDeleted())

        view.findViewById<ListView>(R.id.list_report_new).adapter = adapterIsNew
        view.findViewById<ListView>(R.id.list_report_in_use).adapter = adapterInUse
        view.findViewById<ListView>(R.id.list_report_deleted).adapter = adapterIsDeleted
    }
}