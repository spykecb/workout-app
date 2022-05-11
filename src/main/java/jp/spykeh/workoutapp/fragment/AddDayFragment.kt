package jp.spykeh.workoutapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.RoutineEditorActivity
import jp.spykeh.workoutapp.adapter.WeekDayOptionsAdapter
import jp.spykeh.workoutapp.data.ExerciseDay
import kotlinx.android.synthetic.main.fragment_add_day.view.*

class AddDayFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.fragment_add_day, container, false)
        val weekDays = mutableListOf<Pair<String, Boolean>>()
        weekDays.add(Pair("Day 1", false))
        weekDays.add(Pair("Day 2", false))
        weekDays.add(Pair("Day 3", false))
        weekDays.add(Pair("Day 4", false))
        weekDays.add(Pair("Day 5", false))
        weekDays.add(Pair("Day 6", false))
        weekDays.add(Pair("Day 7", false))

        val weekDayAdapter = WeekDayOptionsAdapter(weekDays)
        rootView.rvWeekDays.adapter = weekDayAdapter
        rootView.rvWeekDays.layoutManager = LinearLayoutManager(context)


        rootView.btnAddDayCancel.setOnClickListener{
            dismiss()
        }

        rootView.btnAddDayOK.setOnClickListener{
            val exercisesAdapter = (activity as RoutineEditorActivity).getExerciseDayAdapter()
            for(it in weekDayAdapter.getItems()){
                if(it.second){
                    val day : Int = it.first.substring(4).toInt()
                    exercisesAdapter.addExerciseDay(ExerciseDay(0, day, mutableListOf()))
                }
            }


            dismiss()
        }
        return rootView
    }

}