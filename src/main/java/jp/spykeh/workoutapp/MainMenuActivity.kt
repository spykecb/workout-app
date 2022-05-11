package jp.spykeh.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import jp.spykeh.workoutapp.fragment.RoutinesFragment
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {
    private val routinesFragment = RoutinesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        replaceFragment(routinesFragment)
        bnvMain.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itmRoutines -> replaceFragment(routinesFragment)
            }
            true

        }
        nvMain.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.itmRoutines -> {
                    replaceFragment(routinesFragment)
                    true
                }
                else -> false
            }
        }
    }

    fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}