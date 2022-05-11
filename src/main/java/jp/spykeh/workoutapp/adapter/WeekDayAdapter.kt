package jp.spykeh.workoutapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.WorkoutActivity
import jp.spykeh.workoutapp.WorkoutWeekViewActivity
import jp.spykeh.workoutapp.data.ExerciseDay
import kotlinx.android.synthetic.main.item_workout_day.view.*

class WeekDayAdapter (
    private val weekdays: MutableList<ExerciseDay>,
    private val week_no: Int,
    private val workout_id: Long?
) : RecyclerView.Adapter<WeekDayAdapter.WeekdayViewHolder>() {
    class WeekdayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekdayViewHolder {
        return WeekdayViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_workout_day,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeekdayViewHolder, position: Int) {
        val curDay = weekdays[position]

        val routine = (holder.itemView.context as WorkoutWeekViewActivity).getRoutine()
        val workout = (holder.itemView.context as WorkoutWeekViewActivity)
            .findWorkoutOfDay(week_no, curDay.day)
        val launcher = (holder.itemView.context as WorkoutWeekViewActivity).resultLauncher

        var intent = Intent(holder.itemView.context, WorkoutActivity::class.java)
        intent.putExtra("exercises", ArrayList(curDay.exerciseSelections))
        intent.putExtra("week_no", week_no)
        intent.putExtra("day_no", curDay.day)
        intent.putExtra("routine", routine)

        holder.itemView.apply {
            tvDay.text = "Day ${curDay.day}"
            tvExerciseCount.text = "${curDay.exerciseSelections.count()} Exercises"
            if(workout == null){
                btnStartOrView.text = "Start"
            }else{
                btnStartOrView.text = "View"
                intent.putExtra("workout_id", workout.id)
            }
            btnStartOrView.setOnClickListener{
                launcher.launch(intent)
            }


        }
    }

    override fun getItemCount(): Int {
        return weekdays.size
    }

}