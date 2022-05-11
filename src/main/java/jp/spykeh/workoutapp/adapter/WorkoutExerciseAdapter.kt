package jp.spykeh.workoutapp.adapter;

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.RoutineEditorActivity
import jp.spykeh.workoutapp.WorkoutActivity
import jp.spykeh.workoutapp.data.WorkoutExercise
import jp.spykeh.workoutapp.fragment.ExerciseHistoryFragment
import jp.spykeh.workoutapp.fragment.ExerciseOptionsFragment
import kotlinx.android.synthetic.main.item_exercise_day.view.*
import kotlinx.android.synthetic.main.item_workout_exercise.view.*

class WorkoutExerciseAdapter(
    private val exercises: MutableList<WorkoutExercise>
) : RecyclerView.Adapter<WorkoutExerciseAdapter.ExerciseViewHolder>(){
    class ExerciseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_workout_exercise,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val curExercise = exercises[position]
        val prevWorkout = (holder.itemView.context as WorkoutActivity).getPreviousWorkout()
        var prevExercise: WorkoutExercise? = null
        if(prevWorkout != null){
            for(_ex in prevWorkout.exercises){
                if(_ex.exerciseSelection.id == curExercise.exerciseSelection.id){
                    prevExercise = _ex
                }
            }
        }
        holder.itemView.apply {
            tvItemExerciseName.text = curExercise.exerciseSelection.exercise.title
            if(!curExercise.note.isNullOrEmpty()){
                etExerciseNotes.setText(curExercise.note)
            }else if(prevExercise != null){
                etExerciseNotes.setText(prevExercise.note)
            }

            etExerciseNotes.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    (context as WorkoutActivity).setExerciseNote(
                        curExercise.exerciseSelection.id!!,
                        s.toString()
                    )
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            var exerciseAdapter = WorkoutExerciseSetAdapter(curExercise.sets, curExercise)
            rvWOExerciseSets.adapter = exerciseAdapter
            rvWOExerciseSets.layoutManager = LinearLayoutManager(this.context)

            ibHistory.setOnClickListener{
                var dialog = ExerciseHistoryFragment()
                val args = Bundle()
                args.putLong("exerciseId", curExercise.exerciseSelection.exercise.id!!)
                args.putString("exerciseTitle", curExercise.exerciseSelection.exercise.title!!)
                dialog.arguments = args
                val mngr = (holder.itemView.context as WorkoutActivity).supportFragmentManager
                dialog.show(mngr, "exerciseHistory")
            }
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}
