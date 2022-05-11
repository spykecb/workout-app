package jp.spykeh.workoutapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import jp.spykeh.workoutapp.adapter.WeekAdapter
import jp.spykeh.workoutapp.data.ExerciseDay
import jp.spykeh.workoutapp.data.Routine
import jp.spykeh.workoutapp.data.Workout
import jp.spykeh.workoutapp.db.SQLiteDBHelper
import kotlinx.android.synthetic.main.activity_workout_week_view.*

class WorkoutWeekViewActivity : AppCompatActivity() {
    private lateinit var db: SQLiteDBHelper
    private lateinit var routine: Routine
    private lateinit var weekAdapter: WeekAdapter
    private lateinit var workouts: MutableList<Workout>

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val newWorkout = data?.getSerializableExtra("workout") as Workout
            val weekPos = updateWorkoutList(newWorkout)
            weekAdapter.notifyItemChanged(weekPos)
            Toast.makeText(this, "Workout saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_week_view)

        val id = intent?.getLongExtra("routine_id", 0)
        db = SQLiteDBHelper(this)
        routine = db.getRoutine(id!!)!!
        workouts = db.getWorkoutsOfRoutine(id)

        val weeks : MutableList<Pair<Int, MutableList<ExerciseDay>>> = mutableListOf()
        repeat(routine.weeks){
            weeks.add(Pair(it + 1, routine.exerciseDays))
        }

        weekAdapter = WeekAdapter(weeks)
        rvWeeks.adapter = weekAdapter
        rvWeeks.layoutManager = LinearLayoutManager(this)

    }

    fun getRoutine() : Routine{
        return routine
    }

    fun findWorkoutOfDay(weekNo: Int, dayNo: Int) : Workout?{
        for(workout in workouts){
            if(workout.weekNo == weekNo && workout.dayNo == dayNo){
                return workout
            }
        }
        return null
    }

    private fun updateWorkoutList(workout: Workout) : Int{
        // iterate it using a mutable iterator and modify values
        val iterate = workouts.listIterator()
        var weekNo = 0
        while (iterate.hasNext()) {
            val oldValue = iterate.next()
            if (oldValue.id == workout.id){
                iterate.set(workout)
                weekNo = workout.weekNo - 1
                break
            }
        }
        if(weekNo == 0) {
            //It's a new workout
            workouts.add(workout)
            weekNo = workout.weekNo - 1
        }
        return weekNo
    }
}