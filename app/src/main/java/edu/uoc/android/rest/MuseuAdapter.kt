package edu.uoc.android.rest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.uoc.android.R
import edu.uoc.android.models.Museums
import kotlinx.android.synthetic.main.list_item.view.*
import retrofit2.Call

class MuseuAdapter(val items : MutableList<Museubanner>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.tvMuseus?.text = items[position].nom
        Picasso.get().load(items[position].url).into(holder?.ivMuseus)
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvMuseus = view.tv_museums
    val ivMuseus = view.iv_museums
}