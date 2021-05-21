package com.example.a7minworkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class AdaptorClass(  private val context: Context , private val items: ArrayList<ExerciseModel>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyler_view,parent,false)
    return  ViewHolder(view)
    }


     override fun onBindViewHolder(holder: ViewHolder, position: Int) {


         val model: ExerciseModel = items[position]

         holder.tvitem.text = model.getid().toString()
         if(model.getisSelected()==true){
             holder.tvitem.background = ContextCompat.getDrawable( context, R.drawable.item_recycler_view_color_change)
         }
     if(model.getisCompleted()==true){
         holder.tvitem.background = ContextCompat.getDrawable( context, R.drawable.item_recycler_view_color_change)
     }



    }

    override fun getItemCount(): Int {
       return  items.size
    }
}
class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
     var tvitem = itemView.findViewById<TextView>(R.id.tvItem)

}

