package jp.spykeh.workoutapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.data.ExerciseHistoryItem
import kotlinx.android.synthetic.main.item_exercise_hist.view.*
import java.text.SimpleDateFormat

class ExerciseHistoryAdapter(
    private val exerciseDays: MutableList<ExerciseHistoryItem>
) : RecyclerView.Adapter<ExerciseHistoryAdapter.ExerciseHistoryViewHolder>(){
    class ExerciseHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseHistoryViewHolder {
        return ExerciseHistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_exercise_hist,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ExerciseHistoryViewHolder,
        position: Int
    ) {
        val curExerciseDay = exerciseDays[position]
        val completionDateStr = SimpleDateFormat("yyyy-MM-dd")
            .format(curExerciseDay.completionDate)
        holder.itemView.apply {
            tvWorkoutDate.setText(completionDateStr)
            tvWorkoutSets.setText(curExerciseDay.getSetsString())
        }
    }

    override fun getItemCount(): Int {
        return exerciseDays.size
    }
}
