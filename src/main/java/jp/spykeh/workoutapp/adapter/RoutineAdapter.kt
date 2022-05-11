package jp.spykeh.workoutapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.RoutineEditorActivity
import jp.spykeh.workoutapp.WorkoutWeekViewActivity
import jp.spykeh.workoutapp.data.Routine
import kotlinx.android.synthetic.main.item_routine.view.*

class RoutineAdapter (
    private val routines: MutableList<Routine>
) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>(){
    class RoutineViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    fun addRoutine(routine: Routine){
        routines.add(routine)
        notifyItemInserted(routines.size - 1)
    }

    fun deleteRoutine(index: Int){
        routines.removeAt(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        return RoutineViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_routine,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val curRoutine = routines[position]
        holder.itemView.apply {
            tvRoutineName.text = curRoutine.title
        }

        holder.itemView.btnEdit.setOnClickListener{
            var intent = Intent(holder.itemView.context, RoutineEditorActivity::class.java)
            intent.putExtra("routine_id", curRoutine.id)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.btnDuplicate.setOnClickListener{
            var intent = Intent(holder.itemView.context, RoutineEditorActivity::class.java)
            intent.putExtra("routine_id", curRoutine.id)
            intent.putExtra("duplicate", true)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.btnExercise.setOnClickListener{
            var intent = Intent(holder.itemView.context, WorkoutWeekViewActivity::class.java)
            intent.putExtra("routine_id", curRoutine.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return routines.size
    }
}