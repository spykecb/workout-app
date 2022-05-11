package jp.spykeh.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import jp.spykeh.workoutapp.adapter.WorkoutExerciseAdapter
import jp.spykeh.workoutapp.data.*
import jp.spykeh.workoutapp.db.SQLiteDBHelper
import kotlinx.android.synthetic.main.activity_workout.*
import java.util.*

class WorkoutActivity : AppCompatActivity() {
    private lateinit var db: SQLiteDBHelper
    private lateinit var exerciseAdapter: WorkoutExerciseAdapter
    private lateinit var workout: Workout
    private var prevWorkout: Workout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout)

        val exercises = intent.getSerializableExtra("exercises") as MutableList<ExerciseSelection>
        val weekNo = intent.getIntExtra("week_no", 0)
        val dayNo = intent.getIntExtra("day_no", 0)
        val routine : Routine = intent.getSerializableExtra("routine") as Routine
        val workoutId = intent.getLongExtra("workout_id", 0)

        db = SQLiteDBHelper(this)

        if (workoutId == 0L ){
            workout = Workout(0, Date(), weekNo, dayNo, mutableListOf())
            for(exercise in exercises){
                val we = WorkoutExercise(exercise, mutableListOf(), null)
                repeat(exercise.sets.second){
                    val eSet = WorkoutExerciseSet(it + 1, 0f, 0)
                    if (it >= exercise.sets.first) eSet.isOptional = true
                    we.sets.add(eSet)
                }
                workout.addExercise(we)
            }
        }else{
            workout = db.getWorkout(workoutId)!!
            exercises.forEachIndexed{ it, exercise ->
                var we = WorkoutExercise(exercise, mutableListOf(), null)
                for (wExercise in workout.exercises){
                    if(wExercise.exerciseSelection.id == exercise.id){
                        we = wExercise
                    }
                }
                val savedSetCount = we.sets.size
                repeat(exercise.sets.second - savedSetCount){
                    val eSet = WorkoutExerciseSet(it + 1 + savedSetCount, 0f, 0)
                    if (it >= exercise.sets.first) eSet.isOptional = true
                    we.sets.add(eSet)
                }
            }
        }

        prevWorkout = db.getPreviousWeeksWorkoutOfRoutine(routine.id, workout)

        exerciseAdapter = WorkoutExerciseAdapter(workout.exercises)

        rvWOExercises.adapter = exerciseAdapter
        rvWOExercises.layoutManager = LinearLayoutManager(this)

        btnSave.setOnClickListener{
            if(workout.id == 0L){
                workout = db.insertWorkout(workout, routine.id)
            }else{
                workout = db.updateWorkout(workout)
            }
            var data = Intent()
            data.putExtra("workout", workout)
            setResult(RESULT_OK, data)
            finish()

        }
    }

    fun setExerciseReps(exerciseId: Long, set: Int, reps: Int){
        workout.setExerciseReps(exerciseId, set, reps)
    }

    fun setExerciseWeight(exerciseId: Long, set: Int, weight: Float){
        workout.setExerciseWeight(exerciseId, set, weight)
    }

    fun setExerciseNote(exerciseId: Long, note: String){
        workout.setExerciseNote(exerciseId, note)
    }

    fun getPreviousWorkout(): Workout?{
        return prevWorkout
    }

    fun isEditMode() : Boolean{
        return workout.id != 0L
    }

}