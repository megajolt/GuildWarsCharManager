package edu.oregonstate.cs492.assignment4.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController
import edu.oregonstate.cs492.assignment4.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginPageFragment : Fragment(R.layout.fragment_login_page) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val tag = "LoginPageFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.loginbutton)
        button?.setOnClickListener{
            val directions = LoginPageFragmentDirections.navToAccountpage()
            Log.d(tag, "Login Button Pushed")
            findNavController().navigate(directions)
        }
    }


}