package jp.spykeh.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import jp.spykeh.workoutapp.adapter.EditExerciseDayAdapter
import jp.spykeh.workoutapp.data.Routine
import jp.spykeh.workoutapp.db.SQLiteDBHelper
import jp.spykeh.workoutapp.fragment.AddDayFragment
import kotlinx.android.synthetic.main.activity_routine_editor.*

class RoutineEditorActivity : AppCompatActivity() {
    private lateinit var exerciseDayAdapter: EditExerciseDayAdapter
    private lateinit var db: SQLiteDBHelper
    private lateinit var routine: Routine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_editor)
        db = SQLiteDBHelper(this)
        val id = intent?.getLongExtra("routine_id", 0)
        val duplicate = intent?.getBooleanExtra("duplicate", false)
        if(id != null && id > 0)
            routine = db.getRoutine(id)!!
            if (duplicate == true){
                routine.id = 0
            }
        else
            routine = Routine(0, "My Workout", 8, mutableListOf())

        etWeeks.setText(routine.weeks.toString())

        exerciseDayAdapter = EditExerciseDayAdapter(routine.exerciseDays)

        rvDays.adapter = exerciseDayAdapter
        rvDays.layoutManager = LinearLayoutManager(this)

        btnAddDay.setOnClickListener{
            var dialog = AddDayFragment()

            dialog.show(supportFragmentManager, "addDay")
        }

        btnSave.setOnClickListener{
            val excDays = (rvDays.adapter as EditExerciseDayAdapter).getExerciseDays()
            if(routine.id == 0L){
                routine = Routine(0,
                    etRoutineTitle.text.toString(),
                    etWeeks.text.toString().toInt(),
                    excDays)
                val _id = db.insertRoutine(routine)
                routine.id = _id
            }else{
                db.updateRoutine(routine)
            }

        }
    }

    fun getExerciseDayAdapter(): EditExerciseDayAdapter{
        return exerciseDayAdapter
    }
}