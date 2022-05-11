package jp.spykeh.workoutapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.adapter.ExerciseHistoryAdapter
import jp.spykeh.workoutapp.db.SQLiteDBHelper
import kotlinx.android.synthetic.main.fragment_exercise_hist.view.*

class ExerciseHistoryFragment : DialogFragment() {
    private lateinit var db : SQLiteDBHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView : View = inflater.inflate(R.layout.fragment_exercise_hist, container, false)
        val exerciseId: Long? = arguments?.getLong("exerciseId", -1)
        val exerciseTitle: String? = arguments?.getString("exerciseTitle", "Invalid")
        db = SQLiteDBHelper(requireContext())
        val exercises = db.getExerciseHistory(exerciseId!!)
        rootView.apply {
            tvExerciseName.setText(exerciseTitle)
            excWorkoutsHistory.adapter = ExerciseHistoryAdapter(exercises)
            excWorkoutsHistory.layoutManager = LinearLayoutManager(requireContext())
        }
        return rootView
    }
}