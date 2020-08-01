package com.wayne.shop3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verifyEmail.setOnClickListener {
            Log.d("TAG", "onViewCreated: Send")
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()?.addOnCompleteListener { task->
                if(task.isSuccessful){
                    Snackbar.make(it,"Verify Success", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        button_first.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null){
            verifyText.text = "${FirebaseAuth.getInstance().currentUser?.email} / ${FirebaseAuth.getInstance().currentUser?.isEmailVerified}"
            if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                verifyEmail.visibility = View.GONE
            } else{
                verifyEmail.visibility = View.VISIBLE
            }
        }
    }
}