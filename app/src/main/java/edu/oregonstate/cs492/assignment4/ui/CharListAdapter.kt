package edu.oregonstate.cs492.assignment4.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.CharacterInformation
import edu.oregonstate.cs492.assignment4.data.GuildInformation
import edu.oregonstate.cs492.assignment4.data.SkillInformation

class CharListAdapter(
    private val onCharClick:(CharacterInformation) -> Unit
): RecyclerView.Adapter<CharListAdapter.ViewHolder>() {
    private var charList: List<CharacterInformation?>? = mutableListOf<CharacterInformation?>()
    private val tag = "CharListAdapter"
    fun updateCharactersList(newCharacterList:List<CharacterInformation?>?){
        charList?.let { notifyItemRangeRemoved(0, it.size) }

//        val newNames:MutableList<String> = mutableListOf()
//        if (newCharacterList != null) {
//            for(i in 0 ..  newCharacterList.size-1){
//                newCharacterList[i]?.let { newNames.add(it.name) }
//                newCharacterList[i]?.let { Log.d(tag, it.name) }
//                Log.d(tag,"In the for loop in Charlist")
//            }
//        }

        charList = newCharacterList
        charList?.let { notifyItemRangeInserted(0, it.size) }
        Log.d(tag, "Received characters: " + newCharacterList)
    }
    override fun getItemCount(): Int {
        return charList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(charList?.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.char_list_item, parent, false)
        Log.d(tag, "In the create viewholder")
        return ViewHolder(view, onCharClick)
    }



    class ViewHolder(itemView:View, onCharClick: (CharacterInformation) -> Unit) : RecyclerView.ViewHolder(itemView){
        private var currentChar:CharacterInformation? = null
        private var name:TextView = itemView.findViewById(R.id.CharName)
        private var className:TextView = itemView.findViewById(R.id.Class)
        private var level:TextView = itemView.findViewById(R.id.Level)
        private val tag ="ViewHolder-CharListAdapter"

        init{
            itemView.setOnClickListener{
                currentChar?.let(onCharClick)
            }
        }
        fun bind(currentCharIn:CharacterInformation?){
            currentChar = currentCharIn
            name.text = currentChar?.name
            level.text = currentChar?.level.toString()
            className.text = currentChar?.race

            Log.d(tag, "Race: ${currentChar?.race}, Level: ${currentChar?.level}")
            Log.d(tag, "Race: ${className.text}, Level: ${level.text}")
        }
    }
}