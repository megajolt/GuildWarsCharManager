package edu.oregonstate.cs492.assignment4.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.CharacterDetailInformationViewModel
import edu.oregonstate.cs492.assignment4.data.CharacterInformation
import edu.oregonstate.cs492.assignment4.data.SkillInformation
import edu.oregonstate.cs492.assignment4.data.SkillInformationViewModel


class CharacterDetailPageFragment : Fragment(R.layout.fragment_character_detail_page) {
    private val viewModel:CharacterDetailInformationViewModel by viewModels()
    private val charSkillsViewModel:SkillInformationViewModel by viewModels()
    private val args:CharacterDetailPageFragmentArgs by navArgs()
    private val tag = "CharacterDetailPageFragment"

    val adapter=CharacterDetailsAdapter(::onSkillClick)


    private lateinit var nameTextView: TextView
    private lateinit var levelTextView: TextView
    private lateinit var classTextView: TextView
    private lateinit var raceTextView: TextView
    private lateinit var body:LinearLayout
    private lateinit var classIcon:ImageView
    private lateinit var raceIcon:ImageView
    private lateinit var errorTV:TextView
    private lateinit var skillRecyclerView:RecyclerView
    private lateinit var loadingindicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body=view.findViewById(R.id.chardetailbody)
        errorTV=view.findViewById(R.id.chardetailserror)
        loadingindicator=view.findViewById(R.id.chardetailsloading)

        nameTextView = view.findViewById(R.id.charnametxt)
        levelTextView = view.findViewById(R.id.lvltext)
        classTextView = view.findViewById(R.id.classtext)
        raceTextView = view.findViewById(R.id.racetxt)
        classIcon = view.findViewById(R.id.classicon)
        raceIcon=view.findViewById(R.id.raceicon)

        skillRecyclerView = view.findViewById(R.id.rv_skill_list)
        skillRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        skillRecyclerView.setHasFixedSize(true)
        skillRecyclerView.itemAnimator = null;

        skillRecyclerView.adapter=adapter

        viewModel.loading.observe(viewLifecycleOwner){loading->
            if(loading!=null){
                Log.d(tag,"loading")
                body.visibility = View.INVISIBLE
                errorTV.visibility=View.INVISIBLE
                loadingindicator.visibility=View.VISIBLE
            }
        }

        viewModel.characterInfo.observe(viewLifecycleOwner){characterInfo->
            if(characterInfo!=null){
                bind(characterInfo)
                body.visibility = View.VISIBLE
                errorTV.visibility = View.INVISIBLE
                loadingindicator.visibility = View.INVISIBLE
            }
        }
        viewModel.error.observe(viewLifecycleOwner){error->
            if (error!=null){
                Log.d(tag,"error")
                body.visibility = View.INVISIBLE
                errorTV.visibility = View.VISIBLE
                loadingindicator.visibility = View.INVISIBLE
            }
        }

        raceTextView.setOnClickListener{
            val directions = CharacterDetailPageFragmentDirections.navToRacepage(raceTextView.text.toString())
            findNavController().navigate(directions)
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object :MenuProvider{
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.character_fragment_menu,menu)
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when(menuItem.itemId){
                        R.id.action_share -> {
                            share()
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED

        )
    }

    private fun share() {
        val bitmap = takeScreenShot(activity?.window?.decorView?.getRootView()!!)
        val bitmapPath = Images.Media.insertImage(activity?.contentResolver, bitmap, "title", null)
        Log.d(tag,bitmapPath)
        val bitMapURI = Uri.parse(bitmapPath)
        val intent: Intent = Intent().apply{
            action= Intent.ACTION_SEND
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM,bitMapURI)
        }
        startActivity(Intent.createChooser(intent,null))
    }

