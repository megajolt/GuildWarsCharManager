package edu.oregonstate.cs492.assignment4.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.AccountInformationViewModel
import edu.oregonstate.cs492.assignment4.data.CharacterInformation
import edu.oregonstate.cs492.assignment4.data.SkillInformation

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountPageFragment : Fragment(R.layout.fragment_profile_page) {
    private val viewModel: AccountInformationViewModel by viewModels()

    private val tag = "AccountPageFragment"

    //View declarations
    private lateinit var usernameTV:TextView
    private lateinit var body : LinearLayout
    private lateinit var errorTV:TextView
    private lateinit var loadingindicator:CircularProgressIndicator
    private lateinit var guildListRV:RecyclerView
    private lateinit var characterListRV:RecyclerView
    private lateinit var homeWorldTV:TextView
    val adapter = GuildsAdapter()
    val charAdapter = CharListAdapter(::onCharacterClick)
    lateinit var api_key:String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Initializing the views
        super.onViewCreated(view, savedInstanceState)
        errorTV = view.findViewById(R.id.loaderror_accountpage)
        usernameTV = view.findViewById(R.id.username)
        loadingindicator = view.findViewById((R.id.progress_accountpage))
        body = view.findViewById(R.id.accountpage_body)
        guildListRV = view.findViewById(R.id.guildsListRV)
        guildListRV.layoutManager = LinearLayoutManager(requireContext())
        guildListRV.setHasFixedSize(false)

        characterListRV = view.findViewById(R.id.characterListRV)
        characterListRV.layoutManager = LinearLayoutManager(requireContext())
        characterListRV.setHasFixedSize(false)
        homeWorldTV = view.findViewById(R.id.TV_homeworld)


        characterListRV.adapter = charAdapter
        guildListRV.adapter = adapter

        characterListRV.itemAnimator=null
        guildListRV.itemAnimator=null



        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        val listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == "charsort") {
                    val sort = prefs.getString("charsort", "Name")
                    viewModel.loadAccountInformation_VM(api_key, sort)
                }
                Log.d(tag, "Settings change detected")
           }


        prefs.registerOnSharedPreferenceChangeListener(listener)

        viewModel.loading.observe(viewLifecycleOwner){loading ->
            if(loading == true ){
                body.visibility = View.INVISIBLE
                errorTV.visibility=View.INVISIBLE
                loadingindicator.visibility = View.VISIBLE
            }
            else{
                body.visibility = View.VISIBLE
                errorTV.visibility=View.INVISIBLE
                loadingindicator.visibility = View.INVISIBLE
            }
        }


        viewModel.accountinfo.observe(viewLifecycleOwner){accountinfo ->
            if(accountinfo!=null){
                updateUsername(accountinfo.name)
                viewModel.checkWorldName_VM(accountinfo.world)
                Log.d(tag, "Guilds: " + accountinfo.guilds.toString())
                viewModel.getGuildName_VM(accountinfo.guilds)
            }
            viewModel.updateLoading()
        }

        viewModel.world.observe(viewLifecycleOwner){ world ->
            if(world!=null) {
                homeWorldTV.text = world.name
            }
            viewModel.updateLoading()
        }

        viewModel.characters.observe(viewLifecycleOwner){characters ->
            if(characters!=null){
                charAdapter.updateCharactersList(characters)
            }
            Log.d(tag, "Detected a change in the characters, updating now")
            viewModel.updateLoading()
        }

        viewModel.guilds.observe(viewLifecycleOwner){guilds ->
            if(guilds!=null){
                adapter.updateGuildsList(guilds)
            }
            viewModel.updateLoading()
        }


        viewModel.error.observe(viewLifecycleOwner){error ->
            if(error != null){
                errorTV.visibility=View.VISIBLE
                body.visibility = View.INVISIBLE
                loadingindicator.visibility = View.INVISIBLE

            }
        }



    }

    override fun onStart() {
        super.onStart()
        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val sort = prefs.getString("charsort", "name")
        api_key =  getString(R.string.api_key_Quinn)
        viewModel.loadAccountInformation_VM(api_key,prefs.getString("charsort", "name"))

    }




    fun updateUsername(newName:String) {
        usernameTV.text = newName
    }
    private fun onCharacterClick(characterInformation: CharacterInformation){
        val directions = AccountPageFragmentDirections.navToCharacterdetailspage(characterInformation.name)
        findNavController().navigate(directions)
    }




}