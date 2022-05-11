package jp.spykeh.workoutapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.RoutineEditorActivity
import jp.spykeh.workoutapp.data.ExerciseSelection
import jp.spykeh.workoutapp.fragment.ExerciseOptionsFragment
import kotlinx.android.synthetic.main.item_exercise.view.*

class EditExerciseAdapter(
    private val exerciseSelections: MutableList<ExerciseSelection>,
    private val dayPosition: Int
) : RecyclerView.Adapter<EditExerciseAdapter.ExerciseViewHolder>(){
    class ExerciseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    fun addExercise(exerciseSelection: ExerciseSelection){
        exerciseSelections.add(exerciseSelection)
        notifyItemInserted(exerciseSelections.size - 1)
    }

    fun updateExercise(exerciseSelection: ExerciseSelection, pos: Int){
        exerciseSelections[pos] = exerciseSelection
        notifyItemChanged(pos)
    }

    fun getExerciseAtPosition(position: Int) : ExerciseSelection{
        return exerciseSelections[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_exercise,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val curExercise = exerciseSelections[position]
        holder.itemView.apply {
            tvItemExerciseName.text = curExercise.exercise.title
            tvItemSets.text = "${curExercise.sets.first}-${curExercise.sets.second}"
            tvItemReps.text = curExercise.reps
            clExerciseContainer.setOnLongClickListener {
                val dialog = ExerciseOptionsFragment()
                val args = Bundle()
                args.putInt("dayPosition", dayPosition)
                args.putInt("exercisePosition", position)
                dialog.arguments = args
                val mngr = (holder.itemView.context as RoutineEditorActivity).supportFragmentManager
                dialog.show(mngr, "exerciseOptions")
                true
            }

        }
    }

    override fun getItemCount(): Int {
        return exerciseSelections.size
    }
}