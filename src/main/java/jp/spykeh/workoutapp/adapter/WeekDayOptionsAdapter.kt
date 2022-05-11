package jp.spykeh.workoutapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.spykeh.workoutapp.R
import kotlinx.android.synthetic.main.item_day_add.view.*

class WeekDayOptionsAdapter (
    private val weekdays: MutableList<Pair<String, Boolean>>
) : RecyclerView.Adapter<WeekDayOptionsAdapter.WeekdayViewHolder>() {
    class WeekdayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun getItems() : MutableList<Pair<String, Boolean>>{
        return weekdays
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekdayViewHolder {
        return WeekdayViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_day_add,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeekdayViewHolder, position: Int) {
        val curDay = weekdays[position]
        holder.itemView.apply {
            tvWeekDayName.text = curDay.first
            tvWeekDayCB.isChecked = curDay.second
        }
        holder.itemView.tvWeekDayCB.setOnCheckedChangeListener{ _, isChecked ->
            weekdays[position] = curDay.copy(second = isChecked)
        }
    }

    override fun getItemCount(): Int {
        return weekdays.size
    }
}