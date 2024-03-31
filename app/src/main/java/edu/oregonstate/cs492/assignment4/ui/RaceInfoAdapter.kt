package edu.oregonstate.cs492.assignment4.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.SkillInformation

class RaceInfoAdapter(
    private val onSkillClick: (SkillInformation) -> Unit
): RecyclerView.Adapter<RaceInfoAdapter.ViewHolder>() {
    private var skillList= listOf<SkillInformation>()

    fun updateSkillList(newSkillList:List<SkillInformation>?){
        notifyItemRangeRemoved(0,skillList.size)
        skillList = newSkillList ?: listOf()
        notifyItemRangeInserted(0,skillList.size)
    }
    override fun getItemCount(): Int {
        return skillList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.race_skill_list_item, parent, false)
        return ViewHolder(view,onSkillClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(skillList[position])
    }
    class ViewHolder(itemView: View, onSkillClick: (SkillInformation) -> Unit):RecyclerView.ViewHolder(itemView) {
        private var currentSkillItem:SkillInformation?=null
        private var name: TextView = itemView.findViewById(R.id.skill_title)
        private var icon: ImageView = itemView.findViewById(R.id.skill_icon)

        init{
            itemView.setOnClickListener{
                currentSkillItem?.let(onSkillClick)
            }
        }
        fun bind(skillItem:SkillInformation){
            currentSkillItem = skillItem
            name.text=skillItem.name
            Glide.with(itemView)
                .load(skillItem.icon)
                .into(icon)
        }
    }
}