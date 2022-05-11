package jp.spykeh.workoutapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.RoutineEditorActivity
import jp.spykeh.workoutapp.adapter.EditExerciseAdapter
import jp.spykeh.workoutapp.adapter.ExerciseSpinnerAdapter
import jp.spykeh.workoutapp.data.Exercise
import jp.spykeh.workoutapp.data.ExerciseSelection
import jp.spykeh.workoutapp.data.MuscleGroup
import jp.spykeh.workoutapp.db.SQLiteDBHelper
import kotlinx.android.synthetic.main.activity_routine_editor.*
import kotlinx.android.synthetic.main.fragment_exercise_opts.view.*
import kotlinx.android.synthetic.main.fragment_exercise_opts.view.btnAddExercise
import kotlinx.android.synthetic.main.item_exercise_day.view.*

class ExerciseOptionsFragment: DialogFragment(), AdapterView.OnItemSelectedListener {
    private lateinit var exerciseSelection: ExerciseSelection
    private lateinit var exercises: ArrayList<Exercise>
    private lateinit var db: SQLiteDBHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView : View = inflater.inflate(R.layout.fragment_exercise_opts, container, false)
        val dayPosition: Int? = arguments?.getInt("dayPosition", -1)
        val exercisePosition: Int? = arguments?.getInt("exercisePosition", -1)

        val vHolder = (activity as RoutineEditorActivity).rvDays.findViewHolderForAdapterPosition(dayPosition!!)
        val rvExercises = vHolder?.itemView?.rvExercises
        var adapterEdit: EditExerciseAdapter = rvExercises?.adapter as EditExerciseAdapter
        val spinner = rootView.spinnerExercise

        db = SQLiteDBHelper(requireContext())
        exercises = db.getExercises()

        spinner.onItemSelectedListener = this
        // Create the instance of ArrayAdapter
        // having the list of courses
        val ad = ExerciseSpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            exercises)
        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spinner.adapter = ad


        if(exercisePosition != -1){
            exerciseSelection = adapterEdit.getExerciseAtPosition(exercisePosition!!)
            rootView.apply {
                spinner.setSelection(ad.getPosition(exerciseSelection.exercise))
                etSets.setText("${exerciseSelection.sets.first}-${exerciseSelection.sets.second}")
                etReps.setText(exerciseSelection.reps)
            }
        }

        rootView.btnAddExercise.setOnClickListener{
            val selectedExercise = (spinner.selectedItem as Exercise)
            var setMin = 0
            var setUpper = 0
            if(rootView.etSets.text.toString().contains("-")){
                setMin = rootView.etSets.text.toString().split("-")[0].toInt()
                setUpper = rootView.etSets.text.toString().split("-")[1].toInt()
            }else{
                setMin = rootView.etSets.text.toString().toInt()
                setUpper = setMin
            }
            if(!this::exerciseSelection.isInitialized){
                val exercise = ExerciseSelection(
                    0,
                    exercisePosition,
                    selectedExercise,
                    Pair(setMin, setUpper),
                    rootView.etReps.text.toString()
                )
                adapterEdit.addExercise(exercise)
            }else{
                exerciseSelection.exercise = selectedExercise
                exerciseSelection.order_no = exercisePosition
                exerciseSelection.sets = Pair(setMin, setUpper)
                exerciseSelection.reps = rootView.etReps.text.toString()
                adapterEdit.updateExercise(exerciseSelection, exercisePosition)
            }

            dismiss()
        }
        return rootView
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // make toastof name of course
        // which is selected in spinner
        Toast.makeText(context,
            exercises[position].title,
            Toast.LENGTH_LONG)
            .show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}