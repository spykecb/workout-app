package jp.spykeh.workoutapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.RoutineEditorActivity
import jp.spykeh.workoutapp.data.ExerciseDay
import jp.spykeh.workoutapp.fragment.ExerciseOptionsFragment
import kotlinx.android.synthetic.main.item_exercise_day.view.*


class EditExerciseDayAdapter(
    private val exerciseDays: MutableList<ExerciseDay>
) : RecyclerView.Adapter<EditExerciseDayAdapter.ExerciseDayViewHolder>(){

    class ExerciseDayViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    fun addExerciseDay(exerciseDay: ExerciseDay){
        exerciseDays.add(exerciseDay)
        notifyItemInserted(exerciseDays.size - 1)
    }

    fun getExerciseDays() : MutableList<ExerciseDay>{
        return exerciseDays
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseDayViewHolder {
        return ExerciseDayViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_exercise_day,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseDayViewHolder, position: Int) {
        val curExerciseDay = exerciseDays[position]

        holder.itemView.tvDay.apply{
            text = "Day " + curExerciseDay.day.toString()
        }

        val exerciseDayAdapter = EditExerciseAdapter(curExerciseDay.exerciseSelections, position)
        holder.itemView.rvExercises.apply {
            adapter = exerciseDayAdapter
            layoutManager =  LinearLayoutManager(holder.itemView.context)
        }

        holder.itemView.btnAddExercise.setOnClickListener{
            var dialog = ExerciseOptionsFragment()
            val args = Bundle()
            args.putInt("dayPosition", position)
            dialog.arguments = args
            val mngr = (holder.itemView.context as RoutineEditorActivity).supportFragmentManager
            dialog.show(mngr, "exerciseOptions")
        }
    }

    override fun getItemCount(): Int {
        return exerciseDays.size
    }
}