package jp.spykeh.workoutapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.data.ExerciseDay
import kotlinx.android.synthetic.main.item_workout_week.view.*

class WeekAdapter (
    private val weeks: MutableList<Pair<Int, MutableList<ExerciseDay>>>
) : RecyclerView.Adapter<WeekAdapter.WeekdayViewHolder>() {
    class WeekdayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekdayViewHolder {
        return WeekdayViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_workout_week,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeekdayViewHolder, position: Int) {
        val curWeek = weeks[position]

        var exerciseAdapter = WeekDayAdapter(curWeek.second, curWeek.first, null)
        holder.itemView.apply {
            tvWeekNo.text = resources.getQuantityString(R.plurals.workout_week, curWeek.first, curWeek.first)
            rvWeekDays.adapter = exerciseAdapter
            rvWeekDays.layoutManager = LinearLayoutManager(context)

        }
    }

    override fun getItemCount(): Int {
        return weeks.size
    }
}