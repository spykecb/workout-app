package jp.spykeh.workoutapp.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import jp.spykeh.workoutapp.data.Exercise

class ExerciseSpinnerAdapter(
    context: Context,
    textViewResourceId: Int,
    val list: List<Exercise>
) : ArrayAdapter<Exercise>(
    context,
    textViewResourceId,
    list
) {
    override fun getCount() = list.size

    override fun getItem(position: Int) = list[position]

    override fun getItemId(position: Int) = list[position].id!!.toLong()

    override fun getPosition(exercise: Exercise?) : Int {
        list.forEachIndexed{ index, item ->
            if(item.title == exercise?.title){
                return index
            }
        }
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return (super.getDropDownView(position, convertView, parent) as TextView).apply {
            text = list[position].title
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return (super.getDropDownView(position, convertView, parent) as TextView).apply {
            text = list[position].title
        }
    }

}