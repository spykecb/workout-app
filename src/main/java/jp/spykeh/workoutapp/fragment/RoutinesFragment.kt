package jp.spykeh.workoutapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.RoutineEditorActivity
import jp.spykeh.workoutapp.adapter.RoutineAdapter
import jp.spykeh.workoutapp.db.SQLiteDBHelper
import kotlinx.android.synthetic.main.fragment_routines.*

class RoutinesFragment : Fragment() {
    private lateinit var rvAdapter: RoutineAdapter
    private lateinit var db: SQLiteDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SQLiteDBHelper(requireContext())
        var routines = db.getAllRoutines()
        rvAdapter = RoutineAdapter(routines)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvRoutines.adapter = rvAdapter
        rvRoutines.layoutManager = LinearLayoutManager(requireContext())

        btnNewRoutine.setOnClickListener{
            var intent = Intent(context, RoutineEditorActivity::class.java)
            startActivity(intent)
        }

    }

}