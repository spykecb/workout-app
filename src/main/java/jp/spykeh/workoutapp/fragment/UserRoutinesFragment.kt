package jp.spykeh.workoutapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.spykeh.workoutapp.R
import jp.spykeh.workoutapp.RoutineEditorActivity
import jp.spykeh.workoutapp.adapter.RoutineAdapter
import jp.spykeh.workoutapp.api.SaikyoApi
import jp.spykeh.workoutapp.api.SaikyoApiInterface
import jp.spykeh.workoutapp.data.UserRoutine
import jp.spykeh.workoutapp.data.Workout
import jp.spykeh.workoutapp.db.SQLiteDBHelper
import kotlinx.android.synthetic.main.fragment_routines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RoutinesFragment : Fragment() {
    private lateinit var rvAdapter: RoutineAdapter
    private lateinit var db: SQLiteDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SQLiteDBHelper(requireContext())
        var routines = db.getAllRoutines()
        rvAdapter = RoutineAdapter(routines)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvRoutines.adapter = rvAdapter
        rvRoutines.layoutManager = LinearLayoutManager(requireContext())

        btnNewRoutine.setOnClickListener{
            var intent = Intent(context, RoutineEditorActivity::class.java)
            startActivity(intent)
        }

        swipeToRefresh.setOnRefreshListener {
            val apiBuilder = SaikyoApi.apiBuilder
            val data = apiBuilder.getUserRoutines();
            data.enqueue(object: Callback<List<UserRoutine>>{
                override fun onResponse(
                    call: Call<List<UserRoutine>>,
                    response: Response<List<UserRoutine>>
                ) {
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT);
                    swipeToRefresh.isRefreshing = false;
                }

                override fun onFailure(call: Call<List<UserRoutine>>, t: Throwable) {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    swipeToRefresh.isRefreshing = false;
                }

            })
        }

    }

}