    private fun bind(characterInfo: CharacterInformation) {
        var classIconPath = classIconSelect(characterInfo.profession)
        var raceIconPath = raceIconSelect(characterInfo.race)
        nameTextView.text = characterInfo.name
        levelTextView.text = characterInfo.level.toString()
        raceTextView.text = characterInfo.race
        classTextView.text=characterInfo.profession
        var skillsQuery:String = skillsQuerySetup(characterInfo)
        charSkillsViewModel.loadSkillInformation_VM(skillsQuery)
        Log.d(tag,skillsQuery)
        charSkillsViewModel.skillInfo.observe(viewLifecycleOwner){
            skillInfo->
            if(skillInfo!=null){
                var skillList: List<SkillInformation> = arraySort(skillInfo)
                adapter.updateSkillList(skillList)
            }
            Log.d(tag,charSkillsViewModel.skillInfo.value.toString())
        }
        Glide.with(this)
            .load(classIconPath)
            .into(classIcon)
        Glide.with(this)
            .load(raceIconPath)
            .into(raceIcon)
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

    private fun takeScreenShot(view: View):Bitmap{
        val bitmap=Bitmap.createBitmap(view.width,
           view.height, Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        view.draw(canvas)
        return bitmap
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
    private fun skillsQuerySetup(characterInfo:CharacterInformation):String{
        var skillsQuery:String = characterInfo.skills.pveSkills.healSkill.toString()
        skillsQuery=skillsQuery.plus(",")
        skillsQuery=skillsQuery.plus(characterInfo.skills.pvpSkills.healSkill.toString())
        skillsQuery=skillsQuery.plus(",")
        skillsQuery=skillsQuery.plus(characterInfo.skills.wvwSkills.healSkill.toString())

        Log.d(tag, "Index 0:${characterInfo.skills?.pveSkills?.utilSkills?.get(index = 0)}")


        for (i in 0..<characterInfo.skills.pveSkills.utilSkills.size) {
            if (characterInfo.skills.pveSkills.utilSkills.get(index = i) != null) {
                skillsQuery = skillsQuery.plus(",")
                skillsQuery = skillsQuery.plus(characterInfo.skills.pveSkills.utilSkills.get(index = i).toString())
            }
        }
        for (i in 0..<characterInfo.skills.pvpSkills.utilSkills.size) {
            if (characterInfo.skills.pvpSkills.utilSkills.get(index = i) != null) {
                skillsQuery = skillsQuery.plus(",")
                skillsQuery = skillsQuery.plus(characterInfo.skills.pvpSkills.utilSkills.get(index = i).toString())
            }
        }
        for (i in 0..<characterInfo.skills.wvwSkills.utilSkills.size) {
            if (characterInfo.skills.wvwSkills.utilSkills.get(index = i) != null) {
                skillsQuery = skillsQuery.plus(",")
                skillsQuery = skillsQuery.plus(characterInfo.skills.wvwSkills.utilSkills.get(index = i).toString())
            }
        }



//        for (i in characterInfo.skills.pveSkills.utilSkills.filterNotNull()){
////            skillsQuery = skillsQuery.plus(",")
////            Log.d(tag, "Skill for guouo:${i}")
////            skillsQuery = skillsQuery.plus(i.toString())
//
////        }
//        for (i in characterInfo.skills.pvpSkills.utilSkills){
//            skillsQuery=skillsQuery.plus(",")
//            skillsQuery=skillsQuery.plus(i.toString())
//        }
//        for (i in characterInfo.skills.wvwSkills.utilSkills){
//            skillsQuery=skillsQuery.plus(",")
//            skillsQuery=skillsQuery.plus(i.toString())
//
//        }
        return skillsQuery
    }

    private fun classIconSelect(currClass:String):String{
        var classIconPath:String
        if(currClass=="Engineer"){
            classIconPath="https://wiki.guildwars2.com/images/5/53/Engineer_icon_%28highres%29.png"
        }
        else if(currClass=="Elementalist"){
            classIconPath="https://wiki.guildwars2.com/images/thumb/c/c0/Elementalist_icon_%28highres%29.png/384px-Elementalist_icon_%28highres%29.png"
        }
        else if(currClass=="Guardian"){
            classIconPath="https://wiki.guildwars2.com/images/thumb/3/30/Guardian_icon_%28highres%29.png/384px-Guardian_icon_%28highres%29.png"
        }
        else if(currClass=="Mesmer"){
            classIconPath="https://wiki.guildwars2.com/images/thumb/0/0a/Mesmer_icon_%28highres%29.png/384px-Mesmer_icon_%28highres%29.png"
        }
        else if(currClass=="Necromancer"){
            classIconPath="https://wiki.guildwars2.com/images/thumb/2/27/Necromancer_icon_%28highres%29.png/384px-Necromancer_icon_%28highres%29.png"
        }
        else if(currClass=="Ranger"){
            classIconPath="https://wiki.guildwars2.com/images/thumb/c/c7/Ranger_icon_%28highres%29.png/384px-Ranger_icon_%28highres%29.png"
        }
        else if(currClass=="Revenant"){
            classIconPath ="https://wiki.guildwars2.com/images/thumb/2/2d/Revenant_icon_%28highres%29.png/384px-Revenant_icon_%28highres%29.png"
        }
        else if(currClass=="Thief"){
            classIconPath="https://wiki.guildwars2.com/images/thumb/3/31/Thief_icon_%28highres%29.png/384px-Thief_icon_%28highres%29.png"
        }
        else{
            classIconPath="https://wiki.guildwars2.com/images/thumb/7/76/Warrior_icon_%28highres%29.png/384px-Warrior_icon_%28highres%29.png"
        }
        return classIconPath
    }
    private fun onSkillClick(skillInformation: SkillInformation){
        val directions = CharacterDetailPageFragmentDirections.navToSkillpage(skillInformation.id.toString())
        findNavController().navigate(directions)
    }
    override fun onStart() {
        super.onStart()
        Log.d(tag, "Args.Name = ${args.name}")
        viewModel.loadCharDetailInformation_VM(args.name, getString(R.string.api_key_Quinn))
    }
}