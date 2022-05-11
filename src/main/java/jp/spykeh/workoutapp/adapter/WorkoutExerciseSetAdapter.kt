package jp.spykeh.workoutapp.adapter;

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.WorkoutActivity
import jp.spykeh.workoutapp.data.WorkoutExercise
import jp.spykeh.workoutapp.data.WorkoutExerciseSet
import kotlinx.android.synthetic.main.item_workout_exercise_set.view.*

class WorkoutExerciseSetAdapter(
    private val exercises: MutableList<WorkoutExerciseSet>,
    private val exercise: WorkoutExercise
) : RecyclerView.Adapter<WorkoutExerciseSetAdapter.ExerciseViewHolder>(){
    class ExerciseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    fun isInteger(str: String?) = str?.toIntOrNull()?.let { true } ?: false
    fun isFloat(str: String?) = str?.toFloatOrNull()?.let { true } ?: false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_workout_exercise_set,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val curExerciseSet = exercises[position]
        val prevWorkout = (holder.itemView.context as WorkoutActivity).getPreviousWorkout()
        var prevExercise: WorkoutExercise? = null
        if(prevWorkout != null){
            for(_ex in prevWorkout.exercises){
                if(_ex.exerciseSelection.id == exercise.exerciseSelection.id){
                    prevExercise = _ex
                }
            }
        }
        val prevExerciseSet = if(prevExercise != null && prevExercise.sets.size > position) prevExercise.sets[position] else null
        holder.itemView.apply {
            tvSetNumber.text = "${position + 1}"

            //Set
            if(curExerciseSet.isOptional){
                tvSetNumber.setTextColor(ContextCompat.getColor(context, R.color.greyText))
            }

            //Reps
            tvTargetReps.text = "${exercise.exerciseSelection.reps} reps"

            etItemReps.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    var rep =  if(!isInteger(s.toString())) 0 else s.toString().toInt()

                    (context as WorkoutActivity).setExerciseReps(
                        exercise.exerciseSelection.id!!,
                        curExerciseSet.set,
                        rep
                    )
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            btnRepDec.setOnClickListener{
                if(isInteger(etItemReps.text.toString()) && etItemReps.text.toString().toInt() > 0){
                    var value = etItemReps.text.toString().toInt() - 1
                    etItemReps.setText(value.toString())
                }
            }
            btnRepInc.setOnClickListener{
                if(isInteger(etItemReps.text.toString())){
                    var value = etItemReps.text.toString().toInt() + 1
                    etItemReps.setText(value.toString())
                }

            }

            if((context as WorkoutActivity).isEditMode()) {
                etItemReps.setText(curExerciseSet.reps.toString())
            }else if(curExerciseSet.reps != 0){
                etItemReps.setText(curExerciseSet.reps.toString())
            }else if(prevExerciseSet != null && prevExerciseSet.reps != 0){
                etItemReps.setText(prevExerciseSet.reps.toString())
            }else{
                val repsPart = exercise.exerciseSelection.reps.split("-").map { it.toInt() }
                var reps = repsPart[0]
                if(repsPart.size > 1){
                    reps = (repsPart[0] + repsPart[1]) / 2
                }
                curExerciseSet.reps = reps
                etItemReps.setText(reps.toString())
            }



            //Weight
            if((context as WorkoutActivity).isEditMode()) {
                etItemWeight.setText(curExerciseSet.weight.toString())
            }else if(curExerciseSet.weight != 0f){
                etItemWeight.setText(curExerciseSet.weight.toString())
            }else{
                etItemWeight.setHint(if(prevExerciseSet != null) prevExerciseSet?.weight.toString() else "0")
            }
            etItemWeight.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    var weight =  if(!isFloat(s.toString())) 0f else s.toString().toFloat()

                    (context as WorkoutActivity).setExerciseWeight(
                        exercise.exerciseSelection.id!!,
                        curExerciseSet.set,
                        weight
                    )
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}


