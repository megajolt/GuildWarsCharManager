package edu.oregonstate.cs492.assignment4.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.SkillInformation
import edu.oregonstate.cs492.assignment4.data.SkillInformationViewModel

class SkillPageFragment : Fragment(R.layout.fragment_skill_page) {
    private val viewModel:SkillInformationViewModel by viewModels()
    private val args:SkillPageFragmentArgs by navArgs()
    private val tag = "SkillPageFragment"

    private lateinit var body: LinearLayout
    private lateinit var errorTV: TextView
    private lateinit var loadingindicator: CircularProgressIndicator
    private lateinit var descriptionTextView: TextView
    private lateinit var skillNameTextView: TextView
    private lateinit var skillIconImageView: ImageView
    private lateinit var rangeTextView: TextView
    private lateinit var rechargeTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body = view.findViewById(R.id.skillbody)
        errorTV = view.findViewById(R.id.skillinfoerror)
        loadingindicator = view.findViewById(R.id.skillinfoloading)
        descriptionTextView=view.findViewById(R.id.skill_description)
        skillIconImageView = view.findViewById(R.id.skill_icon)
        skillNameTextView = view.findViewById(R.id.skill_text)
        rangeTextView = view.findViewById(R.id.range_of_skill)
        rechargeTextView = view.findViewById(R.id.recharge_of_skill)

        viewModel.loading.observe(viewLifecycleOwner){loading->
            if(loading!=null){
                Log.d(tag,"loading")
                body.visibility = View.INVISIBLE
                errorTV.visibility=View.INVISIBLE
                loadingindicator.visibility=View.VISIBLE
            }
        }

        viewModel.skillInfo.observe(viewLifecycleOwner){skillInfo->
            if(skillInfo!=null){
                Log.d(tag,"skillInfo filled")
                bind(skillInfo[0])
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
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadSkillInformation_VM(args.id)
    }
    private fun bind(skillInformation: SkillInformation){
        descriptionTextView.text = skillInformation.description
        Glide.with(this)
            .load(skillInformation.icon)
            .into(skillIconImageView)
        skillNameTextView.text = skillInformation.name
        for(i in skillInformation.facts!!){
            if(i.type=="Range"){
                rangeTextView.text = i.value.toString()
            }
            if(i.type=="Recharge"){
                rechargeTextView.text = i.value.toString()
            }
        }
    }
}