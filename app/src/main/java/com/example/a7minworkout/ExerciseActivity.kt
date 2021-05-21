package com.example.a7minworkout

import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ExerciseActivity : AppCompatActivity() ,TextToSpeech.OnInitListener{
    private  var tts : TextToSpeech ?= null

    private var restTimer : CountDownTimer?=null
    private  var restProgress = 0

    private  var exerciseList : ArrayList<ExerciseModel> ?= null
    private  var  currentExercisePosition : Int = -1

    private lateinit var mAdapter: AdaptorClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

//       val toolbar = findViewById<Toolbar>(R.id.toolbar_exercise_activity)
       setSupportActionBar(findViewById(R.id.toolbar_exercise_activity))
        val actionbar = supportActionBar

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setTitle("Exercises")
        }


        tts = TextToSpeech(this, this)

        exerciseList = Constants.defaultExerciseList()
        setUpRestView()
        mAdapter = AdaptorClass(this, exerciseList!!)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = mAdapter

    }



    override fun onSupportNavigateUp(): Boolean {

        dialogBox()

        return true
    }



    override fun onDestroy() {
        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if (restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }
        super.onDestroy()
    }


  private  fun setRestProgressBar(){
      var progressBar = findViewById<ProgressBar>(R.id.progressBar)
      val imageView = findViewById<ImageView>(R.id.ivImage)
      imageView.visibility = View.GONE
      var tvText = findViewById<TextView>(R.id.tvText)

      val tvTextexer = findViewById<TextView>(R.id.tvText3)
      tvTextexer.visibility =View.VISIBLE
      val tvTextup = findViewById<TextView>(R.id.tvText2)
      tvTextup.visibility = View.VISIBLE
      tvTextexer.text = exerciseList!![currentExercisePosition + 1].getname()
      tvText.text = "GET READY FOR"

      restProgress=0
      progressBar.max =10
      progressBar.progress =restProgress
      val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.press_start)
      mediaPlayer.start()
      val tvtext1 : String
      tvtext1 = "Exercise Completed And "
      val tvtext12 : String = "Next exercise"
      speakout(tvtext1)
      val tospeak = tvText.text.toString()
      speakout(tospeak)
      speakout(tvtext12)
      speakout(exerciseList!![currentExercisePosition+1].getname())


      restTimer = object : CountDownTimer(10000, 1000){

          override fun onTick(millisUntilFinished: Long) {
             restProgress++
             progressBar.progress = 10-restProgress
              var tvTimer = findViewById<TextView>(R.id.tvTimer)
              tvTimer.text = (10-restProgress).toString()
          }

          override fun onFinish() {


               exerciseList!![currentExercisePosition+1].setisSelected(true)
              mAdapter.notifyDataSetChanged()
              val imageView = findViewById<ImageView>(R.id.ivImage)
              imageView.visibility = View.VISIBLE
              val tvTextexer = findViewById<TextView>(R.id.tvText3)
              tvTextexer.visibility = View.GONE
              val tvTextup = findViewById<TextView>(R.id.tvText2)
              tvTextup.visibility = View.GONE
              currentExercisePosition++
              restProgress = 0
              imageView.setImageResource(exerciseList!![currentExercisePosition].getimage())
              speakout(exerciseList!![currentExercisePosition].getname())
              progressBar.progress =restProgress
              progressBar.max = 30
              var tvText = findViewById<TextView>(R.id.tvText)

              tvText.text = exerciseList!![currentExercisePosition].getname()

             restTimer = object : CountDownTimer(30000, 1000){
                 override fun onTick(millisUntilFinished: Long) {
                    restProgress++
                     progressBar.progress = 30-restProgress
                     var tvTimer = findViewById<TextView>(R.id.tvTimer)
                     tvTimer.text = (30-restProgress).toString()
                 }

                 override fun onFinish() {
                     if(currentExercisePosition <exerciseList!!.size -1){
                         exerciseList!![currentExercisePosition].setisSelected(false)
                         exerciseList!![currentExercisePosition].setisCompleted(true)
                         mAdapter.notifyDataSetChanged()
                         setUpRestView()
                     }
                     else{
                         Toast.makeText(
                             this@ExerciseActivity,
                             "Exercises Completed",
                             Toast.LENGTH_SHORT
                         ).show()
                         addDateToDatabase()
                         val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                         startActivity(intent)

                     }
                 }
             }.start()
          }
      }.start()
  }


    private fun setUpRestView(){
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }
        setRestProgressBar()
    }

    override fun onInit(status: Int) {
       if (status == TextToSpeech.SUCCESS){
           val result = tts!!.setLanguage(Locale.UK)
           if(status == TextToSpeech.LANG_MISSING_DATA || status == TextToSpeech.LANG_NOT_SUPPORTED){
               Log.e("tts", "The language specified is not supported")
           }
       }
        else{
            Log.e("tts ", "Initialization failed")
       }
    }

    private  fun speakout(text: String){
        tts!!.setPitch(0.5f)
        tts!!.setSpeechRate(0.9f)
        tts!!.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

   fun dialogBox (){

       val alertDialog = AlertDialog.Builder(this)
       alertDialog.setMessage("Do You Want To go back")

       alertDialog.setPositiveButton("Yes"){ dialogInterface, which ->
          onBackPressed()
       }
       alertDialog.setNegativeButton("No"){ dialogInterface: DialogInterface, i: Int ->
           Toast.makeText(this, "Good Choice", Toast.LENGTH_SHORT).show()
       }
      alertDialog.create()
        alertDialog.show()

   }
    private fun addDateToDatabase() {

        val c = Calendar.getInstance() // Calender Current Instance
        val dateTime = c.time // Current Date and Time of the system.
        Log.e("Date : ", "" + dateTime) // Printed in the log.

        /**
         * Here we have taken an instance of Date Formatter as it will format our
         * selected date in the format which we pass it as an parameter and Locale.
         * Here I have passed the format as dd MMM yyyy HH:mm:ss.
         *
         * The Locale : Gets the current value of the default locale for this instance
         * of the Java Virtual Machine.
         */
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault()) // Date Formatter
        val date = sdf.format(dateTime) // dateTime is formatted in the given format.
        Log.e("Formatted Date : ", "" + date) // Formatted date is printed in the log.

        // Instance of the Sqlite Open Helper class.
        val dbHandler = SqliteOpenHelper(this, null)
        dbHandler.addDate(date) // Add date function is called.
        Log.e("Date : ", "Added...") // Printed in log which is printed if the complete execution is done.
    }

}