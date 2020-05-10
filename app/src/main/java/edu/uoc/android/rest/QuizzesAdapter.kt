package edu.uoc.android.rest

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.uoc.android.R

import kotlinx.android.synthetic.main.list_quizzes.view.*

class QuizzesAdapter(val items : MutableList<QuestionQuizzes>, val context: Context) : RecyclerView.Adapter<ViewHolder2>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        if (items != null) {
            return items.size
        } else{
            return 0
        }
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2 {
        return ViewHolder2(LayoutInflater.from(context).inflate(R.layout.list_quizzes, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder2, position: Int) {
        holder.tvtitle.text = items?.get(position)?.title
        holder.tvchoice1.text = items?.get(position)?.choice1
        holder.tvchoice2.text = items?.get(position)?.choice2
        if (items?.get(position)?.rightChoice == "0") {
            holder?.tvchoice1.setTypeface(null,Typeface.BOLD)
        } else {
            holder?.tvchoice2.setTypeface(null,Typeface.BOLD)
        }
        Picasso.get().load(items?.get(position)?.image).into(holder?.ivimage)
    }

}


class ViewHolder2 (view: View) : RecyclerView.ViewHolder(view) {

    // Holds the TextView that will add each animal to
    val tvtitle = view.tv_Quizzes_title
    val tvchoice1 = view.tv_Quizzes_choice1
    val tvchoice2 = view.tv_Quizzes_choice2
    val ivimage = view.iv_Quizes_image


}