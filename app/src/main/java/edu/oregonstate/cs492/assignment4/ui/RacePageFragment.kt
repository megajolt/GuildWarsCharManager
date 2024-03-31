package edu.oregonstate.cs492.assignment4.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.RaceInformationViewModel
import edu.oregonstate.cs492.assignment4.data.RacialInformation
import edu.oregonstate.cs492.assignment4.data.SkillInformation
import edu.oregonstate.cs492.assignment4.data.SkillInformationViewModel

class RacePageFragment : Fragment(R.layout.fragment_race_page) {
    private val args : RacePageFragmentArgs by navArgs()
    private val viewModel:RaceInformationViewModel by viewModels()
    private val charSkillsViewModel: SkillInformationViewModel by viewModels()
    private val tag = "RacePageFragment"
    val adapter= RaceInfoAdapter(::onSkillClick)

    private lateinit var body: LinearLayout
    private lateinit var loadingProgressBar: CircularProgressIndicator
    private lateinit var errorTV:TextView
    private lateinit var raceIV:ImageView
    private lateinit var raceTV:TextView
    private lateinit var descriptionTV:TextView
    private lateinit var raceRV:RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body = view.findViewById(R.id.raceinfobody)
        errorTV = view.findViewById(R.id.raceinfoerror)
        loadingProgressBar = view.findViewById(R.id.raceinfoloading)
        raceIV = view.findViewById(R.id.race_icon)
        raceTV = view.findViewById(R.id.race_title)
        descriptionTV = view.findViewById(R.id.race_description)

        raceRV = view.findViewById(R.id.rv_race_skill_list)
        raceRV.layoutManager = LinearLayoutManager(requireContext())
        raceRV.setHasFixedSize(true)
        raceRV.adapter=adapter

        viewModel.loading.observe(viewLifecycleOwner){loading->
            if(loading!=null){
                Log.d(tag,"loading")
                body.visibility = View.INVISIBLE
                errorTV.visibility=View.INVISIBLE
                loadingProgressBar.visibility=View.VISIBLE
            }
        }

        viewModel.raceInfo.observe(viewLifecycleOwner){raceInfo->
            if(raceInfo!=null){
                bind(raceInfo)
                body.visibility = View.VISIBLE
                errorTV.visibility = View.INVISIBLE
                loadingProgressBar.visibility = View.INVISIBLE
            }
        }
        viewModel.error.observe(viewLifecycleOwner){error->
            if (error!=null){
                Log.d(tag,"error")
                body.visibility = View.INVISIBLE
                errorTV.visibility = View.VISIBLE
                loadingProgressBar.visibility = View.INVISIBLE
            }
        }
    }
    private fun bind(raceInfo:RacialInformation){
        var raceIconPath = raceIconSelect(raceInfo.id)
        var raceDesc = raceDescSelect(raceInfo.id)
        raceTV.text = raceInfo.id
        descriptionTV.text = raceDesc
        var skillsQuery:String=skillsQuerySetup(raceInfo.skills)
        Log.d(tag,skillsQuery)
        charSkillsViewModel.loadSkillInformation_VM(skillsQuery)
        charSkillsViewModel.skillInfo.observe(viewLifecycleOwner){
                skillInfo->
            if(skillInfo!=null){
                var skillList: List<SkillInformation> = arraySort(skillInfo)
                adapter.updateSkillList(skillList)
            }
                Log.d(tag,charSkillsViewModel.skillInfo.value.toString())
        }
        Glide.with(this)
            .load(raceIconPath)
            .into(raceIV)
    }
    private fun arraySort(skillInfo: List<SkillInformation>): List<SkillInformation> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val sortMethod = prefs.getString(getString(R.string.prefs_skillsort_key), "alphabetical")

        return when (sortMethod) {
            "alphabetical" -> skillInfo.sortedBy { it.name }
            "range" -> skillInfo.sortedBy { skill ->
                val rangeValue = skill.facts?.find { it.type == "range" }?.value as? Int
                rangeValue ?: -1
            }
            else -> skillInfo.sortedBy { skill ->
                val rechargeValue = skill.facts?.find { it.type == "recharge" }?.value as? Int
                rechargeValue ?: -1
            }
        }
    }
    private fun skillsQuerySetup(skills: List<String>): String {
        var skillsQuery:String = skills[0]
        skillsQuery=skillsQuery.plus(",")
        for(i in skills){
            skillsQuery=skillsQuery.plus(i)
            skillsQuery=skillsQuery.plus(",")
        }
        return skillsQuery
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadRaceInformation_VM(args.id)
    }
    private fun raceIconSelect(race: String): String {
        var raceIconPath:String
        if(race=="Asura"){
            raceIconPath="https://wiki.guildwars2.com/images/1/1f/Asura_tango_icon_20px.png"
        }
        else if(race=="Charr"){
            raceIconPath="https://wiki.guildwars2.com/images/f/fa/Charr_tango_icon_20px.png"
        }
        else if (race == "Human"){
            raceIconPath="https://wiki.guildwars2.com/images/e/e1/Human_tango_icon_20px.png"
        }
        else if(race == "Norn"){
            raceIconPath="https://wiki.guildwars2.com/images/3/3d/Norn_tango_icon_20px.png"
        }
        else{
            raceIconPath="https://wiki.guildwars2.com/images/2/29/Sylvari_tango_icon_20px.png"
        }
        return raceIconPath
    }
    private fun raceDescSelect(race: String): String {
        var raceDesc:String
        if(race=="Asura"){
            raceDesc="These alchemagical inventors may be short in stature, but they're intellectual giants. Among the asura, it's not the strong who survive, but the clever. Other races believe they should rule by virtue of their power and strength, but they're deluding themselves. In due time, all will serve the asura."
        }
        else if(race=="Charr"){
            raceDesc="The charr race was forged in the merciless crucible of war. It is all they know. War defines them, and their quest for dominion drives them ever onward. The weakling and the fool have no place among the charr. Victory is all that matters, and it must be achieved by any means and at any cost."
        }
        else if (race == "Human"){
            raceDesc="Humans have lost their homeland, their security, and their former glory. Even their gods have withdrawn. And yet, the human spirit remains unshaken. These brave defenders of Kryta continue to fight with every ounce of their strength."
        }
        else if(race == "Norn"){
            raceDesc="This race of towering hunters experienced a great defeat when the Ice Dragon drove them from their glacial homeland. Nevertheless, they won't let one lost battle—however punishing—dampen their enthusiasm for life and the hunt. They know that only the ultimate victor achieves legendary rewards."
        }
        else{
            raceDesc="Sylvari are not born. They awaken beneath the Pale Tree with knowledge gleaned in their pre-life Dream. These noble beings travel, seeking adventure and pursuing quests. They struggle to balance curiosity with duty, eagerness with chivalry, and warfare with honor. Magic and mystery entwine to shape the future of this race that has so recently appeared."
        }
        return raceDesc
    }
    fun onSkillClick(skillInformation: SkillInformation) {
        val directions = RacePageFragmentDirections.navToSkillpage(skillInformation.id.toString())
        findNavController().navigate(directions)
    }
}


