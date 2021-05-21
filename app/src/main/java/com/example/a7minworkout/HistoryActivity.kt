package com.example.a7minworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(findViewById(R.id.toolbar_history_activity))

    val toolbar = findViewById<Toolbar>(R.id.toolbar_history_activity)
        val actionbar = supportActionBar
           if(actionbar !=null) {
               actionbar.setDisplayHomeAsUpEnabled(true)
               supportActionBar?.setTitle("History")


           }
      getAllCompletedDates()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true

    }
    private fun getAllCompletedDates() {

        // Instance of the Sqlite Open Helper class.
        val tvHistory = findViewById<TextView>(R.id.tvHistory)
        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        val tvNoDataAvailable = findViewById<TextView>(R.id.tvNoDataAvailable)
        val dbHandler = SqliteOpenHelper(this, null)

        val allCompletedDatesList =
                dbHandler.getAllCompletedDatesList() // List of history table data

        // TODO(Step 3 : Now here the dates which were printed in log.
        //  We will pass that list to the adapter class which we have created and bind it to the recycler view.)
        // START
        if (allCompletedDatesList.size > 0) {
            // Here if the List size is greater then 0 we will display the item in the recycler view or else we will show the text view that no data is available.
            tvHistory.visibility = View.VISIBLE
            rvHistory.visibility = View.VISIBLE
            tvNoDataAvailable.visibility = View.GONE

            // Creates a vertical Layout Manager
            rvHistory.layoutManager = LinearLayoutManager(this)

            // History adapter is initialized and the list is passed in the param.
            val historyAdapter = HistoryAdaptor(this, allCompletedDatesList)

            // Access the RecyclerView Adapter and load the data into it
            rvHistory.adapter = historyAdapter
        } else {
            tvHistory.visibility = View.GONE
            rvHistory.visibility = View.GONE
            tvNoDataAvailable.visibility = View.VISIBLE
        }
        // END
    }
}