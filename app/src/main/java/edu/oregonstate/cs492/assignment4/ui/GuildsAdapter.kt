package edu.oregonstate.cs492.assignment4.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.GuildInformation
import edu.oregonstate.cs492.assignment4.data.SkillInformation

class GuildsAdapter(): RecyclerView.Adapter<GuildsAdapter.ViewHolder>() {
    private var guildList = listOf<String>()
    private val tag = "GuildsAdapter"
    fun updateGuildsList(newGuildList:List<GuildInformation?>?){
        notifyItemRangeRemoved(0,guildList.size)
        val newNames:MutableList<String> = mutableListOf()
        if (newGuildList != null) {
            for(i in 0 ..  newGuildList.size-1){
                newGuildList[i]?.let { newNames.add(it.name) }
                newGuildList[i]?.let { Log.d(tag, it.name) }
                Log.d(tag,"In the for loop")
            }
        }

        guildList = newNames
        notifyItemRangeInserted(0,guildList.size)
        Log.d(tag, "Received guilds: " + newGuildList)
    }
    override fun getItemCount(): Int {
        return guildList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(guildList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.guild_list_item, parent, false)
        Log.d(tag, "In the create viewholder")
        return ViewHolder(view)
    }



    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        private var currentGuild:String? = null
        private var name:TextView = itemView.findViewById(R.id.guildDisplay)
        fun bind(currentGuildIn:String){
            currentGuild = currentGuildIn
            name.text = currentGuildIn
        }
    }
}