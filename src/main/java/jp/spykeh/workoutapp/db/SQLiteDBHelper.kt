package jp.spykeh.workoutapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import jp.spykeh.workoutapp.data.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SQLiteDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    object Users {
        const val TABLE_NAME = "users"
        object Columns : BaseColumns {
            const val EMAIL = "email"
            const val NAME = "name"
        }
    }
    object Routines {
        const val TABLE_NAME = "routines"
        object Columns : BaseColumns {
            const val TITLE = "title"
            const val WEEKS = "weeks"
        }
    }

    object ExerciseDays {
        const val TABLE_NAME = "exercise_days"
        object Columns : BaseColumns {
            const val DAY = "day"
            const val ROUTINE_ID = "routine_id"
        }
    }

    object ExerciseSelections {
        const val TABLE_NAME = "exercise_selections"
        object Columns : BaseColumns {
            const val EXERCISE_ID = "exercise_id"
            const val SETS_MIN = "sets_min"
            const val SETS_UPPER = "sets_upper"
            const val REPS = "reps"
            const val EXERCISE_DAY_ID = "exercise_day_id"
            const val ORDER_NO = "order_no"
        }
    }

    object Exercises {
        const val TABLE_NAME = "exercises"
        object Columns : BaseColumns {
            const val TITLE = "title"
            const val MUSCLE_GRP_ID = "muscle_grp_id"
        }
    }

    object MuscleGroups {
        const val TABLE_NAME = "muscle_groups"
        object Columns : BaseColumns {
            const val TITLE = "title"
        }
    }

    object Workouts {
        const val TABLE_NAME = "workouts"
        object Columns : BaseColumns {
            const val COMPLETION_DATE = "completion_date"
            const val ROUTINE_ID = "routine_id"
            const val WEEK_NO = "week_no"
            const val DAY_NO = "day_no"
        }
    }

    object WorkoutExercises {
        const val TABLE_NAME = "workout_exercises"
        object Columns : BaseColumns {
            const val WORKOUT_ID = "workout_id"
            const val EXERCISE_ID = "exercise_id"
            const val NOTE = "note"
        }
    }

    object WorkoutExerciseSets {
        const val TABLE_NAME = "workout_exercise_sets"
        object Columns : BaseColumns {
            const val WORKOUT_ID = "workout_id"
            const val EXERCISE_ID = "exercise_id"
            const val SET = "set_no"
            const val REPS = "reps"
            const val WEIGHT = "weight"
        }
    }


    companion object {
        private const val DATABASE_VERSION = 20
        private const val DATABASE_NAME = "saikyo.db"
        private const val SQL_CREATE_TABLE_USERS =
            "CREATE TABLE ${Users.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${Users.Columns.EMAIL} TEXT," +
                    "${Users.Columns.NAME} TEXT)"
        private const val SQL_CREATE_TABLE_ROUTINES =
            "CREATE TABLE ${Routines.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${Routines.Columns.TITLE} TEXT NOT NULL," +
                    "${Routines.Columns.WEEKS} INTEGER NOT NULL)"
        private const val SQL_CREATE_TABLE_EXERCISE_DAYS =
            "CREATE TABLE ${ExerciseDays.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${ExerciseDays.Columns.DAY} INTEGER NOT NULL," +
                    "${ExerciseDays.Columns.ROUTINE_ID} INTEGER," +
                    "FOREIGN KEY(${ExerciseDays.Columns.ROUTINE_ID}) " +
                    "REFERENCES ${Routines.TABLE_NAME}(${BaseColumns._ID}))"
        private const val SQL_CREATE_TABLE_MUSCLE_GROUPS =
            "CREATE TABLE ${MuscleGroups.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${MuscleGroups.Columns.TITLE} TEXT NOT NULL)"
        private const val SQL_CREATE_TABLE_EXERCISES =
            "CREATE TABLE ${Exercises.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${Exercises.Columns.TITLE} TEXT NOT NULL," +
                    "${Exercises.Columns.MUSCLE_GRP_ID} INTEGER NOT NULL," +
                    "FOREIGN KEY(${Exercises.Columns.MUSCLE_GRP_ID})" +
                    "REFERENCES ${MuscleGroups.TABLE_NAME}(${BaseColumns._ID}))"
        private const val SQL_CREATE_TABLE_EXERCISE_SELECTIONS =
            "CREATE TABLE ${ExerciseSelections.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${ExerciseSelections.Columns.EXERCISE_DAY_ID} INTEGER," +
                    "${ExerciseSelections.Columns.ORDER_NO} INTEGER," +
                    "${ExerciseSelections.Columns.EXERCISE_ID} INTEGER NOT NULL," +
                    "${ExerciseSelections.Columns.SETS_MIN} INTEGER," +
                    "${ExerciseSelections.Columns.SETS_UPPER} INTEGER," +
                    "${ExerciseSelections.Columns.REPS} TEXT NOT NULL," +
                    "FOREIGN KEY(${ExerciseSelections.Columns.EXERCISE_DAY_ID}) " +
                    "REFERENCES ${ExerciseDays.TABLE_NAME}(${BaseColumns._ID}), " +
                    "FOREIGN KEY(${ExerciseSelections.Columns.EXERCISE_ID})" +
                    "REFERENCES ${Exercises.TABLE_NAME}(${BaseColumns._ID}))"
        private const val SQL_CREATE_TABLE_WORKOUTS =
            "CREATE TABLE ${Workouts.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${Workouts.Columns.COMPLETION_DATE} DATE NOT NULL," +
                    "${Workouts.Columns.ROUTINE_ID} INTEGER NOT NULL," +
                    "${Workouts.Columns.WEEK_NO} INTEGER NOT NULL," +
                    "${Workouts.Columns.DAY_NO} INTEGER NOT NULL," +
                    "FOREIGN KEY(${Workouts.Columns.ROUTINE_ID}) " +
                    "REFERENCES ${Routines.TABLE_NAME}(${BaseColumns._ID}))"
        private const val SQL_CREATE_TABLE_WORKOUT_EXERCISE =
            "CREATE TABLE ${WorkoutExercises.TABLE_NAME} (" +
                    "${WorkoutExercises.Columns.WORKOUT_ID} INTEGER," +
                    "${WorkoutExercises.Columns.EXERCISE_ID} INTEGER," +
                    "${WorkoutExercises.Columns.NOTE} TEXT," +
                    "FOREIGN KEY(${WorkoutExercises.Columns.WORKOUT_ID}) " +
                    "REFERENCES ${Workouts.TABLE_NAME}(${BaseColumns._ID}), " +
                    "FOREIGN KEY(${WorkoutExercises.Columns.EXERCISE_ID})" +
                    "REFERENCES ${ExerciseSelections.TABLE_NAME}(${BaseColumns._ID})," +
                    "PRIMARY KEY (${WorkoutExercises.Columns.WORKOUT_ID}," +
                    "${WorkoutExercises.Columns.EXERCISE_ID}))"

        private const val SQL_CREATE_TABLE_WORKOUT_EXERCISE_SET =
            "CREATE TABLE ${WorkoutExerciseSets.TABLE_NAME} (" +
                    "${WorkoutExerciseSets.Columns.WORKOUT_ID} INTEGER," +
                    "${WorkoutExerciseSets.Columns.EXERCISE_ID} INTEGER," +
                    "${WorkoutExerciseSets.Columns.SET} INTEGER NOT NULL," +
                    "${WorkoutExerciseSets.Columns.REPS} INTEGER NOT NULL," +
                    "${WorkoutExerciseSets.Columns.WEIGHT} REAL NOT NULL," +
                    "FOREIGN KEY(${WorkoutExercises.Columns.WORKOUT_ID}) " +
                    "REFERENCES ${Workouts.TABLE_NAME}(${BaseColumns._ID}), " +
                    "FOREIGN KEY(${WorkoutExercises.Columns.EXERCISE_ID})" +
                    "REFERENCES ${ExerciseSelections.TABLE_NAME}(${BaseColumns._ID})," +
                    "PRIMARY KEY (${WorkoutExerciseSets.Columns.WORKOUT_ID}," +
                    "${WorkoutExerciseSets.Columns.EXERCISE_ID}," +
                    "${WorkoutExerciseSets.Columns.SET})) "


        /*
        Test data for now
        * */
        private const val SQL_INSERT_TABLE_ROUTINES =
            "INSERT INTO ${Routines.TABLE_NAME} (${Routines.Columns.TITLE}, ${Routines.Columns.WEEKS}) " +
                    "VALUES ('PHUL', 8)"

        private val SQL_INSERT_TABLE_EXERCISE_DAYS = arrayOf(
            "INSERT INTO ${ExerciseDays.TABLE_NAME} (${ExerciseDays.Columns.ROUTINE_ID}, ${ExerciseDays.Columns.DAY}) " +
                    "VALUES (1, 1)",
            "INSERT INTO ${ExerciseDays.TABLE_NAME} (${ExerciseDays.Columns.ROUTINE_ID}, ${ExerciseDays.Columns.DAY}) " +
                    "VALUES (1, 2)",
            "INSERT INTO ${ExerciseDays.TABLE_NAME} (${ExerciseDays.Columns.ROUTINE_ID}, ${ExerciseDays.Columns.DAY}) " +
                    "VALUES (1, 3)",
            "INSERT INTO ${ExerciseDays.TABLE_NAME} (${ExerciseDays.Columns.ROUTINE_ID}, ${ExerciseDays.Columns.DAY}) " +
                    "VALUES (1, 4)")

        private val SQL_INSERT_TABLE_MUSCLE_GROUPS = arrayOf(
            "INSERT INTO ${MuscleGroups.TABLE_NAME} (${MuscleGroups.Columns.TITLE}) " +
                    "VALUES ('Chest')",
            "INSERT INTO ${MuscleGroups.TABLE_NAME} (${MuscleGroups.Columns.TITLE}) " +
                    "VALUES ('Upper Back')",
            "INSERT INTO ${MuscleGroups.TABLE_NAME} (${MuscleGroups.Columns.TITLE}) " +
                    "VALUES ('Lats')",
            "INSERT INTO ${MuscleGroups.TABLE_NAME} (${MuscleGroups.Columns.TITLE}) " +
                    "VALUES ('Shoulders')",
            "INSERT INTO ${MuscleGroups.TABLE_NAME} (${MuscleGroups.Columns.TITLE}) " +
                    "VALUES ('Biceps')",
            "INSERT INTO ${MuscleGroups.TABLE_NAME} (${MuscleGroups.Columns.TITLE}) " +
                    "VALUES ('Triceps')",
            "INSERT INTO ${MuscleGroups.TABLE_NAME} (${MuscleGroups.Columns.TITLE}) " +
                    "VALUES ('Quads')",
            "INSERT INTO ${MuscleGroups.TABLE_NAME} (${MuscleGroups.Columns.TITLE}) " +
                    "VALUES ('Hamstrings')",
            "INSERT INTO ${MuscleGroups.TABLE_NAME} (${MuscleGroups.Columns.TITLE}) " +
                    "VALUES ('Calves')")

        private val SQL_INSERT_TABLE_EXERCISES = arrayOf(
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Barbell Bench Press', 1)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Incline Dumbbell Bench Press', 1)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Bent Over Row', 2)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Lat Pull Down', 3)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Overhead Press', 4)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Barbell Curl', 5)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Skullcrusher', 6)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Squat', 7)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Deadlift', 8)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Leg Press', 7)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Leg Curl', 8)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Standing Machine Calf Raise', 9)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Leg Press Calf Raise', 9)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Incline Barbell Bench Press', 1)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Flat Bench Dumbbell Flye', 1)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Seated Cable Row', 2)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('One Arm Dumbbell Row', 2)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Dumbbell Lateral Raise', 4)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Seated Incline Dumbbell Curl', 5)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Cable Tricep Extension', 6)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Front Squat', 7)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Barbell Lunge', 7)",
            "INSERT INTO ${Exercises.TABLE_NAME} (${Exercises.Columns.TITLE}, ${Exercises.Columns.MUSCLE_GRP_ID}) " +
                    "VALUES ('Leg Extension', 7)")

        private val SQL_INSERT_TABLE_EXERCISE_SELECTIONS = arrayOf(
            "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (1, 1, 1, 3, 4, '3-5');" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (1, 2, 2, 3, 4, '6-10');" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (1, 3, 3, 3, 4, '3-5')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (1, 4, 4, 3, 4, '6-10')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (1, 5, 5, 3, 4, '5-8')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (1, 6, 6, 3, 4, '6-10')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (1, 7, 7, 3, 4, '6-10')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (2, 1, 8, 3, 4, '3-5')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (2, 2, 9, 3, 4, '3-5')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (2, 3, 10, 3, 4, '10-15')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (2, 4, 11, 3, 4, '6-10')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (2, 5, 12, 3, 4, '6-10')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (3, 1, 14, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (3, 2, 15, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (3, 3, 16, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (3, 4, 17, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (3, 5, 18, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (3, 6, 19, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (3, 7, 20, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (4, 1, 8, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (4, 2, 22, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (4, 3, 23, 3, 4, '10-15')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (4, 4, 11, 3, 4, '10-15')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (4, 5, 12, 3, 4, '8-12')" ,
                    "INSERT INTO ${ExerciseSelections.TABLE_NAME} (${ExerciseSelections.Columns.EXERCISE_DAY_ID}, ${ExerciseSelections.Columns.ORDER_NO}, ${ExerciseSelections.Columns.EXERCISE_ID}, " +
                    "${ExerciseSelections.Columns.SETS_MIN}, ${ExerciseSelections.Columns.SETS_UPPER}, ${ExerciseSelections.Columns.REPS}) " +
                    "VALUES (4, 6, 13, 3, 4, '8-12')")

        //TMP
        private val SQL_INSERT_TABLE_WORKOUTS = arrayOf(

            "INSERT INTO workouts (completion_date,routine_id,week_no,day_no) VALUES('2022-03-08 00:00:00',1,1,2)" ,
            "INSERT INTO workouts (completion_date,routine_id,week_no,day_no) VALUES('2022-03-10 00:00:00',1,1,3)" ,
            "INSERT INTO workouts (completion_date,routine_id,week_no,day_no) VALUES('2022-03-11 00:00:00',1,1,4)" ,
            "INSERT INTO workouts (completion_date,routine_id,week_no,day_no) VALUES('2022-03-14 00:00:00',1,2,1)" ,
            "INSERT INTO workouts (completion_date,routine_id,week_no,day_no) VALUES('2022-03-15 00:00:00',1,2,2)" ,
            "INSERT INTO workouts (completion_date,routine_id,week_no,day_no) VALUES('2022-03-17 00:00:00',1,2,3)" ,
            "INSERT INTO workouts (completion_date,routine_id,week_no,day_no) VALUES('2022-03-18 00:00:00',1,2,4)" ,
            "INSERT INTO workouts (completion_date,routine_id,week_no,day_no) VALUES('2022-03-24 00:00:00',1,3,3)" ,
            "INSERT INTO workouts (completion_date,routine_id,week_no,day_no) VALUES('2022-03-25 00:00:00',1,3,4)" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(1,8,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(1,9,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(1,10,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(1,11,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(1,12,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(2,13,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(2,14,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(2,15,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(2,16,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(2,17,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(2,18,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(2,19,'Used machine')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(3,20,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(3,21,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(3,22,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(3,23,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(3,25,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(4,1,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(4,2,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(4,3,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(4,4,'Used Iso Lateral PD machine')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(4,5,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(4,6,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(4,7,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(5,8,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(5,9,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(5,10,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(5,11,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(5,12,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(6,13,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(6,14,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(6,15,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(6,16,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(6,17,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(6,18,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(6,19,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(7,20,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(7,21,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(7,22,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(7,23,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(7,24,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(7,25,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(8,13,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(8,14,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(8,15,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(8,16,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(8,17,'Should reduce momentum as much as possible')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(8,18,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(8,19,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(9,20,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(9,21,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(9,22,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(9,23,'')" ,
            "INSERT INTO workout_exercises (workout_id,exercise_id,note) VALUES(9,24,'')" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,8,1,5,43.6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,9,1,5,60)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,10,1,6,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,11,1,10,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,12,1,10,55)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,13,1,12,30)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,14,1,12,8)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,15,1,12,13)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,16,1,12,12)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,17,1,12,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,18,1,12,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,19,1,12,20)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,20,1,12,33)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,21,1,12,10)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,22,1,12,19)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,23,1,15,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,25,1,12,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,1,1,5,35)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,2,1,8,12)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,3,1,5,40)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,4,1,8,60)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,5,1,7,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,6,1,10,2.5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,7,1,10,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,8,1,8,43.6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,9,1,4,70)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,10,1,15,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,11,1,10,32)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,12,1,12,65)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,13,1,5,35)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,14,1,12,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,15,1,12,13)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,16,1,12,14)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,17,1,12,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,18,1,12,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,19,1,12,18)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,20,1,12,33)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,21,1,12,12)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,22,1,15,26)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,23,1,15,32)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,24,1,15,55)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,25,1,12,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,13,1,12,30)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,14,1,12,7)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,15,1,12,16.5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,16,1,12,16)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,17,1,8,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,18,1,12,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,19,1,12,16)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,20,1,10,45)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,21,1,10,14)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,22,1,13,33)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,23,1,15,32)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,24,1,15,65)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,8,2,5,43.6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,9,2,5,60)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,10,2,6,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,11,2,10,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,12,2,10,55)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,13,2,10,30)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,14,2,12,8)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,15,2,8,16.5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,16,2,10,12)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,17,2,10,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,18,2,10,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,19,2,12,20)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,20,2,12,33)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,21,2,12,10)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,22,2,15,19)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,23,2,12,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,25,2,12,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,1,2,5,35)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,2,2,9,12)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,3,2,5,40)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,4,2,7,60)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,5,2,4,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,6,2,10,2.5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,7,2,10,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,8,2,5,53)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,9,2,5,70)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,10,2,13,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,11,2,9,32)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,12,2,9,65)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,13,2,8,30)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,14,2,10,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,15,2,10,16.5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,16,2,10,14)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,17,2,10,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,18,2,12,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,19,2,10,18)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,20,2,12,43)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,21,2,12,12)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,22,2,13,26)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,23,2,10,32)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,24,2,12,65)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,25,2,12,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,13,2,9,30)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,14,2,12,8)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,15,2,10,16.5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,16,2,9,16)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,17,2,12,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,18,2,12,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,19,2,12,18)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,20,2,12,45)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,21,2,14,14)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,22,2,13,26)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,23,2,10,32)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,24,2,12,65)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,8,3,5,43.6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,9,3,5,70)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,10,3,6,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,11,3,10,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(1,12,3,10,55)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,13,3,8,30)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,14,3,12,8)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,15,3,12,13)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,16,3,12,10)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,17,3,12,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,18,3,8,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,19,3,12,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,20,3,12,33)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,21,3,12,10)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,22,3,15,19)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,23,3,10,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(3,25,3,12,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,1,3,5,40)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,2,3,7,12)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,3,3,5,40)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,4,3,6,60)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,5,3,4,20)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,6,3,9,2.5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,7,3,6,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,8,3,5,53)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,9,3,3,70)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,10,3,13,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,11,3,7,32)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,12,3,10,65)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,13,3,5,30)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,14,3,10,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,15,3,9,16.5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,16,3,12,14)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,17,3,10,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,18,3,9,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,19,3,8,18)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,20,3,10,43)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,21,3,10,12)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,22,3,11,26)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,23,3,9,32)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,24,3,10,65)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(7,25,3,12,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,13,3,8,30)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,14,3,12,8)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,15,3,10,16.5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,16,3,10,14)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,17,3,12,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,18,3,11,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,19,3,12,18)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,20,3,12,50)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,21,3,10,14)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,22,3,12,26)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,23,3,12,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,24,3,12,75)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,14,4,12,8)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,17,4,10,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,18,4,10,4)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(2,19,4,10,32)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,1,4,5,40)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(4,3,4,5,40)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,8,4,5,53)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(5,11,4,8,25)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,13,4,6,30)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,14,4,10,6)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,15,4,10,13)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(6,19,4,7,18)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,14,4,14,8)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,15,4,9,13)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,16,4,10,14)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,17,4,11,5)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(8,19,4,11,18)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,20,4,8,55)" ,
            "INSERT INTO workout_exercise_sets (workout_id,exercise_id,set_no,reps,weight) VALUES(9,24,4,10,75)"
            )
    }

    fun getExercises(): ArrayList<Exercise>{
        val exerciseList: ArrayList<Exercise> = ArrayList()
        val selectQuery = "SELECT " +
                "e.${BaseColumns._ID} e_id, " +
                "e.${Exercises.Columns.TITLE} e_title, " +
                "m.${BaseColumns._ID} m_id, " +
                "m.${MuscleGroups.Columns.TITLE} m_title " +
                "FROM ${Exercises.TABLE_NAME} e " +
                "LEFT JOIN ${MuscleGroups.TABLE_NAME} m " +
                "ON m.${BaseColumns._ID} = e.${Exercises.Columns.MUSCLE_GRP_ID} "
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            return ArrayList()
        }
        var eId: Long
        var eTitle: String
        var mId: Int
        var mTitle: String
        if(cursor.moveToFirst()){
            do{
                eId = cursor.getLong(cursor.getColumnIndexOrThrow("e${BaseColumns._ID}"))
                eTitle = cursor.getString(cursor.getColumnIndexOrThrow("e_${Exercises.Columns.TITLE}"))
                mId = cursor.getInt(cursor.getColumnIndexOrThrow("m${BaseColumns._ID}"))
                mTitle = cursor.getString(cursor.getColumnIndexOrThrow("m_${MuscleGroups.Columns.TITLE}"))
                val exercise = Exercise(eId, eTitle, MuscleGroup(mId, mTitle))
                exerciseList.add((exercise))
            } while(cursor.moveToNext())
        }
        cursor.close()

        return exerciseList
    }

    fun getAllRoutines(): ArrayList<Routine>{
        val routineList: ArrayList<Routine> = ArrayList()
        val selectQuery = "SELECT * FROM ${Routines.TABLE_NAME}"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            return ArrayList()
        }
        var id: Long
        var title: String
        var week: Int
        if(cursor.moveToFirst()){
            do{
                id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                title = cursor.getString(cursor.getColumnIndexOrThrow(Routines.Columns.TITLE))
                week = cursor.getInt(cursor.getColumnIndexOrThrow(Routines.Columns.WEEKS))
                val routine = Routine(id, title, week, mutableListOf())
                routineList.add((routine))
            } while(cursor.moveToNext())
        }
        cursor.close()

        return routineList
    }

    fun getRoutine(id: Long): Routine?{
        val selectQuery = "SELECT " +
                "rt.${BaseColumns._ID} as rt_id, " +
                "rt.${Routines.Columns.TITLE} as rt_title, " +
                "rt.${Routines.Columns.WEEKS} as rt_weeks, " +
                "ed.${BaseColumns._ID} as ed_id, " +
                "ed.${ExerciseDays.Columns.DAY}, " +
                "e.${BaseColumns._ID} as e_id, " +
                "e.${ExerciseSelections.Columns.EXERCISE_DAY_ID}, " +
                "e.${ExerciseSelections.Columns.ORDER_NO}, " +
                "e.${ExerciseSelections.Columns.SETS_MIN}, " +
                "e.${ExerciseSelections.Columns.SETS_UPPER}, " +
                "e.${ExerciseSelections.Columns.REPS}, " +
                "exc.${BaseColumns._ID} as exc_id, " +
                "exc.${Exercises.Columns.TITLE} as exc_title, " +
                "mus.${BaseColumns._ID} as m_id, " +
                "mus.${MuscleGroups.Columns.TITLE} as m_title " +
                "FROM ${Routines.TABLE_NAME} as rt " +
                "LEFT JOIN ${ExerciseDays.TABLE_NAME} as ed " +
                " ON rt.${BaseColumns._ID} = ed.${ExerciseDays.Columns.ROUTINE_ID} " +
                "LEFT JOIN ${ExerciseSelections.TABLE_NAME} as e " +
                " ON ed.${BaseColumns._ID} = e.${ExerciseSelections.Columns.EXERCISE_DAY_ID} " +
                "LEFT JOIN ${Exercises.TABLE_NAME} as exc" +
                " ON exc.${BaseColumns._ID} = e.${ExerciseSelections.Columns.EXERCISE_ID} " +
                "LEFT JOIN ${MuscleGroups.TABLE_NAME} AS mus" +
                " ON mus.${BaseColumns._ID} = exc.${Exercises.Columns.MUSCLE_GRP_ID} " +
                "WHERE rt.${BaseColumns._ID} = ?"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, arrayOf(id.toString()))
        } catch (e: Exception){
            e.printStackTrace()
            return null
        }

        lateinit var routine : Routine
        lateinit var currExerciseDay: ExerciseDay
        var initialized = false
        var currEdId = 0L
        var currEId = 0L

        if(cursor.moveToFirst()){
            do{
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("rt${BaseColumns._ID}"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("rt_${Routines.Columns.TITLE}"))
                val weeks = cursor.getInt(cursor.getColumnIndexOrThrow("rt_${Routines.Columns.WEEKS}"))
                val edId: Long = cursor.getLong(cursor.getColumnIndexOrThrow("ed${BaseColumns._ID}"))
                val day: Int = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseDays.Columns.DAY))
                val eId = cursor.getLong(cursor.getColumnIndexOrThrow("e${BaseColumns._ID}"))
                val eOrderNo = cursor.getInt(cursor.getColumnIndexOrThrow("${ExerciseSelections.Columns.ORDER_NO}"))
                val excId = cursor.getLong(cursor.getColumnIndexOrThrow("exc${BaseColumns._ID}"))
                val excTitle = cursor.getString(cursor.getColumnIndexOrThrow("exc_${Exercises.Columns.TITLE}"))
                val mId = cursor.getInt(cursor.getColumnIndexOrThrow("m${BaseColumns._ID}"))
                val mTitle = cursor.getString(cursor.getColumnIndexOrThrow("m_${MuscleGroups.Columns.TITLE}"))
                val setMin = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseSelections.Columns.SETS_MIN))
                val setUpper = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseSelections.Columns.SETS_UPPER))
                val reps = cursor.getString(cursor.getColumnIndexOrThrow(ExerciseSelections.Columns.REPS))

                if(!initialized){
                    initialized = true
                    routine = Routine(id, title, weeks, mutableListOf())
                }
                if(currEdId != edId){
                    currEdId = edId
                    currExerciseDay = ExerciseDay(edId, day, mutableListOf())
                    routine.exerciseDays += currExerciseDay
                }
                if(currEId != eId){
                    currEId = eId
                    val exercise = ExerciseSelection(
                        eId,
                        eOrderNo,
                        Exercise(excId,excTitle,MuscleGroup(mId,mTitle)),
                        Pair(setMin,setUpper),
                        reps
                    )
                    currExerciseDay.exerciseSelections += exercise
                }

            } while(cursor.moveToNext())
        }
        cursor.close()
        return routine
    }

    fun insertRoutine(routine: Routine): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Routines.Columns.TITLE, routine.title)
        contentValues.put(Routines.Columns.WEEKS, routine.weeks)

        val id = db.insert(Routines.TABLE_NAME, null, contentValues)
        db.close()
        for(exerciseDay in routine.exerciseDays){
            insertExerciseDay(exerciseDay, id)
        }

        return id
    }



    private fun insertExerciseDay(exerciseDay: ExerciseDay, routineId: Long): Long{
        if(exerciseDay.exerciseSelections.size == 0) return 0L

        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ExerciseDays.Columns.DAY, exerciseDay.day)
        contentValues.put(ExerciseDays.Columns.ROUTINE_ID, routineId)

        val id = db.insert(ExerciseDays.TABLE_NAME, null, contentValues)
        db.close()
        for(exercise in exerciseDay.exerciseSelections){
            insertExercise(exercise, id)
        }
        return id
    }

    private fun insertExercise(exerciseSelection: ExerciseSelection, exerciseDayId: Long): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ExerciseSelections.Columns.EXERCISE_ID, exerciseSelection.exercise.id)
        contentValues.put(ExerciseSelections.Columns.REPS, exerciseSelection.reps)
        contentValues.put(ExerciseSelections.Columns.SETS_MIN, exerciseSelection.sets.first)
        contentValues.put(ExerciseSelections.Columns.SETS_UPPER, exerciseSelection.sets.second)
        contentValues.put(ExerciseSelections.Columns.EXERCISE_DAY_ID, exerciseDayId)

        val id = db.insert(ExerciseSelections.TABLE_NAME, null, contentValues)
        db.close()
        return id
    }

    fun updateRoutine(routine: Routine){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(Routines.Columns.TITLE, routine.title)

        db.update(Routines.TABLE_NAME, contentValues, "_id = ?", arrayOf(routine.id.toString()))
        db.close()
        for(exerciseDay in routine.exerciseDays){
            if(exerciseDay.id == 0L){
                exerciseDay.id = insertExerciseDay(exerciseDay, routine.id!!)
            }else{
                updateExerciseDay(exerciseDay)
            }
        }
    }

    fun updateExerciseDay(exerciseDay: ExerciseDay){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ExerciseDays.Columns.DAY, exerciseDay.day)

        db.update(ExerciseDays.TABLE_NAME, contentValues, "_id = ?", arrayOf(exerciseDay.id.toString()))
        db.close()
        for(exercise in exerciseDay.exerciseSelections){
            if(exercise.id == 0L){
                exercise.id = insertExercise(exercise, exerciseDay.id!!)
            }else{
                updateExercises(exercise)
            }
        }
    }

    fun updateExercises(exerciseSelection: ExerciseSelection){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ExerciseSelections.Columns.EXERCISE_ID, exerciseSelection.exercise.id)
        contentValues.put(ExerciseSelections.Columns.REPS, exerciseSelection.reps)
        contentValues.put(ExerciseSelections.Columns.SETS_MIN, exerciseSelection.sets.first)
        contentValues.put(ExerciseSelections.Columns.SETS_UPPER, exerciseSelection.sets.second)

        db.update(ExerciseSelections.TABLE_NAME, contentValues, "_id = ?", arrayOf(exerciseSelection.id.toString()))
        db.close()
    }

    fun getWorkout(workout_id: Long) : Workout?{
        val selectQuery = "SELECT wo.${BaseColumns._ID} wo_id, " +
                "wo.${Workouts.Columns.COMPLETION_DATE}, " +
                "wo.${Workouts.Columns.WEEK_NO}, " +
                "wo.${Workouts.Columns.DAY_NO}, " +
                "wo_exc.${WorkoutExercises.Columns.NOTE}, " +
                "wo_exc.${WorkoutExercises.Columns.EXERCISE_ID}, " +
                "e.${BaseColumns._ID} as e_id, " +
                "e.${Exercises.Columns.TITLE} as e_title, " +
                "m.${BaseColumns._ID} as m_id, " +
                "m.${MuscleGroups.Columns.TITLE} as m_title, " +
                "exc.${ExerciseSelections.Columns.ORDER_NO}, " +
                "exc.${ExerciseSelections.Columns.SETS_MIN} as targetSetsMin, " +
                "exc.${ExerciseSelections.Columns.SETS_UPPER} as targetSetsUp, " +
                "exc.${ExerciseSelections.Columns.REPS} AS targetReps, " +
                "wo_exc_set.${WorkoutExerciseSets.Columns.SET}, " +
                "wo_exc_set.${WorkoutExerciseSets.Columns.REPS}, " +
                "wo_exc_set.${WorkoutExerciseSets.Columns.WEIGHT} " +
                "FROM ${Workouts.TABLE_NAME} as wo " +
                "LEFT JOIN ${WorkoutExercises.TABLE_NAME} as wo_exc " +
                " ON wo_exc.${WorkoutExercises.Columns.WORKOUT_ID} = wo.${BaseColumns._ID} " +
                "LEFT JOIN ${ExerciseSelections.TABLE_NAME} as exc " +
                " ON exc.${BaseColumns._ID} = wo_exc.${WorkoutExercises.Columns.EXERCISE_ID} " +
                "LEFT JOIN ${Exercises.TABLE_NAME} as e " +
                " ON e.${BaseColumns._ID} = exc.${ExerciseSelections.Columns.EXERCISE_ID} " +
                "LEFT JOIN ${MuscleGroups.TABLE_NAME} as m " +
                " ON m.${BaseColumns._ID} = e.${Exercises.Columns.MUSCLE_GRP_ID} " +
                "LEFT JOIN ${WorkoutExerciseSets.TABLE_NAME} AS wo_exc_set " +
                " ON wo_exc_set.${WorkoutExerciseSets.Columns.WORKOUT_ID} = wo.${BaseColumns._ID} " +
                " AND wo_exc_set.${WorkoutExerciseSets.Columns.EXERCISE_ID} = wo_exc.${WorkoutExercises.Columns.EXERCISE_ID} " +
                "WHERE wo.${BaseColumns._ID} = ?"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, arrayOf(workout_id.toString()))
        } catch (e: Exception){
            e.printStackTrace()
            return null
        }
        var isInitialized = false
        lateinit var workout : Workout
        var lastEid = 0L
        var lastSet = 0
        lateinit var currentExercise : WorkoutExercise
        if(cursor.moveToFirst()){
            do{
                val id = cursor.getLong(cursor.getColumnIndexOrThrow("wo_id"))
                val completionDateStr = cursor.getString(cursor.getColumnIndexOrThrow(Workouts.Columns.COMPLETION_DATE))
                val completionDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(completionDateStr)
                val weekNo = cursor.getInt(cursor.getColumnIndexOrThrow(Workouts.Columns.WEEK_NO))
                val dayNo = cursor.getInt(cursor.getColumnIndexOrThrow(Workouts.Columns.DAY_NO))
                val exerciseId = cursor.getLong(cursor.getColumnIndexOrThrow(WorkoutExercises.Columns.EXERCISE_ID))
                val orderNo = cursor.getInt(cursor.getColumnIndexOrThrow(ExerciseSelections.Columns.ORDER_NO))
                val eId = cursor.getLong(cursor.getColumnIndexOrThrow("e${BaseColumns._ID}"))
                val eTitle = cursor.getString(cursor.getColumnIndexOrThrow("e_${Exercises.Columns.TITLE}"))
                val mId = cursor.getInt(cursor.getColumnIndexOrThrow("m${BaseColumns._ID}"))
                val mTitle = cursor.getString(cursor.getColumnIndexOrThrow("m_${MuscleGroups.Columns.TITLE}"))
                val targetSetsMin = cursor.getInt(cursor.getColumnIndexOrThrow("targetSetsMin"))
                val targetSetsUp = cursor.getInt(cursor.getColumnIndexOrThrow("targetSetsUp"))
                val targetReps = cursor.getString(cursor.getColumnIndexOrThrow("targetReps"))
                val note = cursor.getString(cursor.getColumnIndexOrThrow(WorkoutExercises.Columns.NOTE))
                val set = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutExerciseSets.Columns.SET))
                val reps = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutExerciseSets.Columns.REPS))
                val weight = cursor.getFloat(cursor.getColumnIndexOrThrow(WorkoutExerciseSets.Columns.WEIGHT))

                if(!isInitialized){
                    isInitialized = true
                    workout = Workout(id, completionDate, weekNo, dayNo, mutableListOf())
                }

                if(lastEid != exerciseId){
                    lastEid = exerciseId
                    var exercise = ExerciseSelection(
                        exerciseId,
                        orderNo,
                        Exercise(eId, eTitle, MuscleGroup(mId, mTitle)),
                        Pair(targetSetsMin, targetSetsUp),
                        targetReps
                    )
                    currentExercise = WorkoutExercise(exercise, mutableListOf(), note)
                    workout.addExercise(currentExercise)
                }

                if(lastSet != set && set != 0){
                    lastSet = set
                    currentExercise.sets.add(WorkoutExerciseSet(set, weight, reps))
                }
            } while(cursor.moveToNext())
        }
        return workout
    }

    fun getPreviousWeeksWorkoutOfRoutine(routine_id: Long, workout: Workout) : Workout?{
        val selectQuery = "SELECT wo.${BaseColumns._ID} " +
                "FROM ${Workouts.TABLE_NAME} as wo " +
                "INNER JOIN ${Routines.TABLE_NAME} as rt " +
                " ON rt.${BaseColumns._ID} = wo.${Workouts.Columns.ROUTINE_ID} " +
                "WHERE rt.${BaseColumns._ID} = ?" +
                "AND wo.${Workouts.Columns.WEEK_NO} = ? " +
                "AND wo.${Workouts.Columns.DAY_NO} = ?"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery,
                arrayOf(
                    routine_id.toString(),
                    (workout.weekNo - 1).toString(),
                    workout.dayNo.toString()
                )
            )
        } catch (e: Exception){
            e.printStackTrace()
            return null
        }

        if(cursor.moveToFirst()){
            var id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
            return getWorkout(id)
        }
        return null

    }

    fun getWorkoutsOfRoutine(routine_id: Long) : MutableList<Workout>{
        var workouts : MutableList<Workout> = mutableListOf()
        val selectQuery = "SELECT wo.* " +
                "FROM ${Workouts.TABLE_NAME} as wo " +
                "INNER JOIN ${Routines.TABLE_NAME} as rt " +
                " ON rt.${BaseColumns._ID} = wo.${Workouts.Columns.ROUTINE_ID} " +
                "WHERE rt.${BaseColumns._ID} = ?"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, arrayOf(routine_id.toString()))
        } catch (e: Exception){
            e.printStackTrace()
            return workouts
        }

        if(cursor.moveToFirst()){
            do{
                var id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                var weekNo = cursor.getInt(cursor.getColumnIndexOrThrow(Workouts.Columns.WEEK_NO))
                var dayNo = cursor.getInt(cursor.getColumnIndexOrThrow(Workouts.Columns.DAY_NO))
                var completionDateStr = cursor.getString(cursor.getColumnIndexOrThrow(Workouts.Columns.COMPLETION_DATE))
                var completionDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(completionDateStr)
                val workout = Workout(id, completionDate, weekNo, dayNo, mutableListOf())
                workouts.add(workout)
            } while(cursor.moveToNext())
        }
        return workouts
    }

    fun getExerciseHistory(exerciseId: Long): ArrayList<ExerciseHistoryItem>{
        val exerciseList: ArrayList<ExerciseHistoryItem> = arrayListOf()
        val selectQuery = "SELECT " +
                "wo.${BaseColumns._ID}, " +
                "wo.${Workouts.Columns.COMPLETION_DATE}, " +
                "sets.${WorkoutExerciseSets.Columns.SET}, " +
                "sets.${WorkoutExerciseSets.Columns.REPS}, " +
                "sets.${WorkoutExerciseSets.Columns.WEIGHT} " +
                "FROM ${Workouts.TABLE_NAME} wo " +
                "LEFT JOIN ${WorkoutExerciseSets.TABLE_NAME} sets " +
                "ON sets.${WorkoutExerciseSets.Columns.WORKOUT_ID} = wo.${BaseColumns._ID} " +
                "LEFT JOIN ${ExerciseSelections.TABLE_NAME} exc_sel " +
                "ON exc_sel.${BaseColumns._ID} = sets.${WorkoutExerciseSets.Columns.EXERCISE_ID} " +
                "LEFT JOIN ${Exercises.TABLE_NAME} exc " +
                "ON exc.${BaseColumns._ID} = exc_sel.${ExerciseSelections.Columns.EXERCISE_ID} " +
                "WHERE exc.${BaseColumns._ID} = ? " +
                "ORDER BY ${Workouts.Columns.COMPLETION_DATE} DESC"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(selectQuery, arrayOf(exerciseId.toString()))
        } catch (e: Exception){
            e.printStackTrace()
            return exerciseList
        }
        var lastId = 0L
        lateinit var currExcItem : ExerciseHistoryItem
        if(cursor.moveToFirst()){
            do{
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID))
                val completionDateStr = cursor.getString(cursor.getColumnIndexOrThrow(Workouts.Columns.COMPLETION_DATE))
                val completionDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(completionDateStr)
                val setNo = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutExerciseSets.Columns.SET))
                val reps = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutExerciseSets.Columns.REPS))
                val weight = cursor.getFloat(cursor.getColumnIndexOrThrow(WorkoutExerciseSets.Columns.WEIGHT))
                val exercise = WorkoutExerciseSet(setNo, weight, reps)
                if(lastId != id){
                    currExcItem = ExerciseHistoryItem(completionDate, arrayListOf())
                    exerciseList.add(currExcItem)
                    lastId = id
                }
                currExcItem.sets.add(exercise)
            } while(cursor.moveToNext())
        }
        cursor.close()

        return exerciseList
    }

    fun updateWorkout(workout: Workout): Workout{
        for(exercise in workout.exercises){
            updateWorkoutExercise(exercise, workout.id!!)
        }
        return workout
    }

    private fun updateWorkoutExercise(workoutExercise: WorkoutExercise, workoutId: Long){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(WorkoutExercises.Columns.NOTE, workoutExercise.note)

        db.update(WorkoutExercises.TABLE_NAME,
            contentValues,
            "${WorkoutExercises.Columns.WORKOUT_ID} = ? AND ${WorkoutExercises.Columns.EXERCISE_ID} = ?",
            arrayOf(workoutId.toString(), workoutExercise.exerciseSelection.id.toString()))
        db.close()
        for(exercise in workoutExercise.sets){
            updateWorkoutExerciseSet(exercise, workoutId, workoutExercise.exerciseSelection.id!!)
        }
    }

    private fun updateWorkoutExerciseSet(workoutExerciseSet: WorkoutExerciseSet, workoutId: Long, exerciseId: Long){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(WorkoutExerciseSets.Columns.REPS, workoutExerciseSet.reps)
        contentValues.put(WorkoutExerciseSets.Columns.WEIGHT, workoutExerciseSet.weight)

        val rows = db.update(WorkoutExerciseSets.TABLE_NAME,
            contentValues,
            "${WorkoutExerciseSets.Columns.WORKOUT_ID} = ? " +
                    "AND ${WorkoutExerciseSets.Columns.EXERCISE_ID} = ? " +
                    "AND ${WorkoutExerciseSets.Columns.SET} = ?",
            arrayOf(workoutId.toString(), exerciseId.toString(), workoutExerciseSet.set.toString()))

        if(rows == 0){
            insertWorkoutExerciseSet(workoutExerciseSet, workoutId, exerciseId)
        }
        db.close()
    }

    fun insertWorkout(workout: Workout, routine_id: Long): Workout{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        val completionDateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(workout.completionDate)
        contentValues.put(Workouts.Columns.COMPLETION_DATE, completionDateStr)
        contentValues.put(Workouts.Columns.ROUTINE_ID, routine_id)
        contentValues.put(Workouts.Columns.WEEK_NO, workout.weekNo)
        contentValues.put(Workouts.Columns.DAY_NO, workout.dayNo)

        val id = db.insert(Workouts.TABLE_NAME, null, contentValues)
        db.close()
        for(exercise in workout.exercises){
            insertWorkoutExercise(exercise, id)
        }
        workout.id = id
        return workout
    }

    private fun insertWorkoutExercise(exercise: WorkoutExercise, workoutId: Long){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(WorkoutExercises.Columns.WORKOUT_ID, workoutId)
        contentValues.put(WorkoutExercises.Columns.EXERCISE_ID, exercise.exerciseSelection.id)
        contentValues.put(WorkoutExercises.Columns.NOTE, exercise.note)
        db.insert(WorkoutExercises.TABLE_NAME, null, contentValues)
        for( set in exercise.sets ){
            insertWorkoutExerciseSet(set, workoutId, exercise.exerciseSelection.id!!)
        }
        db.close()
    }

    private fun insertWorkoutExerciseSet(exerciseSet: WorkoutExerciseSet, workoutId: Long, exerciseId: Long){
        val db = this.writableDatabase
        if(exerciseSet.weight == 0F) return
        val contentValues = ContentValues()
        contentValues.put(WorkoutExerciseSets.Columns.WORKOUT_ID, workoutId)
        contentValues.put(WorkoutExerciseSets.Columns.EXERCISE_ID, exerciseId)
        contentValues.put(WorkoutExerciseSets.Columns.SET, exerciseSet.set)
        contentValues.put(WorkoutExerciseSets.Columns.REPS, exerciseSet.reps)
        contentValues.put(WorkoutExerciseSets.Columns.WEIGHT, exerciseSet.weight)
        db.insert(WorkoutExerciseSets.TABLE_NAME, null, contentValues)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_USERS)
        db?.execSQL(SQL_CREATE_TABLE_ROUTINES)
        db?.execSQL(SQL_CREATE_TABLE_MUSCLE_GROUPS)
        db?.execSQL(SQL_CREATE_TABLE_EXERCISE_DAYS)
        db?.execSQL(SQL_CREATE_TABLE_EXERCISES)
        db?.execSQL(SQL_CREATE_TABLE_EXERCISE_SELECTIONS)
        db?.execSQL(SQL_CREATE_TABLE_WORKOUTS)
        db?.execSQL(SQL_CREATE_TABLE_WORKOUT_EXERCISE)
        db?.execSQL(SQL_CREATE_TABLE_WORKOUT_EXERCISE_SET)
        db?.execSQL(SQL_INSERT_TABLE_ROUTINES)
        for(insertQry in SQL_INSERT_TABLE_EXERCISE_DAYS ) db?.execSQL(insertQry)
        for(insertQry in SQL_INSERT_TABLE_MUSCLE_GROUPS ) db?.execSQL(insertQry)
        for(insertQry in SQL_INSERT_TABLE_EXERCISES ) db?.execSQL(insertQry)
        for(insertQry in SQL_INSERT_TABLE_EXERCISE_SELECTIONS ) db?.execSQL(insertQry)

        //tmp
        for(insertQry in SQL_INSERT_TABLE_WORKOUTS ) db?.execSQL(insertQry)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS ${Users.TABLE_NAME}")
        db!!.execSQL("DROP TABLE IF EXISTS ${Routines.TABLE_NAME}")
        db!!.execSQL("DROP TABLE IF EXISTS ${ExerciseDays.TABLE_NAME}")
        db!!.execSQL("DROP TABLE IF EXISTS ${MuscleGroups.TABLE_NAME}")
        db!!.execSQL("DROP TABLE IF EXISTS ${Exercises.TABLE_NAME}")
        db!!.execSQL("DROP TABLE IF EXISTS ${ExerciseSelections.TABLE_NAME}")
        db!!.execSQL("DROP TABLE IF EXISTS ${Workouts.TABLE_NAME}")
        db!!.execSQL("DROP TABLE IF EXISTS ${WorkoutExercises.TABLE_NAME}")
        db!!.execSQL("DROP TABLE IF EXISTS ${WorkoutExerciseSets.TABLE_NAME}")
        onCreate(db)
    }
}